import java.net.*
import java.util.concurrent.*

const val DEFAULT_PORT = 8080

class WebServer(port: Int = DEFAULT_PORT,
				private val requestParser: RequestParser,
				private val requestInterpreter: RequestInterpreter,
				private val pathMapper: PathMapper) {

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
                    executor.execute(ClientHandler(socket, requestParser, requestInterpreter, pathMapper))
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

	val mapper = PathMapper()
	mapper.add("/hello", "Hello world!")
	mapper.add("/ana", "Hello Ana!")

    val server = WebServer(port, requestParser = parser, requestInterpreter = interpreter, pathMapper = mapper)
    server.start()
}