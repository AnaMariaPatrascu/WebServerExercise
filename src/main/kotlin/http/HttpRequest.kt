package http

import java.io.*

enum class HttpMethod {
	GET, PUT, POST, PATCH, TRACE, OPTIONS, DELETE, CONNECT, HEAD
}

const val CONTENT_LENGTH = "Content-Length"
const val CONTENT_TYPE = "Content-Type"

const val HTTP_VERSION = "HTTP/1.0"

class HttpRequest(val method: HttpMethod,
					   val URI: String,
					   val httpVersion: String,
					   val headers: Map<String, String> = mapOf(),
					   val body: InputStream){

	fun readBodyContent(): String {
		val size = headers.get(CONTENT_LENGTH)?.toInt() ?: 0
		return body.bufferedReader().readText().substring(0, size)
	}
}