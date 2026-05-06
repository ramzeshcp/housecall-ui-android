@file:Suppress("MagicNumber", "TooManyFunctions")

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.housecall.designsystem.color.HcColors
import com.housecall.designsystem.textinput.HcTextField
import com.housecall.designsystem.typography.HcTypography
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Layout DSL for the `?screen=builder&layout=BASE64_JSON` route.
 *
 * The interpreter renders an arbitrary tree of layout primitives + Hc components
 * from a JSON description. Designers compose freely through prompts; the agent
 * generates the JSON and embeds it in the iframe URL.
 *
 * Schema (uniform — all nodes share one shape, only some fields apply per type):
 *
 * ```json
 * { "type": "column",
 *   "padding": 16, "spacing": 8,
 *   "children": [
 *     { "type": "text", "text": "Hello", "style": "HeadingLg" },
 *     { "type": "hc-text-field", "label": "Name", "outlined": true }
 *   ]
 * }
 * ```
 */
@Serializable
data class LayoutNode(
    val type: String,
    val children: List<LayoutNode>? = null,

    // ----- Layout props (column, row, box) -----
    val padding: Int? = null,
    val paddingHorizontal: Int? = null,
    val paddingVertical: Int? = null,
    val spacing: Int? = null,
    val fillWidth: Boolean? = null,
    val fillHeight: Boolean? = null,
    val scroll: Boolean? = null,
    /** "start" | "center" | "end" | "spaceBetween" | "spaceAround" — applies to row/column main axis */
    val mainAxis: String? = null,
    /** "start" | "center" | "end" — applies to row/column cross axis */
    val crossAxis: String? = null,

    // ----- Spacer props -----
    val height: Int? = null,
    val width: Int? = null,

    // ----- Text props -----
    val text: String? = null,
    /** MML token name e.g. "HeadingLg", "BodyDefault", "LabelSm" or M2 alias e.g. "H1", "Body1" */
    val style: String? = null,
    /** Color path e.g. "Primary.Main", "Text.OnSurfacePrimary", "Error.Main" */
    val color: String? = null,

    // ----- HcTextField props -----
    val value: String? = null,
    val label: String? = null,
    val placeholder: String? = null,
    val helperText: String? = null,
    val errorText: String? = null,
    val isError: Boolean? = null,
    val enabled: Boolean? = null,
    val outlined: Boolean? = null,
    val trailingText: String? = null,
    val passwordToggleEnabled: Boolean? = null,
    val isCharLimited: Boolean? = null,
    val charLimit: Int? = null,
    val singleLine: Boolean? = null,
)

private val builderJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

fun parseLayout(jsonString: String): Result<LayoutNode> = runCatching {
    builderJson.decodeFromString(LayoutNode.serializer(), jsonString)
}

@Composable
fun RenderNode(node: LayoutNode, modifier: Modifier = Modifier) {
    when (node.type) {
        "column" -> RenderColumn(node, modifier)
        "row" -> RenderRow(node, modifier)
        "box" -> RenderBox(node, modifier)
        "spacer" -> RenderSpacer(node)
        "text" -> RenderText(node, modifier)
        "hc-text-field" -> RenderHcTextField(node, modifier)
        else -> RenderUnknown(node, modifier)
    }
}

@Composable
private fun RenderColumn(node: LayoutNode, modifier: Modifier) {
    val arrangement = if (node.spacing != null) {
        Arrangement.spacedBy(node.spacing.dp, resolveCrossAlignmentVertical(node.mainAxis))
    } else {
        resolveMainArrangementVertical(node.mainAxis)
    }
    Column(
        modifier = modifier.applyLayoutProps(node).maybeVerticalScroll(node.scroll == true),
        verticalArrangement = arrangement,
        horizontalAlignment = resolveCrossAlignmentHorizontal(node.crossAxis),
    ) {
        node.children.orEmpty().forEach { child -> RenderNode(child) }
    }
}

@Composable
private fun RenderRow(node: LayoutNode, modifier: Modifier) {
    val arrangement = if (node.spacing != null) {
        Arrangement.spacedBy(node.spacing.dp, resolveCrossAlignmentHorizontal(node.mainAxis))
    } else {
        resolveMainArrangementHorizontal(node.mainAxis)
    }
    Row(
        modifier = modifier.applyLayoutProps(node),
        horizontalArrangement = arrangement,
        verticalAlignment = resolveCrossAlignmentVertical(node.crossAxis),
    ) {
        node.children.orEmpty().forEach { child -> RenderNode(child) }
    }
}

