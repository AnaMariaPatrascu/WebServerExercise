import java.io.*
import java.net.*

class ClientHandler(private val socket: Socket) : Runnable {
	private val parser = Parser()
	private val interpreter = RequestInterpreter()

	override fun run() {
		val request = parser.parseRequest(socket.getInputStream())
		val response = if (request != null) interpreter.interpretRequest(request)
								   else HttpResponse("HTTP/1.1", 400, "Bad Request", mapOf(), null)
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