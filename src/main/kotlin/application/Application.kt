package application

import server.*

fun main(args: Array<String>) {

	val port = 8081
	val server = WebServer.create(port,
											listOf("/hello" to RouteContentHelloWorld(),
													"/ana" to RouteContentHelloAna(),
													"/html" to RouteContentHtml()))
	server.start()
}