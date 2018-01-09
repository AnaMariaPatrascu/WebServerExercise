package http

import java.io.*

interface RequestParser {
	fun parseRequest(input: InputStream): HttpRequest
}

class HttpRequestParser : RequestParser {

	override fun parseRequest(input: InputStream): HttpRequest {
		val firstLine = parseFirstLine(input)
		val headers = parseHeaders(input)

		val body = input

		// try to avoid !!
		return HttpRequest(firstLine!!.method, firstLine.uri, firstLine.httpVersion, headers, body)
	}

	// does this method have the correct return type?
	private fun parseFirstLine(input: InputStream): FirstRequestLine? {
		try {
			val firstLine = input.readNextLine()
			if (firstLine.isEmpty()) {
				throw HttpRequestParseException("Request first line is missing!")
			}
			val items = firstLine.split(' ')
			if (items.size != 3) {
				throw HttpRequestParseException("First request line should contain three parameters!")
			}

			val method = HttpMethod.valueOf(items[0])
			val uri = items[1]
			val httpVersion = items[2]

			if (httpVersion != "HTTP/1.1") {
				// actually it could be 1.0, couldn't it? (theoretically even 2.0 even though nobody implemented support for it without TLS)
				throw HttpRequestParseException("$httpVersion is not a valid http version!")
			}

			return FirstRequestLine(method, uri, httpVersion)
		} catch (e: Exception) {
			throw HttpRequestParseException("First request line does not fit to the http format! ${e.message}")
		}
	}

	private fun parseHeaders(input: InputStream): Map<String, String> {
		val headers = mutableMapOf<String, String>()
		var line = input.readNextLine()
		while (!line.isBlank()) {
			val elements = line.split(":".toRegex(), 2)
			if (elements.size < 2) {
				throw HttpRequestParseException("$line header does not have expected format Name: Value")
			}

			headers.put(elements[0], elements[1])
			line = input.readNextLine()
		}
		return headers
	}

//	private fun parseBody(reader: BufferedReader, length: Int): String? {
//		var body: String? = null
//		if (length > 0) {
//			val content = CharArray(length)
//			reader.read(content)
//
//			body = String(content)
//		}
//		return body
//	}
}

fun InputStream.readNextLine(): String {
	var result = ""
	while (!result.contains("\n")) {
		result = "$result${this.read().toChar()}" // character encoding?
	}
	return result.removeSuffix("\n").removeSuffix("\r")
}

data class FirstRequestLine(val method: HttpMethod,
							val uri: String,
							val httpVersion: String)

class HttpRequestParseException(override var message: String) : RuntimeException()