@Composable
private fun RenderBox(node: LayoutNode, modifier: Modifier) {
    Box(modifier = modifier.applyLayoutProps(node)) {
        node.children.orEmpty().forEach { child -> RenderNode(child) }
    }
}

@Composable
private fun RenderSpacer(node: LayoutNode) {
    val m = when {
        node.height != null -> Modifier.height(node.height.dp)
        node.width != null -> Modifier.width(node.width.dp)
        else -> Modifier.height(8.dp)
    }
    Spacer(modifier = m)
}

@Composable
private fun RenderText(node: LayoutNode, modifier: Modifier) {
    Text(
        modifier = modifier,
        text = node.text.orEmpty(),
        style = resolveTypography(node.style),
        color = resolveColor(node.color) ?: HcColors.Text.OnSurfacePrimary,
    )
}

@Composable
private fun RenderHcTextField(node: LayoutNode, modifier: Modifier) {
    var v by remember(node.label, node.placeholder, node.value) {
        mutableStateOf(node.value.orEmpty())
    }
    HcTextField(
        modifier = modifier.fillMaxWidth(),
        value = v,
        label = node.label,
        placeholder = node.placeholder,
        helperText = node.helperText,
        errorText = node.errorText,
        isError = node.isError == true,
        enabled = node.enabled != false,
        outlined = node.outlined == true,
        trailingText = node.trailingText,
        passwordToggleEnabled = node.passwordToggleEnabled == true,
        isCharLimited = node.isCharLimited == true,
        charLimit = node.charLimit ?: Int.MAX_VALUE,
        singleLine = node.singleLine == true,
        onValueChange = { v = it },
    )
}

@Composable
private fun RenderUnknown(node: LayoutNode, modifier: Modifier) {
    Box(
        modifier = modifier.padding(8.dp),
    ) {
        Text(
            text = "Unknown node type: '${node.type}'",
            style = HcTypography.BodySm,
            color = HcColors.Error.Main,
        )
    }
}

// ----- helpers -----

private fun Modifier.applyLayoutProps(node: LayoutNode): Modifier {
    var m = this
    when {
        node.fillWidth == true && node.fillHeight == true -> m = m.fillMaxSize()
        node.fillWidth == true -> m = m.fillMaxWidth()
        node.fillHeight == true -> m = m.fillMaxHeight()
    }
    when {
        node.padding != null -> m = m.padding(node.padding.dp)
        node.paddingHorizontal != null || node.paddingVertical != null -> {
            m = m.padding(
                PaddingValues(
                    horizontal = (node.paddingHorizontal ?: 0).dp,
                    vertical = (node.paddingVertical ?: 0).dp,
                ),
            )
        }
    }
    return m
}

@Composable
private fun Modifier.maybeVerticalScroll(enabled: Boolean): Modifier {
    if (!enabled) return this
    val state = rememberScrollState()
    return verticalScroll(state)
}

private fun resolveMainArrangementVertical(name: String?): Arrangement.Vertical = when (name) {
    "center" -> Arrangement.Center
    "end" -> Arrangement.Bottom
    "spaceBetween" -> Arrangement.SpaceBetween
    "spaceAround" -> Arrangement.SpaceAround
    else -> Arrangement.Top
}

private fun resolveMainArrangementHorizontal(name: String?): Arrangement.Horizontal = when (name) {
    "center" -> Arrangement.Center
    "end" -> Arrangement.End
    "spaceBetween" -> Arrangement.SpaceBetween
    "spaceAround" -> Arrangement.SpaceAround
    else -> Arrangement.Start
}

private fun resolveCrossAlignmentHorizontal(name: String?): Alignment.Horizontal = when (name) {
    "center" -> Alignment.CenterHorizontally
    "end" -> Alignment.End
    else -> Alignment.Start
}

private fun resolveCrossAlignmentVertical(name: String?): Alignment.Vertical = when (name) {
    "center" -> Alignment.CenterVertically
    "end" -> Alignment.Bottom
    else -> Alignment.Top
}

