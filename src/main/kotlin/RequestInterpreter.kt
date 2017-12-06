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
			HttpMethod.GET -> handleGet(request)
			else -> HttpResponse("HTTP/1.1", 501, "Not Implemented", mapOf(), null)
		}
	}

	private fun handleGet(request: HttpRequest): HttpResponse{
		val routeContent = routesMap[request.URI]
		return routeContent?.customizedRouteContent(request) ?: HttpResponse("HTTP/1.1", 404, "Not Found", mapOf(), null)
	}
}

enum class ContentType (val type: String) {
	HTMl("text/html"),
	PLAIN("text/plain")
}

interface RouteContent {
	fun customizedRouteContent(request: HttpRequest): HttpResponse
}

class RouteContentHelloWorld : RouteContent {
	override fun customizedRouteContent(request: HttpRequest): HttpResponse {
		val bodyMessage = "Hello world!"
		val contentType = ContentType.PLAIN

		return HttpResponse("HTTP/1.1",
				200,
				"OK",
				mapOf(Pair("Content-Lenght", "${bodyMessage.length}"), Pair("Content-Type", contentType.type)),
				bodyMessage)
	}
}

class RouteContentHelloAna : RouteContent {
	override fun customizedRouteContent(request: HttpRequest): HttpResponse {
		val bodyMessage = "Hello Ana!"
		val contentType = ContentType.PLAIN

		return HttpResponse("HTTP/1.1",
				200,
				"OK",
				mapOf(Pair("Content-Lenght", "${bodyMessage.length}"), Pair("Content-Type", contentType.type)),
				bodyMessage)
	}
}

class RouteContentHtml : RouteContent {
	override fun customizedRouteContent(request: HttpRequest): HttpResponse {
		val bodyMessage = "<html><body><h1>Hello, World!</h1></body></html>"
		val contentType = ContentType.HTMl

		return HttpResponse("HTTP/1.1",
				200,
				"OK",
				mapOf(Pair("Content-Lenght", "${bodyMessage.length}"), Pair("Content-Type", contentType.type)),
				bodyMessage)
	}
}