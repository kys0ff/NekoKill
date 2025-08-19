package off.kys.pkill.data.manager

import off.kys.pkill.domain.model.ProcessInfo
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

/**
 * Provides functionality to manage system processes, including retrieving non-system processes,
 * killing processes, and restarting processes.
 */
object ProcessManager {

    /**
     * Retrieves a list of non-system processes running on the system.
     * A non-system process is defined as a process not owned by the "root" user
     * and not initiated as the very first process in the system.
     *
     * @return A list of process information, where each item contains details
     * such as the process ID (PID), user, name, and command line arguments
     * of the respective non-system process.
     */
    fun getNonSystemProcesses(): List<ProcessInfo> {
        val processList = mutableListOf<ProcessInfo>()
        val cmd = arrayOf("bash", "-c", "ps -eo pid,user --no-headers | grep -v '^ *1 ' | grep -v 'root'")

        val process = ProcessBuilder(*cmd).start()
        BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
            reader.lineSequence().forEach { line ->
                val parts = line.trim().split(Regex("\\s+"))
                if (parts.size >= 2) {
                    val pid = parts[0].toIntOrNull()
                    val user = parts[1]
                    if (pid != null) {
                        val name = readProcessName(pid)
                        val command = readProcessCmdline(pid)
                        processList.add(ProcessInfo(pid, user, name, command))
                    }
                }
            }
        }
        return processList
    }

    /**
     * Attempts to forcibly terminate a process with the specified process ID (PID).
     *
     * @param pid The process ID of the process to be terminated.
     * @return True if the process was successfully terminated, false otherwise.
     */
    fun killProcess(pid: Int): Boolean {
        return try {
            val process = ProcessBuilder("kill", "-9", pid.toString()).start()
            process.waitFor() == 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Restarts a process identified by its process ID (PID).
     * This operation runs asynchronously to avoid blocking the UI thread.
     *
     * @param pid The process ID of the process to restart.
     * @param onResult Optional callback to handle the result (true if successful, false otherwise).
     */
    fun restartProcess(pid: Int, onResult: ((Boolean) -> Unit)? = null) {
        Thread {
            try {
                val command = readProcessCmdline(pid)

                val result = if (command.isBlank()) {
                    false
                } else if (!killProcess(pid)) {
                    false
                } else {
                    val restartProcess = ProcessBuilder("bash", "-c", command).start()
                    restartProcess.waitFor() == 0
                }

                onResult?.invoke(result)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult?.invoke(false)
            }
        }.start()
    }

    /**
     * Reads the name of a process based on its process ID (PID).
     *
     * @param pid The process ID of the process whose name is to be retrieved.
     * @return The name of the process as a trimmed string. If the process name cannot be read,
     *         the function returns "unknown".
     */
    private fun readProcessName(pid: Int): String {
        val file = File("/proc/$pid/comm")

        return try {
            file.readText().trim()
        } catch (e: Exception) {
            System.err.println("Failed to read process name: ${e.localizedMessage}")
            "unknown"
        }
    }

    /**
     * Reads the command-line arguments of a process given its process ID (PID).
     * For AppImage processes, it extracts a user-friendly name.
     *
     * @param pid The process ID for which the command line should be read.
     * @return A string representing the command line used to start the process.
     *         Returns an empty string if an error occurs or if the command line cannot be read.
     */
    private fun readProcessCmdline(pid: Int): String {
        val file = File("/proc/$pid/cmdline")
        return try {
            val raw = file.readText().replace("\u0000", " ").trim()

            val entry = DesktopEntryManager.getDesktopEntryForPid(pid)
            if (entry != null) {
                entry.exec ?: raw
            } else {
                raw
            }
        } catch (e: Exception) {
            System.err.println("Failed to read process cmdline: ${e.localizedMessage}")
            ""
        }
    }
}