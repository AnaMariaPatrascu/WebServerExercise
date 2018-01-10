package server

import http.*
import mu.*
import java.net.*
import java.util.concurrent.*

const val DEFAULT_PORT = 8080

class WebServer(port: Int = DEFAULT_PORT,
                private val requestParser: RequestParser,
                private val requestHandler: RequestHandler) {

	private val executor: ExecutorService = Executors.newCachedThreadPool()

	// how do we deals with errors when we cannot open the server socket?
	// we should process common errors and give the user a tip how to resolve these issues
	// ==> i think user should surround crate() call with try catch....but I am not sure if is the best approach...
	private var serverSocket: ServerSocket = ServerSocket(port)

	companion object: KLogging() {
		fun create(port: Int, routes: List<Pair<String, RouteContent>>): WebServer {
			val parser = HttpRequestParser()

			val router = HttpRouter()
			routes.forEach { router.addNewRoute(it.first, it.second) }

			val handler = HttpRequestHandler(router)

			try{
				return WebServer(port, requestParser = parser, requestHandler = handler)
			}catch (e: Exception) {
				logger.error {"Failed to create web server instance: ${e.message}"}
				throw e
			}
		}
	}

	// I think a property delegated to `serverSocket.localPort` would be more idiomatic, but that's style :-)
	fun getPort(): Int {
		return serverSocket.localPort
	}

	fun start() {
		// is this on purpose?
		// if `start()` is called multiple times, it executes multiple threads calling the same accept method
		executor.execute {
			while (!serverSocket.isClosed) {
				try {
					val socket = serverSocket.accept()
					executor.execute(ClientHandler(socket, requestParser, requestHandler))
				} catch (e: Exception) {
					// we should check whether the exception was triggered by closing the server socket
					// we shouldn't pollute the log with exceptions if it's a normal use case
					logger.error{"Something went wrong: $e"}
					// --> use a logger...
				}
			}
		}
	}

	fun stop() {
		try {
			serverSocket.close()
			executor.shutdownNow()
		} catch (e: Exception) {
			logger.error{"Closing server failed: $e"}
		}
	}
}
