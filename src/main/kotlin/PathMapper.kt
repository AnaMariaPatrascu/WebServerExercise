import java.util.concurrent.*

class PathMapper {
	//TODO extend for files or images
	private val map = ConcurrentHashMap<String, String>()

	fun add(key: String, value: String) {
		map.put(key, value)
	}

	fun get(key: String): String? {
		return map.get(key)
	}

	fun remove(key: String) {
		map.remove(key)
	}

	fun containsKey(key: String): Boolean {
		return map.containsKey(key)
	}
}