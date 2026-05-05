package com.housecall.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.housecall.housecall_ui_android.generated.resources.Res
import com.housecall.housecall_ui_android.generated.resources.ic_visibility_on
import org.jetbrains.compose.resources.painterResource

/**
 * Spike composable used to validate the entire CMP→AGP `.aar` consumption pipeline.
 *
 * Renders a label and an icon loaded via Compose Resources. If this composable
 * shows up correctly when consumed from `:internal:dev-tools`, the toolchain is
 * proven and the heavy migration can proceed.
 */
@Composable
fun HcHello(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "hi from external lib",
            style = MaterialTheme.typography.h6,
            color = Color(0xFF0057FF),
        )
        Icon(
            painter = painterResource(Res.drawable.ic_visibility_on),
            contentDescription = null,
            tint = Color(0xFF0057FF),
            modifier = Modifier.size(32.dp),
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "(packaged from com.housecall:housecall-ui-android:0.1.0-SNAPSHOT)",
            style = MaterialTheme.typography.caption,
        )
    }
}
