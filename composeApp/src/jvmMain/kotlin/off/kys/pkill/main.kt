package off.kys.pkill

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import nekokill.composeapp.generated.resources.Res
import nekokill.composeapp.generated.resources.icon
import org.jetbrains.compose.resources.painterResource

fun main() = application {
    val windowState = rememberWindowState(
        width = 320.dp,
        height = 500.dp
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "NekoKill",
        state = windowState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF353636))
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    tint = Color.White,
                    painter = painterResource(Res.drawable.icon),
                    contentDescription = "Icon",
                )

                Spacer(Modifier.size(12.dp))

                Text(
                    text = "NekoKill",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 26.sp)
                )
            }

            Text(
                modifier = Modifier.padding(8.dp),
                text = "Non-system processes: ",
                color = Color.White,
                fontSize = 16.sp
            )

            Spacer(Modifier.size(12.dp))

            LazyColumn {
                items(ProcessManager.getNonSystemProcesses()) { process ->
                    ProcessItem(processInfo = process)
                }
            }
        }
    }
}

@Composable
fun ProcessItem(modifier: Modifier = Modifier, processInfo: ProcessInfo) {
    Column {
        Box(modifier.fillMaxWidth()) {
            val itemShape = RoundedCornerShape(18)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(itemShape)
                    .background(
                        color = Color(0xFF353535),
                        shape = itemShape
                    )
                    .border(1.dp, Color(0xFF444544), itemShape)
                    .padding(12.dp)
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = processInfo.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp),
                        color = Color.White
                    )
                    Text(
                        text = processInfo.pid.toString(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleSmall.copy(fontSize = 13.sp),
                        color = Color(0xFF888888)
                    )
                }

                Box(
                    Modifier
                        .clip(itemShape)
                        .background(Color(0xFFC33A36), itemShape)
                        .clickable { ProcessManager.killProcess(processInfo.pid) }
                        .padding(8.dp)
                        .padding(start = 4.dp, end = 4.dp)
                ) {
                    Text(
                        text = "Kill",
                        color = Color.White,
                        fontSize = 15.sp
                    )
                }

                Spacer(Modifier.size(8.dp))

                Box(
                    Modifier
                        .clip(itemShape)
                        .background(Color(0xFF474747), itemShape)
                        .clickable { ProcessManager.restartProcess(processInfo.pid) }
                        .padding(8.dp)
                        .padding(start = 4.dp, end = 4.dp)
                ) {
                    Text(
                        text = "Restart",
                        color = Color.White,
                        fontSize = 15.sp
                    )
                }
            }
        }

        Spacer(Modifier.size(8.dp))
    }
}