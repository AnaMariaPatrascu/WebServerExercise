interface RequestInterpreter {
	fun interpretRequest(request: HttpRequest): HttpResponse
}

class HttpRequestInterpreter : RequestInterpreter {
	override fun interpretRequest(request: HttpRequest): HttpResponse {
		return if (request.method == HttpMethod.GET && request.URI == "/hello") {
			HttpResponse("HTTP/1.1", 200, "OK", mapOf(),
					"Hello World!")
		} else if (request.method == HttpMethod.GET) {
			HttpResponse("HTTP/1.1", 200, "OK", mapOf(),
					"Hello ${request.URI.substring(1)}!")
		} else {
			HttpResponse("HTTP/1.1", 200, "OK", mapOf(),
					"Your request was: \n" +
							"${request.method} ${request.URI} ${request.httpVersion}\n" +
							"${request.headers.map { it -> "${it.key}: ${it.value}\n" }}\n " +
							"\n" +
							"${request.body}")
		}
	}
}