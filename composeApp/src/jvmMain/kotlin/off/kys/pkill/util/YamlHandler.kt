package off.kys.pkill.util

import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileInputStream

class YamlHandler(filePath: String) {
    private val yamlData: Map<String, Any?>

    init {
        val yaml = Yaml()
        FileInputStream(File(filePath)).use { input ->
            @Suppress("UNCHECKED_CAST")
            yamlData = yaml.load(input) as Map<String, Any?>? ?: emptyMap()
        }
    }

    fun get(path: String): Any? {
        val keys = path.split(".")
        var current: Any? = yamlData
        for (key in keys) {
            if (current is Map<*, *>) {
                current = current[key]
            } else {
                return null
            }
        }
        return current
    }

    fun getString(path: String, default: String? = null): String? =
        get(path)?.toString() ?: default

    fun getInt(path: String, default: Int? = null): Int? =
        (get(path) as? Number)?.toInt() ?: default

    fun getDouble(path: String, default: Double? = null): Double? =
        (get(path) as? Number)?.toDouble() ?: default

    fun getBoolean(path: String, default: Boolean? = null): Boolean? =
        (get(path) as? Boolean) ?: default

    fun getList(path: String): List<Any?>? =
        get(path) as? List<Any?>

    @Suppress("UNCHECKED_CAST")
    fun getMap(path: String): Map<String, Any?>? =
        get(path) as? Map<String, Any?>
}
