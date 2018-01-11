package application

import http.*
import java.io.*

class RouteContentHelloWorld : RouteContent {
	override fun customizedRouteContent(request: HttpRequest): HttpResponse {
		val bodyMessage = "Hello, World!"
		val contentType = ContentType.PLAIN

		return HttpResponse(HTTP_VERSION,
				200,
				"OK",
				mapOf(CONTENT_LENGTH to "${bodyMessage.length}", CONTENT_TYPE to contentType.type),
				ByteArrayInputStream(bodyMessage.toByteArray(Charsets.ISO_8859_1)))
	}
}

class RouteContentHelloAna : RouteContent {
	override fun customizedRouteContent(request: HttpRequest): HttpResponse {
		val bodyMessage = "Hello, Ana!"
		val contentType = ContentType.PLAIN

		return HttpResponse(HTTP_VERSION,
				200,
				"OK",
				mapOf(CONTENT_LENGTH to "${bodyMessage.length}", CONTENT_TYPE to contentType.type),
				ByteArrayInputStream(bodyMessage.toByteArray(Charsets.ISO_8859_1)))
	}
}

class RouteContentHtml : RouteContent {
	override fun customizedRouteContent(request: HttpRequest): HttpResponse {
		val bodyMessage = "<html><body><h1>Hello, World!</h1></body></html>"
		val contentType = ContentType.HTMl

		return HttpResponse(HTTP_VERSION,
				200,
				"OK",
				mapOf(CONTENT_LENGTH to "${bodyMessage.length}", CONTENT_TYPE to contentType.type),
				ByteArrayInputStream(bodyMessage.toByteArray(Charsets.ISO_8859_1)))
	}
}