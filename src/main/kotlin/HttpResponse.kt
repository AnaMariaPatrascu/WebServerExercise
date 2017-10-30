import java.io.OutputStream

class HttpResponse(private val httpVersion: String,
                   private val statusCode: Int,
                   private val statusDescription: String,
                   private val headers: Map<String, String> = mapOf(),
                   private val body: String?) {

    fun writeTo(output: OutputStream) {
        with(output.writer()) {
            write("$httpVersion $statusCode $statusDescription $headers \r\n $body")
            flush()
        }
    }
}