package off.kys.pkill.data

import off.kys.pkill.util.FileUtils
import off.kys.pkill.util.YamlHandler

object PreferencesManager {

    object Filters {
        object Commands {
            fun endsWith(): String? {
                val yamlHandler = YamlHandler(FileUtils.getPreferencesFile().absolutePath)
                return yamlHandler.getString("FILTERS.CMD.ENDS")
            }
        }

        object Name {
            fun contains(): Pair<List<String>, Boolean>? {
                val yamlHandler = YamlHandler(FileUtils.getPreferencesFile().absolutePath)
                val filterList = yamlHandler.getList("FILTERS.NAME.CONTAINS")?.mapNotNull { it as? String } ?: return null
                val ignoreCase = yamlHandler.getBoolean("FILTERS.NAME.IGNORE_CASE") ?: false

                return Pair(filterList, ignoreCase)
            }
        }
    }

    object Sort {

    }

}