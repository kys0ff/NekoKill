package off.kys.pkill.domain.model

data class DesktopEntry(
    val name: String? = null,
    val genericName: String? = null,
    val comment: String? = null,
    val exec: String? = null,
    val icon: String? = null,
    val type: String? = null,
    val categories: List<String> = emptyList(),
    val mimeType: List<String> = emptyList(),
    val noDisplay: Boolean = false,
    val hidden: Boolean = false,
    val terminal: Boolean = false,
    val startupNotify: Boolean = false,
    val filePath: String
)