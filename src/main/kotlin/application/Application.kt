package application

// why do we need to import two different packages?
import http.*
import server.*
import java.io.*

fun main(args: Array<String>) {
	val router = HttpRouter()
	router.addNewRoute("/hello", RouteContentHelloWorld())
	router.addNewRoute("/ana", RouteContentHelloAna())
	router.addNewRoute("/html", RouteContentHtml())

	val port = 8081

	val server = try{
						WebServer.create(port, router)
					}catch (e: Exception) {
						println("Failed to create web server instance: ${e.message}")
						null
					}

	server?.start()
}

class RouteContentHelloWorld : RouteContent {
	override fun customizedRouteContent(request: HttpRequest): HttpResponse {
		val bodyMessage = "Hello, World!"
		val contentType = ContentType.PLAIN

		return HttpResponse("HTTP/1.1",
				200,
				"OK",
			        // typo! maybe typed API so this cannot happen? (this is also true for other parts of this constructor)
				mapOf("Content-Length" to "${bodyMessage.length}", "Content-Type" to contentType.type),
				ByteArrayInputStream(bodyMessage.toByteArray(Charsets.UTF_8)))
	}
}

class RouteContentHelloAna : RouteContent {
	override fun customizedRouteContent(request: HttpRequest): HttpResponse {
		val bodyMessage = "Hello, Ana!"
		val contentType = ContentType.PLAIN

		return HttpResponse("HTTP/1.1",
				200,
				"OK",
				mapOf("Content-Length" to "${bodyMessage.length}", "Content-Type" to contentType.type),
				ByteArrayInputStream(bodyMessage.toByteArray(Charsets.UTF_8)))
	}
}

class RouteContentHtml : RouteContent {
	override fun customizedRouteContent(request: HttpRequest): HttpResponse {
		val bodyMessage = "<html><body><h1>Hello, World!</h1></body></html>"
		val contentType = ContentType.HTMl

		return HttpResponse("HTTP/1.1",
				200,
				"OK",
				mapOf("Content-Length" to "${bodyMessage.length}", "Content-Type" to contentType.type),
				ByteArrayInputStream(bodyMessage.toByteArray(Charsets.UTF_8)))
	}
}