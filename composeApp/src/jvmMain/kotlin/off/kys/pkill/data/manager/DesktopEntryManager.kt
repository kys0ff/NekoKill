package off.kys.pkill.data.manager

import off.kys.pkill.domain.model.DesktopEntry
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths


object DesktopEntryManager {

    fun getDesktopEntryForPid(pid: Int): DesktopEntry? {
        val exePath = try {
            Files.readSymbolicLink(Paths.get("/proc/$pid/exe")).toString()
        } catch (e: Exception) {
            return null
        }

        val desktopDirs = listOf(
            File(System.getProperty("user.home"), ".local/share/applications"),
            File("/usr/share/applications"),
            File("/usr/local/share/applications")
        )

        for (dir in desktopDirs) {
            if (dir.exists()) {
                dir.walkTopDown()
                    .filter { it.isFile && it.extension == "desktop" }
                    .forEach { file ->
                        val execLine = file.useLines { lines ->
                            lines.firstOrNull { it.startsWith("Exec=") }
                        }
                        execLine?.let {
                            val execCmd = it.removePrefix("Exec=").split(" ").first()
                            val execFile = File(execCmd).name
                            if (exePath.endsWith(execFile)) {
                                return parseDesktopEntry(file)
                            }
                        }
                    }
            }
        }
        return null
    }

    private fun parseDesktopEntry(file: File): DesktopEntry {
        val map = mutableMapOf<String, String>()

        file.useLines { lines ->
            lines.forEach { line ->
                val trimmed = line.trim()
                if (trimmed.startsWith("#") || !trimmed.contains("=")) return@forEach
                val (key, value) = trimmed.split("=", limit = 2)
                map[key] = value
            }
        }

        return DesktopEntry(
            name = map["Name"],
            genericName = map["GenericName"],
            comment = map["Comment"],
            exec = map["Exec"],
            icon = map["Icon"],
            type = map["Type"],
            categories = map["Categories"]?.split(";")?.filter { it.isNotBlank() } ?: emptyList(),
            mimeType = map["MimeType"]?.split(";")?.filter { it.isNotBlank() } ?: emptyList(),
            noDisplay = map["NoDisplay"]?.equals("true", ignoreCase = true) ?: false,
            hidden = map["Hidden"]?.equals("true", ignoreCase = true) ?: false,
            terminal = map["Terminal"]?.equals("true", ignoreCase = true) ?: false,
            startupNotify = map["StartupNotify"]?.equals("true", ignoreCase = true) ?: false,
            filePath = file.absolutePath
        )
    }
}
