import java.io.*
import java.net.*

class ClientHandler(private val socket: Socket,
					private val requestParser: RequestParser,
					private val interpreter: RequestInterpreter) : Runnable {

	override fun run() {
		var response = try {
			val request = requestParser.parseRequest(socket.getInputStream())
			interpreter.interpretRequest(request)
		}catch (e: HttpRequestParseException){
			HttpResponse("HTTP/1.1", 400, "Bad Request", mapOf(), null)
		} catch (e: Exception) {
			HttpResponse("HTTP/1.1", 500, "Internal Server Error", mapOf(), null)
		}

		try {
			response.writeTo(socket.getOutputStream())
		} catch (e: IOException) {
			throw Exception("Failed during writing response to output stream $e")
		} finally {
			try {
				socket.close()
			}catch (e: IOException){
				println("Error during closing socket connection $e")
			}
		}
	}
}