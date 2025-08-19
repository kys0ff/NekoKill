package off.kys.pkill.ui.componets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import coil3.compose.AsyncImage
import nekokill.composeapp.generated.resources.Res
import nekokill.composeapp.generated.resources.icon
import off.kys.pkill.data.manager.ProcessManager
import off.kys.pkill.domain.model.ProcessInfo
import org.jetbrains.compose.resources.painterResource
import java.io.File

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
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                if (processInfo.desktopEntry != null) {
                    runCatching {
                        AsyncImage(
                            modifier = Modifier.size(36.dp),
                            model = File(processInfo.desktopEntry.icon),
                            contentDescription = "${processInfo.name} Icon",
                        )
                    }.onFailure {
                        Icon(
                            modifier = Modifier.size(36.dp),
                            painter = painterResource(Res.drawable.icon),
                            contentDescription = "${processInfo.name} Icon",
                            tint = Color.White.copy(alpha = 0.65f),
                        )
                    }
                } else {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        painter = painterResource(Res.drawable.icon),
                        contentDescription = "${processInfo.name} Icon",
                        tint = Color.White.copy(alpha = 0.65f),
                    )
                }

                Spacer(Modifier.size(10.dp))

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

                KillButton { ProcessManager.killProcess(processInfo.pid) }

                Spacer(Modifier.size(8.dp))

                RestartButton { ProcessManager.restartProcess(processInfo.pid) }
            }
        }

        Spacer(Modifier.size(8.dp))
    }
}