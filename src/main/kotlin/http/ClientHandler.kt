package http

import mu.*
import java.io.*
import java.net.*

class ClientHandler(private val socket: Socket,
					private val requestParser: RequestParser,
					private val handler: RequestHandler) : Runnable {

	companion object: KLogging()

	override fun run() {
		var response = try {
			val request = requestParser.parseRequest(socket.getInputStream())
			handler.handleRequest(request)
		} catch (e: HttpRequestParseException){
			HttpResponse(HTTP_VERSION, 400, "Bad Request", mapOf(), null)
		} catch (e: Exception) {
			HttpResponse(HTTP_VERSION, 500, "Internal Server Error", mapOf(), null)
		}

		try {
			response.writeTo(socket.getOutputStream())
		} catch (e: IOException) {
			throw Exception("Failed during writing response to output stream $e")
		} finally {
			try {
				// you're implementing HTTP/1.1 which SHOULD not close the connection
				// from the spec: A significant difference between HTTP/1.1 and earlier versions of HTTP
				//                is that persistent connections are the default behavior of any HTTP
				//                connection. That is, unless otherwise indicated, the client SHOULD
				//                assume that the server will maintain a persistent connection, even
				//                after error responses from the server.
				socket.close()
			}catch (e: IOException){
				logger.error {"Error during closing socket connection $e"}
			}
		}
	}
}