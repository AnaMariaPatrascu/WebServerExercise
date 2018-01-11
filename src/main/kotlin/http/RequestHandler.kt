package http

interface RequestHandler {
	fun handleRequest(request: HttpRequest): HttpResponse
}

class HttpRequestHandler(private val router: Router) : RequestHandler {

	override fun handleRequest(request: HttpRequest): HttpResponse {
		return when (request.method) {
			HttpMethod.GET -> handleGet(request)
			else -> HttpResponse(HTTP_VERSION, 501, "Not Implemented", mapOf(), null)
		}
	}

	private fun handleGet(request: HttpRequest): HttpResponse{
		val routesMap = router.getRoutes()
		val routeContent = routesMap[request.URI]
		return routeContent?.customizedRouteContent(request) ?: HttpResponse(HTTP_VERSION, 404, "Not Found", mapOf(), null)
	}
}

enum class ContentType (val type: String) {
	HTMl("text/html"),
	PLAIN("text/plain")
}

interface RouteContent {
	fun customizedRouteContent(request: HttpRequest): HttpResponse
}