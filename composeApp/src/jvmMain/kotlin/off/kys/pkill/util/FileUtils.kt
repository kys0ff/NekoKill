package off.kys.pkill.util

import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

object FileUtils {

    private val DEFAULT_LOCALE_EN = """
            <?xml version="1.0" encoding="UTF-8"?>
            <translations view='ltr'>
                <entry key="app_name">NekoKill</entry>
                <entry key="kill">Kill</entry>
                <entry key="restart">Restart</entry>
                <entry key="settings">Settings</entry>
                <entry key="about">About</entry>
            </translations>
        """.trimIndent()

    fun getConfigFolder(): Path {
        val get = Paths.get(System.getProperty("user.home"), ".config", "NekoKill")
        if (!get.toFile().exists()) get.toFile().mkdirs()
        return get
    }

    fun getLocaleFile(): File {
        val localePath = getConfigFolder().resolve("locale.xml")
        if (!localePath.toFile().exists()) {
            localePath.toFile().writeText(DEFAULT_LOCALE_EN)
        }
        return localePath.toFile()
    }

    fun getPreferencesFile(): File {
        val preferencesPath = getConfigFolder().resolve("preferences.yaml")
        if (!preferencesPath.toFile().exists()) {
            preferencesPath.toFile().createNewFile()
        }
        return preferencesPath.toFile()
    }

}