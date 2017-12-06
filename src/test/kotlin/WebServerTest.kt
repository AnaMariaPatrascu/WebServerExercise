import org.junit.*
import java.net.*

class WebServerTest {

	private val server = server.WebServer(8081, HttpRequestParser(), HttpRequestInterpreter())
//
//    @Before
//    fun setUp() {
//        server.start()
//    }
//
//    @After
//    fun tearDown() {
//        server.stop()
//    }

	@Test
	fun `should accept client connections`() {
		server.start()

		val socket = Socket("localhost", server.getPort())
		Thread.sleep(1000)
		socket.close()

		server.stop()
	}

	@Test
	fun `should happen nothing wrong when calling stop without start`() {
		server.stop()
	}

	@Test
	fun `should return hello world`() {
		server.start()
		Socket("localhost", server.getPort()).use { socket ->
			val writer = socket.getOutputStream().writer()
			writer.write("GET /hello HTTP/1.1\n\n")
			writer.flush()

			val reader = socket.getInputStream()
			val response = reader.bufferedReader().readText()
			Assert.assertTrue(response.contains("Hello World"))
		}
		server.stop()
	}

	@Test
	fun `should return hello name`() {
		server.start()
		Socket("localhost", server.getPort()).use { socket ->
			val writer = socket.getOutputStream().writer()
			writer.write("GET /Ana HTTP/1.1\n\n")
			writer.flush()

			val reader = socket.getInputStream()
			val response = reader.bufferedReader().readText()
			Assert.assertTrue(response.contains("Hello Ana"))
		}
		server.stop()
	}

	@Test
	fun `should return bad request`() {
		server.start()
		Socket("localhost", server.getPort()).use { socket ->
			val writer = socket.getOutputStream().writer()
			writer.write("very bad request\n\n")
			writer.flush()

			val reader = socket.getInputStream()
			val response = reader.bufferedReader().readText()
			Assert.assertTrue(response.contains("400"))
		}
		server.stop()
	}
}