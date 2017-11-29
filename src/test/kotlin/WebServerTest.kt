import org.junit.*
import java.net.Socket

class WebServerTest {

	private val server = WebServer()
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
}