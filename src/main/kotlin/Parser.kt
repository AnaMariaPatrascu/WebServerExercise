import java.io.*
import java.net.*

class Parser {
	fun parseRequest(input: InputStream): HttpRequest? {
		//TODO
		return HttpRequest(HttpMethod.GET, URL("http://localhost:8081"), "", mapOf(), null)
	}
}