@Composable
private fun resolveTypography(name: String?): TextStyle = when (name) {
    // MML
    "DisplayXl" -> HcTypography.DisplayXl
    "DisplayLg" -> HcTypography.DisplayLg
    "DisplayDefault" -> HcTypography.DisplayDefault
    "HeadingLg" -> HcTypography.HeadingLg
    "HeadingDefault" -> HcTypography.HeadingDefault
    "HeadingSm" -> HcTypography.HeadingSm
    "HeadingSmBold" -> HcTypography.HeadingSmBold
    "HeadingXs" -> HcTypography.HeadingXs
    "HeadingXsBold" -> HcTypography.HeadingXsBold
    "SubtitleDefault" -> HcTypography.SubtitleDefault
    "SubtitleSm" -> HcTypography.SubtitleSm
    "BodyDefault", null -> HcTypography.BodyDefault
    "BodySm" -> HcTypography.BodySm
    "LabelDefault" -> HcTypography.LabelDefault
    "LabelSm" -> HcTypography.LabelSm
    // M2 aliases
    "H1" -> HcTypography.H1
    "H2" -> HcTypography.H2
    "H3" -> HcTypography.H3
    "H4" -> HcTypography.H4
    "H5" -> HcTypography.H5
    "H6" -> HcTypography.H6
    "Subtitle1" -> HcTypography.Subtitle1
    "Subtitle2" -> HcTypography.Subtitle2
    "Body1" -> HcTypography.Body1
    "Body2" -> HcTypography.Body2
    "Button" -> HcTypography.Button
    "Caption" -> HcTypography.Caption
    "Toolbar" -> HcTypography.Toolbar
    else -> HcTypography.BodyDefault
}

@Suppress("ReturnCount", "CyclomaticComplexMethod", "LongMethod")
private fun resolveColor(path: String?): androidx.compose.ui.graphics.Color? {
    if (path == null) return null
    return when (path) {
        "Primary.Main" -> HcColors.Primary.Main
        "Primary.Light" -> HcColors.Primary.Light
        "Primary.Dark" -> HcColors.Primary.Dark
        "Primary.OnPrimary" -> HcColors.Primary.OnPrimary
        "Primary.Container" -> HcColors.Primary.Container
        "Primary.OnContainer" -> HcColors.Primary.OnContainer
        "Secondary.Main" -> HcColors.Secondary.Main
        "Secondary.Light" -> HcColors.Secondary.Light
        "Secondary.Dark" -> HcColors.Secondary.Dark
        "Success.Main" -> HcColors.Success.Main
        "Success.Light" -> HcColors.Success.Light
        "Success.Dark" -> HcColors.Success.Dark
        "Warning.Main" -> HcColors.Warning.Main
        "Warning.Light" -> HcColors.Warning.Light
        "Warning.Dark" -> HcColors.Warning.Dark
        "Error.Main" -> HcColors.Error.Main
        "Error.Light" -> HcColors.Error.Light
        "Error.Dark" -> HcColors.Error.Dark
        "Info.Main" -> HcColors.Info.Main
        "Info.Light" -> HcColors.Info.Light
        "Info.Dark" -> HcColors.Info.Dark
        "Surface.Default" -> HcColors.Surface.Default
        "Surface.Background" -> HcColors.Surface.Background
        "Surface.BackgroundSecondary" -> HcColors.Surface.BackgroundSecondary
        "Surface.Inverse" -> HcColors.Surface.Inverse
        "Surface.OnInverse" -> HcColors.Surface.OnInverse
        "Text.OnSurfacePrimary" -> HcColors.Text.OnSurfacePrimary
        "Text.OnSurfaceSecondary" -> HcColors.Text.OnSurfaceSecondary
        "Text.OnSurfaceDisabled" -> HcColors.Text.OnSurfaceDisabled
        "Text.OnSurfaceHint" -> HcColors.Text.OnSurfaceHint
        "Border.Subtle" -> HcColors.Border.Subtle
        "Border.Default" -> HcColors.Border.Default
        "Border.Emphasis" -> HcColors.Border.Emphasis
        "Border.Divider" -> HcColors.Border.Divider
        "Common.Black" -> HcColors.Common.Black
        "Common.White" -> HcColors.Common.White
        else -> null
    }
}
