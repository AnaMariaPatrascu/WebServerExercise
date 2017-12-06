package http

import java.io.OutputStream

class HttpResponse(private val httpVersion: String,
                   private val statusCode: Int,
                   private val statusDescription: String,
                   private val headers: Map<String, String> = mapOf(),
                   private val body: String?) {

    fun writeTo(output: OutputStream) {
        with(output.writer()) {
            write(toHttpString())
            flush()
        }
    }

    private fun toHttpString(): String {
        return buildString {
            append("$httpVersion $statusCode $statusDescription\r\n")
            headers.forEach { append("${it.key}:${it.value}\r\n") }
            append("\r\n")
            append(body)
        }
    }
}