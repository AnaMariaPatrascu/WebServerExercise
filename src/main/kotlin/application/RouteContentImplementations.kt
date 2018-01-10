package application

import http.*
import java.io.*

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