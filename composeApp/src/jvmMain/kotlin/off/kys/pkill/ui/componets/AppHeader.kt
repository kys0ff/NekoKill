package off.kys.pkill.ui.componets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nekokill.composeapp.generated.resources.Res
import nekokill.composeapp.generated.resources.icon
import org.jetbrains.compose.resources.painterResource

@Composable
fun AppHeader() {
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
}