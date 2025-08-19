package off.kys.pkill

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import off.kys.pkill.ui.screens.MainScreen

val SHAPE_RADIUS: RoundedCornerShape
    get() = RoundedCornerShape(20)

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
        MainScreen()
    }
}