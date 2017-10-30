
data class HttpResponse (private val httpVersion: String,
                   private val statusCode: Int,
                   private val statusDescription: String,
                   private val headers: List< Pair<String, String> > = emptyList(),
                   private val body: String?)