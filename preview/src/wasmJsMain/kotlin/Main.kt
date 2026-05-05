import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeViewport
import com.housecall.designsystem.color.HcColors
import com.housecall.designsystem.textinput.HcTextField
import com.housecall.designsystem.typography.HcTypography
import kotlinx.browser.document

/**
 * Web preview of the externalized DS rendered at **Pixel 9 dimensions** (412 x 915 dp logical).
 *
 * Consumes `com.housecall:housecall-ui-android:0.1.0-SNAPSHOT` from mavenLocal —
 * the **same** published artifact whose `.aar` variant the Android app's
 * `:internal:dev-tools` consumes. The Wasm browser render uses the `.klib` variant.
 *
 * Goal: validate that what designers see in this web preview matches what end-users
 * see in the Android app. Same Kotlin source, same Skiko renderer.
 */
@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body!!) {
        MaterialTheme(
            colors = lightColors(
                primary = HcColors.Primary.Main,
                onPrimary = HcColors.Primary.OnPrimary,
                surface = HcColors.Surface.Default,
                background = HcColors.Surface.Background,
                error = HcColors.Error.Main,
                onSurface = HcColors.Text.OnSurfacePrimary,
                onBackground = HcColors.Text.OnSurfacePrimary,
            ),
        ) {
            Pixel9Frame {
                HcTextFieldsShowcaseScreen()
            }
        }
    }
}

/**
 * Renders the content inside a Pixel 9-sized canvas (412 x 915 dp logical),
 * with a subtle border and shadow to make the device-frame visible against the
 * page background.
 */
@Composable
private fun Pixel9Frame(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE9ECF2)),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .requiredWidth(412.dp)
                .requiredHeight(915.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(HcColors.Surface.Default),
        ) {
            content()
        }
    }
}

/**
 * A subset of the variants shown in `internal/dev-tools/.../HcTextFieldsScreen.kt`.
 * Only `HcTextField` is migrated in this POC scope, so we exercise its variants
 * exhaustively. Other components (HcSelectTextField, HcNumberInputField,
 * HcCounterTextField) are deferred per the migration log.
 */
@Composable
private fun HcTextFieldsShowcaseScreen() {
    val scroll = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Header()
        SectionTitle("Basic Variation")
        VariantBlock("With label") {
            var v by remember { mutableStateOf("") }
            HcTextField(
                value = v,
                label = "Label",
                onValueChange = { v = it },
                singleLine = true,
            )
        }
        VariantBlock("Without label (placeholder only)") {
            var v by remember { mutableStateOf("") }
            HcTextField(
                value = v,
                placeholder = "Placeholder",
                onValueChange = { v = it },
                singleLine = true,
            )
        }
        VariantBlock("With helper text") {
            var v by remember { mutableStateOf("") }
            HcTextField(
                value = v,
                label = "Username",
                placeholder = "e.g. john.doe",
                helperText = "Letters and numbers only",
                onValueChange = { v = it },
                singleLine = true,
            )
        }
        VariantBlock("Error state") {
            var v by remember { mutableStateOf("not-an-email") }
            HcTextField(
                value = v,
                label = "Email",
                isError = true,
                errorText = "Enter a valid email address",
                onValueChange = { v = it },
                singleLine = true,
            )
        }
        VariantBlock("With trailing text (units)") {
            var v by remember { mutableStateOf("0.00") }
            HcTextField(
                value = v,
                label = "Weight",
                trailingText = "Kg",
                onValueChange = { v = it },
                singleLine = true,
            )
        }
        VariantBlock("Password (toggle)") {
            var v by remember { mutableStateOf("supersecret") }
            HcTextField(
                value = v,
                label = "Password",
                passwordToggleEnabled = true,
                onValueChange = { v = it },
                singleLine = true,
            )
        }
        VariantBlock("Disabled") {
            HcTextField(
                value = "Read-only value",
                label = "Account ID",
                enabled = false,
                onValueChange = {},
                singleLine = true,
            )
        }
        VariantBlock("Outlined") {
            var v by remember { mutableStateOf("") }
            HcTextField(
                value = v,
                label = "Company",
                placeholder = "Acme Inc.",
                outlined = true,
                onValueChange = { v = it },
                singleLine = true,
            )
        }
        VariantBlock("Char limit (60)") {
            var v by remember { mutableStateOf("Short bio") }
            HcTextField(
                value = v,
                label = "Bio",
                helperText = "Tell us about yourself",
                isCharLimited = true,
                charLimit = 60,
                onValueChange = { v = it },
            )
        }
        Box(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun Header() {
    Column {
        Text(text = "Text Fields", style = HcTypography.HeadingLg, color = HcColors.Text.OnSurfacePrimary)
        Box(modifier = Modifier.height(4.dp))
        Text(
            text = "com.housecall:housecall-ui-android:0.1.0-SNAPSHOT (.klib)",
            style = HcTypography.Caption,
            color = HcColors.Text.OnSurfaceSecondary,
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = HcTypography.HeadingDefault,
        color = HcColors.Text.OnSurfacePrimary,
        modifier = Modifier.padding(top = 8.dp),
    )
}

@Composable
private fun VariantBlock(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            style = HcTypography.LabelDefault,
            color = HcColors.Text.OnSurfaceSecondary,
        )
        content()
    }
}
