import java.io.BufferedReader
import java.io.InputStream
import java.net.Socket
import java.net.URL

class RequestHandler(private val socket: Socket) : Runnable {

    override fun run() {
            val request = parseRequest(socket.getInputStream())

            //TODO handle request
            val response = if (request != null) handleRequest(request) else HttpResponse("HTTP/1.1", 400, "Bad Request", mapOf(), null)

            //TODO write HttpResponse to output stream
            response.writeTo(socket.getOutputStream())

            socket.close()
    }

    private fun parseRequest(input: InputStream): HttpRequest? {
        return try {
            val reader = input.bufferedReader()

            val firstLine = parseFirstLine(reader)
            val headers = parseHeaders(reader)
            reader.lines().skip(1)
            val body = parseBody(reader, headers.get("Content-Length")?.toInt() ?: 0)

            //todo additional check if GET must have body != null...etc.

            HttpRequest(firstLine!!.method, firstLine.uri, firstLine.httpVersion, headers, body)
        } catch (e: Exception) {
            null
        }
    }

    private fun parseFirstLine(reader: BufferedReader): FirstRequestLine? {
        try {
            val firstLine = reader.lines().findFirst().get().split(' ')
            if (firstLine.size != 3) {
                throw IllegalArgumentException("First request line should contain three parameters!")
            }

            var method = HttpMethod.valueOf(firstLine[0])
            var uri = URL(firstLine[1])

            val httpVersion = firstLine[2]
            if (httpVersion != "HTTP/1.1") {
                throw IllegalArgumentException("$httpVersion is not a valid http version!")
            }

            return FirstRequestLine(method, uri, httpVersion)
        } catch (e: Exception) {
            throw IllegalArgumentException("First request line does not fit to the http format!")
        }
    }

    private fun parseHeaders(reader: BufferedReader): Map<String, String> {
        var headers = mutableMapOf<String, String>()
        reader.lineSequence()
                .takeWhile { it.isNotBlank() }
                .forEach {
                    val elements = it.split(':')
                    if (elements.size != 2) {
                        throw IllegalArgumentException("$it header does not have expected format Name: Value")
                    }
                    headers.put(elements[0], elements[1])
                }
        return headers
    }

    private fun parseBody(reader: BufferedReader, length: Int): String? {
        var body: String? = null
        if (length > 0) {
            val content = CharArray(length)
            reader.read(content)

            body = String(content)
        }
        return body
    }

    private fun handleRequest(request: HttpRequest): HttpResponse {
        return HttpResponse("HTTP/1.1", 200, "OK", mapOf(), null)
    }
}

data class FirstRequestLine(val method: HttpMethod,
                            val uri: URL,
                            val httpVersion: String)