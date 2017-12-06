package http

import java.util.concurrent.*

interface RequestHandler {
	fun handleRequest(request: HttpRequest): HttpResponse
}

class HttpRequestHandler : RequestHandler {

	private val routesMap = ConcurrentHashMap<String, RouteContent>()

	//addPath or submitPath or submitNewRoute
	fun addNewRoute(key: String, value: RouteContent) {
		routesMap.put(key, value)
	}

	override fun handleRequest(request: HttpRequest): HttpResponse {
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