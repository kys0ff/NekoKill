package off.kys.pkill.ui.componets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import off.kys.pkill.SHAPE_RADIUS

@Composable
fun KillButton(
    onClick: () -> Unit
) {
    Box(
        Modifier
            .clip(SHAPE_RADIUS)
            .background(
                color = Color(0xFFC33A36),
                shape = SHAPE_RADIUS
            )
            .clickable { onClick() }
            .padding(8.dp)
            .padding(start = 4.dp, end = 4.dp)
    ) {
        Text(
            text = "Kill",
            color = Color.White,
            fontSize = 15.sp
        )
    }
}