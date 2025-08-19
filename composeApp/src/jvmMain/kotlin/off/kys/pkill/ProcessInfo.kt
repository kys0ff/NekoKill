package off.kys.pkill

/**
 * Represents information about a process running on the system.
 *
 * @property pid The process ID of the running process.
 * @property user The user who owns the process.
 * @property name The name of the process, retrieved from the system.
 * @property command The full command line that was used to start the process.
 */
data class ProcessInfo(
    val pid: Int,
    val user: String,
    val name: String,
    val command: String,
)