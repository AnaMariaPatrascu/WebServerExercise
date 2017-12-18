package http

import java.io.*

enum class HttpMethod {
	GET, PUT, POST, PATCH, TRACE, OPTIONS, DELETE, CONNECT, HEAD
}

class HttpRequest(val method: HttpMethod,
					   val URI: String,
					   val httpVersion: String,
					   val headers: Map<String, String> = mapOf(),
					   val body: InputStream){

	fun readBodyContent(): String {
		val size = headers.get("Content-Length")?.toInt() ?: 0
		return body.bufferedReader().readText().substring(0, size)
	}
}