package application

import server.*
import http.*
import java.io.*

fun main(args: Array<String>) {
	val parser = HttpRequestParser()
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