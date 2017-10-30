import org.junit.*
import java.net.Socket

class WebServerTest {

	private val server = WebServer()

    @Before
    fun setUp() {
        server.start(0)
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
}