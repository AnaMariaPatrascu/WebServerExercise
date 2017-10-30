import java.net.ServerSocket
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class WebServer {

    private val executor: ExecutorService = Executors.newCachedThreadPool()
    private lateinit var serverSocket: ServerSocket

    fun getPort(): Int {
        return serverSocket.localPort
    }

    fun start(port: Int) {
        serverSocket = ServerSocket(port)

        executor.execute {
            while (!serverSocket.isClosed) {
                try {
                    val socket = serverSocket.accept()
                    executor.execute(RequestHandler(socket))
                } catch (e: Exception) {
                    println("Client connection failed: $e")
                }
            }
        }
    }


    fun stop() {
        serverSocket.close()

        executor.shutdown()
        if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
            executor.shutdownNow()
            executor.awaitTermination(30, TimeUnit.SECONDS)
        }
    }
}

fun main(args: Array<String>) {
    val server = WebServer()
    server.start(8080)
}