package off.kys.pkill.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import nekokill.composeapp.generated.resources.Res
import nekokill.composeapp.generated.resources.refresh
import off.kys.pkill.data.PreferencesManager
import off.kys.pkill.data.manager.DesktopEntryManager
import off.kys.pkill.data.manager.ProcessManager
import off.kys.pkill.domain.model.ProcessInfo
import off.kys.pkill.ui.componets.AppHeader
import off.kys.pkill.ui.componets.ProcessItem
import org.jetbrains.compose.resources.painterResource

@Composable
fun MainScreen() {
    val activeProcesses = remember {
        mutableStateListOf<ProcessInfo>()
    }

    var isLoading by remember { mutableStateOf(true) }
    var refresh by remember { mutableStateOf(true) }

    LaunchedEffect(refresh) {
        isLoading = true
        runCatching {
            withContext(Dispatchers.IO) {
                val processes = ProcessManager.getActiveProcesses().map { process ->
                    val entry = DesktopEntryManager.getDesktopEntryForPid(process.pid)
                    if (entry != null) {
                        println("Found entry: $entry")
                        process.copy(desktopEntry = entry)
                    } else {
                        process
                    }
                }.sortedBy { it.name }
                    .let { list ->
                        // Apply filters
                        var filtered = list
                        PreferencesManager.Filters.Commands.endsWith()?.let { filter ->
                            filtered = filtered.filter { it.command.endsWith(filter) }
                        }
                        PreferencesManager.Filters.Name.contains()?.let { filter ->
                            filtered = filtered.filter { process ->
                                filter.first.any { query ->
                                    process.name.contains(query, ignoreCase = filter.second)
                                }
                            }
                        }
                        filtered
                    }

                withContext(Dispatchers.Main) {
                    activeProcesses.clear()
                    activeProcesses.addAll(processes)
                }
            }
        }.onFailure { e ->
            println("Failed to update active processes: ${e.message}")
        }.onSuccess {
            delay(1500) // keep loading indicator a bit
        }
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF353636))
            .padding(10.dp)
    ) {
        AppHeader()
        ListHeader {
            refresh = !refresh
        }
        Spacer(Modifier.size(12.dp))

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Loading...",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn {
                items(activeProcesses) { process ->
                    ProcessItem(processInfo = process)
                }
            }
        }
    }
}

@Composable
fun ListHeader(onRefreshClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp)
                .weight(1f),
            text = "Processes: ",
            color = Color.White,
            fontSize = 16.sp
        )

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onRefreshClick() }
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.refresh),
                contentDescription = "Refresh",
                tint = Color.White.copy(alpha = 0.65f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}