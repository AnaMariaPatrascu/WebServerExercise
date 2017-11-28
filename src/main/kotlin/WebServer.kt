import java.net.*
import java.util.concurrent.*

class WebServer {
    private val executor: ExecutorService = Executors.newCachedThreadPool()
    private var serverSocket: ServerSocket = ServerSocket(0)

    fun getPort(): Int {
        return serverSocket.localPort
    }

    fun start() {
        executor.execute {
            while (!serverSocket.isClosed) {
                try {
                    val socket = serverSocket.accept()
                    executor.execute(ClientHandler(socket))
                } catch (e: Exception) {
                    println("Client connection failed: $e")
                }
            }
        }
    }

    fun stop() {
        serverSocket.close()
        executor.shutdownNow()
    }
}

fun main(args: Array<String>) {
    val server = WebServer()
    server.start()
}