package http

import java.util.concurrent.*

interface Router{
	fun addNewRoute(key: String, value: RouteContent)
	fun getRoutes() : ConcurrentHashMap<String, RouteContent>
}

class HttpRouter: Router {
	private val routesMap = ConcurrentHashMap<String, RouteContent>()

	override fun addNewRoute(key: String, value: RouteContent) {
		routesMap.put(key, value)
	}

	override fun getRoutes() : ConcurrentHashMap<String, RouteContent> {
		return routesMap
	}
}