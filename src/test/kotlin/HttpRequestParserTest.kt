import org.junit.*

class HttpRequestParserTest {

	private val parser = HttpRequestParser()

	@Test(expected = HttpRequestParseException::class)
	fun `should throw when first line has less than three parameters`() {
		parser.parseRequest("hallo world".byteInputStream())
	}

	@Test(expected = HttpRequestParseException::class)
	fun `should throw when method is not an existing one`() {
		parser.parseRequest("method /test HTTP/1.1".byteInputStream())
	}

	@Test(expected = HttpRequestParseException::class)
	fun `should throw when http version isn't right`() {
		parser.parseRequest("GET /test version".byteInputStream())
	}

	@Test(expected = HttpRequestParseException::class)
	fun `should throw when headers are not having right format`() {
		parser.parseRequest("GET /test HTTP/1.1\nwrong header format\nContent-Length:length".byteInputStream())
	}

	@Test
	fun `should haven no body when no content-length header`() {
		val request = parser.parseRequest("GET /test HTTP/1.1\nContent-Type:type\n\nsome body content".byteInputStream())
		Assert.assertNull(request.body)
	}

	@Test
	fun `should haven no body when content-length is 0`() {
		val request = parser.parseRequest("GET http://test HTTP/1.1\nContent-Length:0\n\nsome body content".byteInputStream())
		Assert.assertNull(request.body)
	}

	@Test
	fun `should haven body no longer than content-length`() {
		val request = parser.parseRequest("GET /test HTTP/1.1\nContent-Length:3\n\nsome body content".byteInputStream())
		Assert.assertEquals("som", request.body)
	}

	@Test
	fun `should create a request as expected`() {
		val request = parser.parseRequest("GET /test HTTP/1.1\nContent-Length:3\nContent-Type:type\n\nsome body content".byteInputStream())
		Assert.assertEquals(HttpMethod.GET, request.method)
		Assert.assertEquals("/test", request.URI)
		Assert.assertEquals("3", request.headers.get("Content-Length"))
		Assert.assertEquals("type", request.headers.get("Content-Type"))
		Assert.assertEquals("som", request.body)
	}
}