import java.net.*
import java.util.concurrent.*

const val DEFAULT_PORT = 8080

class WebServer(port: Int = DEFAULT_PORT,
				private val requestParser: RequestParser,
				private val requestInterpreter: RequestInterpreter) {

    private val executor: ExecutorService = Executors.newCachedThreadPool()
    private var serverSocket: ServerSocket = ServerSocket(port)

    fun getPort(): Int {
        return serverSocket.localPort
    }

    fun start() {
        executor.execute {
            while (!serverSocket.isClosed) {
                try {
                    val socket = serverSocket.accept()
                    executor.execute(ClientHandler(socket, requestParser, requestInterpreter))
                } catch (e: Exception) {
                    println("Client connection failed: $e")
                }
            }
        }
    }

    fun stop() {
        try {
            serverSocket.close()
            executor.shutdownNow()
        } catch (e: Exception) {
            println("Closing server failed: $e")
        }
    }
}

fun main(args: Array<String>) {
    val parser = HttpRequestParser()
    val interpreter = HttpRequestInterpreter()
	val port = 8081

    interpreter.addNewRoute("/hello", RouteContent("Hello world!", ContentType.PLAIN))
    interpreter.addNewRoute("/ana", RouteContent("Hello Ana!", ContentType.PLAIN))
    interpreter.addNewRoute("/html", RouteContent("<html><body><h1>Hello, World!</h1></body></html>", ContentType.HTMl))

    val server = WebServer(port, requestParser = parser, requestInterpreter = interpreter)
    server.start()
}