package http

import java.io.*

interface RequestParser {
	fun parseRequest(input: InputStream): HttpRequest
}

class HttpRequestParser : RequestParser {

	override fun parseRequest(input: InputStream): HttpRequest {
		val reader = input.bufferedReader()

		val firstLine = parseFirstLine(reader)
		val headers = parseHeaders(reader)
		reader.lines().skip(1)
		val body = parseBody(reader, headers.get("Content-Length")?.replace(" ", "")?.toInt() ?: 0)

		return HttpRequest(firstLine!!.method, firstLine.uri, firstLine.httpVersion, headers, body)
	}

	private fun parseFirstLine(reader: BufferedReader): FirstRequestLine? {
		try {
			val firstLine = reader.lines().findFirst().get().split(' ')
			if (firstLine.size != 3) {
				throw HttpRequestParseException("First request line should contain three parameters!")
			}

			val method = HttpMethod.valueOf(firstLine[0])
			val uri = firstLine[1]

			val httpVersion = firstLine[2]
			if (httpVersion != "HTTP/1.1") {
				throw HttpRequestParseException("$httpVersion is not a valid http version!")
			}

			return FirstRequestLine(method, uri, httpVersion)
		} catch (e: Exception) {
			throw HttpRequestParseException("First request line does not fit to the http format! ${e.message}")
		}
	}

	private fun parseHeaders(reader: BufferedReader): Map<String, String> {
		val headers = mutableMapOf<String, String>()
		reader.lineSequence()
				.takeWhile { it.isNotBlank() }
				.forEach {
					val elements = it.split(":".toRegex(), 2)
					if (elements.size < 2) {
						throw HttpRequestParseException("$it header does not have expected format Name: Value")
					}
					headers.put(elements[0], elements[1])
				}
		return headers
	}

	private fun parseBody(reader: BufferedReader, length: Int): String? {
		var body: String? = null
		if (length > 0) {
			val content = CharArray(length)
			reader.read(content)

			body = String(content)
		}
		return body
	}
}

data class FirstRequestLine(val method: HttpMethod,
							val uri: String,
							val httpVersion: String)

class HttpRequestParseException(override var message: String): RuntimeException()