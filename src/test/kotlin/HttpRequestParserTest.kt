import http.*
import org.junit.*

class HttpRequestParserTest {

	private val parser = HttpRequestParser()

	@Test(expected = HttpRequestParseException::class)
	fun `should throw when first line has less than three parameters`() {
		parser.parseRequest("hallo world\r\n".byteInputStream())
	}

	@Test(expected = HttpRequestParseException::class)
	fun `should throw when method is not an existing one`() {
		parser.parseRequest("method /test HTTP/1.1\r\n".byteInputStream())
	}

	@Test(expected = HttpRequestParseException::class)
	fun `should throw when http version isn't right`() {
		parser.parseRequest("GET /test version\r\n".byteInputStream())
	}

	@Test(expected = HttpRequestParseException::class)
	fun `should throw when headers are not having right format`() {
		parser.parseRequest("GET /test HTTP/1.1\r\nwrong header format\r\nContent-Length:length\r\n".byteInputStream())
	}

	@Test
	fun `should haven empty body when content-length header is missing`() {
		val request = parser.parseRequest("GET /test HTTP/1.1\r\nContent-Type:type\r\n\r\nsome body content".byteInputStream())
		Assert.assertTrue(request.readBodyContent().isEmpty())

	}

	@Test
	fun `should haven empty body when content-length is 0`() {
		val request = parser.parseRequest("GET http://test HTTP/1.1\r\nContent-Length:0\r\n\r\nsome body content".byteInputStream())
		Assert.assertTrue(request.readBodyContent().isEmpty())
	}

	@Test
	fun `should haven body no longer than content-length`() {
		val request = parser.parseRequest("GET /test HTTP/1.1\r\nContent-Length:3\r\n\r\nsome body content".byteInputStream())
		Assert.assertTrue(request.body.bufferedReader().readText().contains("som"))
		Assert.assertFalse(request.body.bufferedReader().readText().contains("some"))
	}

	@Test
	fun `should create a request as expected`() {
		val request = parser.parseRequest("GET /test HTTP/1.1\r\nContent-Length:3\r\nContent-Type:type\r\n\r\nsome body content".byteInputStream())
		Assert.assertEquals(HttpMethod.GET, request.method)
		Assert.assertEquals("/test", request.URI)
		Assert.assertEquals("3", request.headers.get("Content-Length"))
		Assert.assertEquals("type", request.headers.get("Content-Type"))
		Assert.assertTrue(request.body.bufferedReader().readText().contains("som"))
	}
}