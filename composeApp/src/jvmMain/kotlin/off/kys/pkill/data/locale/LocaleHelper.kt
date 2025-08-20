package off.kys.pkill.data.locale

import off.kys.pkill.util.FileUtils
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

object LocaleHelper {

    data class Translations(
        val view: String,
        val entries: Map<String, String>,
    )

    class InvalidTranslationFileException(message: String) : Exception(message)

    enum class View {
        LTR, RTL;

        companion object {
            fun of(value: String): View = when (value.lowercase()) {
                "ltr" -> LTR
                "rtl" -> RTL
                else -> throw IllegalArgumentException("Invalid view: $value")
            }
        }
    }

    fun getString(key: String): String {
        parseTranslations(FileUtils.getLocaleFile()).entries[key]?.let { return it }

        throw IllegalStateException("String with key $key not found in locale file")
    }

    fun getView(): View = View.of(parseTranslations(FileUtils.getLocaleFile()).view)

    private fun parseTranslations(file: File): Translations {
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val document = builder.parse(file)

        val root = document.documentElement
        if (root.nodeName != "translations") {
            throw InvalidTranslationFileException("Root element must be <translations>")
        }

        val view = root.getAttribute("view")
        if (view.isNullOrBlank()) {
            throw InvalidTranslationFileException("<translations> must have a 'view' attribute")
        }

        val nodeList = root.getElementsByTagName("entry")
        if (nodeList.length == 0) {
            throw InvalidTranslationFileException("<translations> must contain at least one <entry>")
        }

        val entries = mutableMapOf<String, String>()
        for (i in 0 until nodeList.length) {
            val node = nodeList.item(i)
            if (node is Element) {
                if (node.nodeName != "entry") {
                    throw InvalidTranslationFileException("Unexpected node <${node.nodeName}> inside <translations>")
                }

                val key = node.getAttribute("key")
                if (key.isBlank()) {
                    throw InvalidTranslationFileException("<entry> is missing 'key' attribute")
                }

                val value = node.textContent.trim()
                if (value.isBlank()) {
                    throw InvalidTranslationFileException("<entry key=\"$key\"> must have text content")
                }

                if (entries.containsKey(key)) {
                    throw InvalidTranslationFileException("Duplicate entry key: $key")
                }

                entries[key] = value
            }
        }

        return Translations(view, entries)
    }

}