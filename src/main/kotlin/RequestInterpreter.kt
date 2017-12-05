interface RequestInterpreter {
	fun interpretRequest(request: HttpRequest, pathMapper: PathMapper): HttpResponse
}

class HttpRequestInterpreter : RequestInterpreter {
	override fun interpretRequest(request: HttpRequest, pathMapper: PathMapper): HttpResponse {
		return when (request.method) {
			HttpMethod.GET -> handleGet(request.URI, pathMapper)
			else -> HttpResponse("HTTP/1.1", 501, "Not Implemented", mapOf(), null)
		}
	}

	private fun handleGet(uri: String, pathMapper: PathMapper): HttpResponse{
		val bodyMessage = pathMapper.get(uri)

		return if (bodyMessage.isNullOrEmpty())  HttpResponse("HTTP/1.1", 404, "Not Found", mapOf(), null)
			else  HttpResponse("HTTP/1.1", 200, "OK",
				mapOf(Pair("Content-Lenght", "${bodyMessage?.length}"), Pair("Content-Type", "text/plain")), bodyMessage)
	}
}