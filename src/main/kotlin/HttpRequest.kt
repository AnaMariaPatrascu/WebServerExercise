import java.net.URL

enum class HttpMethod {
    GET, PUT, POST, PATCH, TRACE, OPTIONS, DELETE, CONNECT, HEAD
}

data class HttpRequest (private val method: HttpMethod,
                  private val URI: URL,
                  private val httpVersion: String,
                  private val headers: Map<String, String> = mapOf(),
                  private val body: String?)