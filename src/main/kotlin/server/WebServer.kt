package server

import http.*
import java.net.*
import java.util.concurrent.*

const val DEFAULT_PORT = 8080

class WebServer(port: Int = DEFAULT_PORT,
                private val requestParser: RequestParser,
                private val requestHandler: RequestHandler) {

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
                    executor.execute(ClientHandler(socket, requestParser, requestHandler))
                } catch (e: Exception) {
                    println("Something went wrong: $e")
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