import java.util.concurrent.*

interface RequestInterpreter {
	fun interpretRequest(request: HttpRequest): HttpResponse
}

class HttpRequestInterpreter : RequestInterpreter {

	private val routesMap = ConcurrentHashMap<String, RouteContent>()

	//addPath or submitPath or submitNewRoute
	fun addNewRoute(key: String, value: RouteContent) {
		routesMap.put(key, value)
	}

	override fun interpretRequest(request: HttpRequest): HttpResponse {
		return when (request.method) {
			HttpMethod.GET -> handleGet(request.URI)
			else -> HttpResponse("HTTP/1.1", 501, "Not Implemented", mapOf(), null)
		}
	}

	private fun handleGet(uri: String): HttpResponse{
		val routeContent = routesMap[uri]
		val bodyMessage = routeContent?.content

		return if (bodyMessage.isNullOrEmpty())  HttpResponse("HTTP/1.1", 404, "Not Found", mapOf(), null)
			else  HttpResponse("HTTP/1.1",
				200,
				"OK",
				mapOf(Pair("Content-Lenght", "${bodyMessage?.length}"), Pair("Content-Type", routeContent?.contentType?.type ?: "text/plain")),
				bodyMessage)
	}
}

enum class ContentType (val type: String) {
	HTMl("text/html"),
	PLAIN("text/plain")
}

data class RouteContent(val content: String, val contentType: ContentType)