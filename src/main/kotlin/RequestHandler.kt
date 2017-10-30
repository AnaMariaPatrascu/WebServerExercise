import java.net.Socket

class RequestHandler(private val socket: Socket) : Runnable {

    override fun run() {
        //TODO parse to HttpRequest object
        val reader = socket.getInputStream().bufferedReader()

        var length = 0

        reader.lineSequence()
                .takeWhile { it.isNotBlank() }
                .forEach {
                    println(it)
                    if (it.startsWith("Content-Length:")) {
                        length = it.removePrefix("Content-Length:").trim().toInt()
                    }
                }
        reader.lines().skip(1)

        if (length > 0) {
            println()

            val content = CharArray(length)
            reader.read(content)

            println(String(content))
        }

        //TODO handle request and get HttpResponse as answer

        //TODO write HttpResponse to output stream
        with(socket.getOutputStream().writer()) {
            write("HTTP/1.1 200 OK")
            flush()
        }

        socket.close()
    }
}