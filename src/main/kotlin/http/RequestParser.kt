package http

import java.io.*

interface RequestParser {
	fun parseRequest(input: InputStream): HttpRequest
}

class HttpRequestParser : RequestParser {

	override fun parseRequest(input: InputStream): HttpRequest {
		val reader = input.reader(Charsets.ISO_8859_1)

		val firstLine = parseFirstLine(reader)
		val headers = parseHeaders(reader)

		val body = HttpBodyInputStream(reader, headers[CONTENT_LENGTH]?.toIntOrNull() ?: 0)

		return HttpRequest(firstLine.method, firstLine.uri, firstLine.httpVersion, headers, body)
	}

	private fun parseFirstLine(reader: Reader): FirstRequestLine {
		try {
			val firstLine = reader.readNextLine()
			if (firstLine.isEmpty()) {
				throw HttpRequestParseException("Request first line is missing!")
			}
			val items = firstLine.split(' ')
			if (items.size != 3) {
				throw HttpRequestParseException("First request line should contain three parameters!")
			}

			val method = try {
				HttpMethod.valueOf(items[0])
			}catch (ie: IllegalArgumentException){
				throw HttpRequestParseException("Http method is not valid!")
			}

			val uri = items[1]
			val httpVersion = items[2]

			if (httpVersion != HTTP_VERSION) {
				// actually it could be 1.0, couldn't it? (theoretically even 2.0 even though nobody implemented support for it without TLS)
				throw HttpRequestParseException("$httpVersion is not supported! Only $HTTP_VERSION supported!")
			}

			return FirstRequestLine(method, uri, httpVersion)
		} catch (e: Exception) {
			throw HttpRequestParseException("First request line does not fit to the http format! ${e.message}")
		}
	}

	private fun parseHeaders(reader: Reader): Map<String, String> {
		val headers = mutableMapOf<String, String>()
		var line = reader.readNextLine()
		while (!line.isBlank()) {
			val elements = line.split(":".toRegex(), 2)
			if (elements.size < 2) {
				throw HttpRequestParseException("$line header does not have expected format Name: Value")
			}

			headers.put(elements[0], elements[1])
			line = reader.readNextLine()
		}
		return headers
	}
}

fun Reader.readNextLine(): String {
	var result = ""
	while (!result.contains("\n")) {
		val read = this.read()
		if (read == -1) break

		result += read.toChar()
	}

	return result.removeSuffix("\n").removeSuffix("\r")
}

data class FirstRequestLine(val method: HttpMethod,
							val uri: String,
							val httpVersion: String)

class HttpRequestParseException(override var message: String) : RuntimeException()


private class HttpBodyInputStream(private val delegate: Reader,
								  private var contentLength: Int) : InputStream() {

	override fun read(): Int {
		if (contentLength <= 0){
			return -1
		}
		contentLength--
		return delegate.read()
	}

}