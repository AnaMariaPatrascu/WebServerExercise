import http.*
import server.*
import org.junit.*
import java.io.*
import java.net.*

class WebServerTest {

	private lateinit var server: WebServer

    @Before
    fun setUp() {
		val port = 0
		val routes = listOf("/hello" to RouteContentHelloWorld(),
				"/ana" to RouteContentHelloAna(),
				"/html" to RouteContentHtml())

		server = WebServer.create(port, routes )
		server.start()
    }

    @After
    fun tearDown() {
        server.stop()
    }

	@Test
	fun `should accept client connections`() {
		val socket = Socket("localhost", server.getPort())
		Thread.sleep(1000)
		socket.close()
	}

	@Test
	fun `should happen nothing wrong when calling stop without start`() {
		server.stop()
		server.stop()
	}

	@Test
	fun `should return hello world`() {
		Socket("localhost", server.getPort()).use { socket ->
			val writer = socket.getOutputStream().writer()
			writer.write("GET /hello $HTTP_VERSION\r\n\r\n")
			writer.flush()

			val reader = socket.getInputStream()

			val response = reader.bufferedReader().readText()
			Assert.assertTrue(response.contains("Hello, World!"))
			socket.close()
		}
	}

	@Test
	fun `should return hello name`() {
		Socket("localhost", server.getPort()).use { socket ->
			val writer = socket.getOutputStream().writer()
			writer.write("GET /ana $HTTP_VERSION\r\n\r\n")
			writer.flush()

			val reader = socket.getInputStream()
			val response = reader.bufferedReader().readText()
			Assert.assertTrue(response.contains("Hello, Ana!"))
			socket.close()
		}
	}

	@Test
	fun `should return bad request`() {
		Socket("localhost", server.getPort()).use { socket ->
			val writer = socket.getOutputStream().writer()
			writer.write("very /bad request\r\n\r\n")
			writer.flush()

			val reader = socket.getInputStream()
			val response = reader.bufferedReader().readLine()
			Assert.assertTrue(response.contains("400"))
			socket.close()
		}
	}

}

class RouteContentHelloWorld : RouteContent {
	override fun customizedRouteContent(request: HttpRequest): HttpResponse {
		val bodyMessage = "Hello, World!"
		val contentType = ContentType.PLAIN

		return HttpResponse(HTTP_VERSION,
				200,
				"OK",
				mapOf(CONTENT_LENGTH to "${bodyMessage.length}", CONTENT_TYPE to contentType.type),
				ByteArrayInputStream(bodyMessage.toByteArray(Charsets.ISO_8859_1)))
	}
}

class RouteContentHelloAna : RouteContent {
	override fun customizedRouteContent(request: HttpRequest): HttpResponse {
		val bodyMessage = "Hello, Ana!"
		val contentType = ContentType.PLAIN

		return HttpResponse(HTTP_VERSION,
				200,
				"OK",
				mapOf(CONTENT_LENGTH to "${bodyMessage.length}", CONTENT_TYPE to contentType.type),
				ByteArrayInputStream(bodyMessage.toByteArray(Charsets.ISO_8859_1)))
	}
}

class RouteContentHtml : RouteContent {
	override fun customizedRouteContent(request: HttpRequest): HttpResponse {
		val bodyMessage = "<html><body><h1>Hello, World!</h1></body></html>"
		val contentType = ContentType.HTMl

		return HttpResponse(HTTP_VERSION,
				200,
				"OK",
				mapOf(CONTENT_LENGTH to "${bodyMessage.length}", CONTENT_TYPE to contentType.type),
				ByteArrayInputStream(bodyMessage.toByteArray(Charsets.ISO_8859_1)))
	}
}