import http.*
import org.junit.*

class HttpRequestParserTest {

	private val parser = HttpRequestParser()

	@Test(expected = HttpRequestParseException::class)
	fun `should throw when first line has less than three parameters`() {
		parser.parseRequest("hallo world\r\n".byteInputStream(Charsets.ISO_8859_1))
	}

	@Test(expected = HttpRequestParseException::class)
	fun `should throw when method is not an existing one`() {
		parser.parseRequest("method /test $HTTP_VERSION\r\n".byteInputStream(Charsets.ISO_8859_1))
	}

	@Test(expected = HttpRequestParseException::class)
	fun `should throw when http version isn't right`() {
		parser.parseRequest("GET /test version\r\n".byteInputStream(Charsets.ISO_8859_1))
	}

	@Test(expected = HttpRequestParseException::class)
	fun `should throw when headers are not having right format`() {
		parser.parseRequest("GET /test $HTTP_VERSION\r\nwrong header format\r\n$CONTENT_LENGTH:length\r\n".byteInputStream(Charsets.ISO_8859_1))
	}

	@Test
	fun `should haven empty body when content-length header is missing`() {
		val request = parser.parseRequest("GET /test $HTTP_VERSION\r\n$CONTENT_TYPE:type\r\n\r\nsome body content".byteInputStream(Charsets.ISO_8859_1))
		Assert.assertTrue(request.readBodyContent().isEmpty())

	}

	@Test
	fun `should haven empty body when content-length is 0`() {
		val request = parser.parseRequest("GET http://test $HTTP_VERSION\r\n$CONTENT_LENGTH:0\r\n\r\nsome body content".byteInputStream(Charsets.ISO_8859_1))
		Assert.assertTrue(request.readBodyContent().isEmpty())
	}

	@Test
	fun `should haven body no longer than content-length`() {
		val request = parser.parseRequest("GET /test $HTTP_VERSION\r\n$CONTENT_LENGTH:3\r\n\r\nsome body content".byteInputStream(Charsets.ISO_8859_1))
		Assert.assertEquals("som", request.body.bufferedReader().use { it.readText() })
	}

	@Test
	fun `should create a request as expected`() {
		val request = parser.parseRequest("GET /test $HTTP_VERSION\r\n$CONTENT_LENGTH:3\r\n$CONTENT_TYPE:type\r\n\r\nsome body content".byteInputStream(Charsets.ISO_8859_1))
		Assert.assertEquals(HttpMethod.GET, request.method)
		Assert.assertEquals("/test", request.URI)
		Assert.assertEquals("3", request.headers[CONTENT_LENGTH])
		Assert.assertEquals("type", request.headers[CONTENT_TYPE])
		Assert.assertEquals("som", request.body.bufferedReader().use { it.readText() })
	}
}