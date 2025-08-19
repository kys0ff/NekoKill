package off.kys.pkill.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import off.kys.pkill.data.manager.DesktopEntryManager
import off.kys.pkill.data.manager.ProcessManager
import off.kys.pkill.domain.model.ProcessInfo
import off.kys.pkill.ui.componets.AppHeader
import off.kys.pkill.ui.componets.ProcessItem

@Composable
fun MainScreen() {
    val activeProcesses = remember {
        mutableStateListOf<ProcessInfo>()
    }

    LaunchedEffect(Unit) {
        runCatching {
            activeProcesses.clear()
            val nonSystemProcesses = ProcessManager.getNonSystemProcesses()

            nonSystemProcesses.forEach { process ->
                val entry = DesktopEntryManager.getDesktopEntryForPid(process.pid)

                if (entry != null) {
                    activeProcesses.add(process.copy(desktopEntry = entry))
                } else {
                    activeProcesses.add(process)
                }
            }

            // ✅ Group processes by their desktop entry (so same app stays together)
            activeProcesses.sortWith(compareBy(
                { it.desktopEntry == null }, // put ones with no entry last
                { it.desktopEntry?.name ?: it.name } // group by app name or process name
            ))
        }.onFailure { e ->
            println("Failed to update active processes: ${e.message}")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF353636))
            .padding(10.dp)
    ) {
        AppHeader()

        Text(
            modifier = Modifier.padding(8.dp),
            text = "Processes: ",
            color = Color.White,
            fontSize = 16.sp
        )

        Spacer(Modifier.size(12.dp))

        LazyColumn {
            items(activeProcesses) { process ->
                ProcessItem(processInfo = process)
            }
        }
    }
}