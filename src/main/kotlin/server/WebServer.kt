package server

import http.*
import mu.*
import java.net.*
import java.util.concurrent.*

const val DEFAULT_PORT = 8080

class WebServer(private val serverSocket: ServerSocket,
                private val requestParser: RequestParser,
                private val requestHandler: RequestHandler) {

	private val executor: ExecutorService = Executors.newCachedThreadPool()

	private var started = false

	companion object: KLogging() {
		fun create(port: Int?, routes: List<Pair<String, RouteContent>>): WebServer {
			val parser = HttpRequestParser()

			val router = HttpRouter()
			routes.forEach { router.addNewRoute(it.first, it.second) }
			val handler = HttpRequestHandler(router)

			val serverSocket = try {
				ServerSocket(port ?: DEFAULT_PORT)
			}catch (e: Exception){
				throw Exception("Failed to create server socket at port ${port ?: DEFAULT_PORT}!", e)
			}

			try{
				return WebServer(serverSocket, requestParser = parser, requestHandler = handler)
			}catch (e: Exception) {
				throw Exception("Failed to create web server instance!", e)
			}
		}
	}

	// I think a property delegated to `serverSocket.localPort` would be more idiomatic, but that's style :-)
	fun getPort(): Int {
		return serverSocket.localPort
	}

	@Synchronized
	fun start() {
		// is this on purpose?
		// if `start()` is called multiple times, it executes multiple threads calling the same accept method
		if (started) return
		started = true

		executor.execute {
			while (!serverSocket.isClosed) {
				try {
					val socket = serverSocket.accept()
					executor.execute(ClientHandler(socket, requestParser, requestHandler))
				} catch (e: Exception) {
					// we should check whether the exception was triggered by closing the server socket
					// we shouldn't pollute the log with exceptions if it's a normal use case
					if(!serverSocket.isClosed){
						logger.error{"Something went wrong: $e"}
					}
				}
			}

			logger.info("Server stopped!")
		}
	}

	@Synchronized
	fun stop() {
		try {
			serverSocket.close()
			executor.shutdownNow()
		} catch (e: Exception) {
			logger.error{"Closing server failed: $e"}
		}
	}
}
