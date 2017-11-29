import java.net.URL

enum class HttpMethod {
    GET, PUT, POST, PATCH, TRACE, OPTIONS, DELETE, CONNECT, HEAD
}

data class HttpRequest (val method: HttpMethod,
                        val URI: URL,
                        val httpVersion: String,
                        val headers: Map<String, String> = mapOf(),
                        val body: String?)