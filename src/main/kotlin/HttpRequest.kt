enum class HttpMethod {
    GET, PUT, POST, PATCH, TRACE, OPTIONS, DELETE, CONNECT, HEAD
}

class HttpRequest (private val method: HttpMethod,
                  private val URI: String,
                  private val httpVersion: String,
                  private val headers: List< Pair<String, String> > = emptyList(),
                  private val body: String?) {

    fun parseRequest(message: String) : HttpRequest {
        //todo
        return HttpRequest(HttpMethod.GET, "", "HTTP/1.1", emptyList(), null)
    }

}