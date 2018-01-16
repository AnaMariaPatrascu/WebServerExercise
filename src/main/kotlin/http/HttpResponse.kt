package http

import java.io.*

class HttpResponse(private val httpVersion: String,
                   private val statusCode: Int,
                   private val statusDescription: String,
                   private val headers: Map<String, String> = mapOf(),
                   private val body: InputStream?) {

    fun writeTo(output: OutputStream) {
        output.write(toHttpString().toByteArray(Charsets.ISO_8859_1))
        body?.copyTo(output)
    }

    private fun toHttpString(): String {
        return buildString {
            append("$httpVersion $statusCode $statusDescription\r\n")
            headers.forEach { append("${it.key}:${it.value}\r\n") }
            append("\r\n")
        }
    }
}