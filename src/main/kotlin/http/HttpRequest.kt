package http

enum class HttpMethod {
	GET, PUT, POST, PATCH, TRACE, OPTIONS, DELETE, CONNECT, HEAD
}

data class HttpRequest(val method: HttpMethod,
					   val URI: String,
					   val httpVersion: String,
					   val headers: Map<String, String> = mapOf(),
					   val body: String?)