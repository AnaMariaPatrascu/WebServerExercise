package application

// why do we need to import two different packages?
import http.*
import server.*
import java.io.*

fun main(args: Array<String>) {
	// parser looks like an implementation detail of the WebServer to me
	// maybe use a factory method or something similar to create the webserver so the user doesn't need to
	// create the parser
	val parser = HttpRequestParser()

	// is it an interpreter,  a handler or a router ?
	val interpreter = HttpRequestHandler()
	val port = 8081

	interpreter.addNewRoute("/hello", RouteContentHelloWorld())
	interpreter.addNewRoute("/ana", RouteContentHelloAna())
	interpreter.addNewRoute("/html", RouteContentHtml())

	val server = WebServer(port, requestParser = parser, requestHandler = interpreter)
	server.start()
}

class RouteContentHelloWorld : RouteContent {
	override fun customizedRouteContent(request: HttpRequest): HttpResponse {
		val bodyMessage = "Hello, World!"
		val contentType = ContentType.PLAIN

		return HttpResponse("HTTP/1.1",
				200,
				"OK",
			        // typo! maybe typed API so this cannot happen? (this is also true for other parts of this constructor)
			        // also check the `to` method in kotlin that creates pairs as it enhances readability
				mapOf(Pair("Content-Lenght", "${bodyMessage.length}"), Pair("Content-Type", contentType.type)),
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
				mapOf(Pair("Content-Lenght", "${bodyMessage.length}"), Pair("Content-Type", contentType.type)),
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
				mapOf(Pair("Content-Lenght", "${bodyMessage.length}"), Pair("Content-Type", contentType.type)),
				ByteArrayInputStream(bodyMessage.toByteArray(Charsets.UTF_8)))
	}
}