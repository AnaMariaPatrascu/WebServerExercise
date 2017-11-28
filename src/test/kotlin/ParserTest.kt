import org.junit.*

class ParserTest {

	private val parser = Parser()

	@Test(expected = IllegalArgumentException::class)
	fun `should fail when first line has less than three parameters`() {
		parser.parseRequest("hallo world".byteInputStream())
	}

	@Test(expected = IllegalArgumentException::class)
	fun `should fail when method is not an existing one`() {
		parser.parseRequest("method http://test HTTP/1.1".byteInputStream())
	}

	@Test(expected = IllegalArgumentException::class)
	fun `should fail when uri is not right`() {
		parser.parseRequest("GET uri HTTP/1.1".byteInputStream())
	}

	@Test(expected = IllegalArgumentException::class)
	fun `should fail when http version isn't right`() {
		parser.parseRequest("GET http://test version".byteInputStream())
	}

	@Test(expected = IllegalArgumentException::class)
	fun `should fail when headers are not having right format`() {
		parser.parseRequest("GET http://test HTTP/1.1\n wrong header format \n Content-Length: length".byteInputStream())
	}
}