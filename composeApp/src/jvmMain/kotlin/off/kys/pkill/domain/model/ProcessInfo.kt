package off.kys.pkill.domain.model

/**
 * Represents information about a process running on the system.
 *
 * @property pid The process ID (PID) of the process.
 * @property user The name of the user who owns the process.
 * @property name The name of the process or application.
 * @property command The command that was used to start the process.
 * @property desktopEntry The optional `DesktopEntry` associated with the process, providing
 * additional metadata when available.
 */
data class ProcessInfo(
    val pid: Int,
    val user: String,
    val name: String,
    val command: String,
    val desktopEntry: DesktopEntry? = null,
)