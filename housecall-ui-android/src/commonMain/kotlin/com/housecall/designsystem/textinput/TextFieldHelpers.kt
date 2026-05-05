@file:Suppress("MatchingDeclarationName")

package com.housecall.designsystem.textinput

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.housecall.designsystem.color.HcColors
import com.housecall.designsystem.typography.HcTypography

/**
 * Port of `housecall.core.ui.textinput.NumberFormatType`. Pure enum.
 */
enum class NumberFormatType {
    CURRENCY,
    PERCENTAGE,
    PHONE_NUMBER,
    DECIMAL,
    NUMBER,
    DEFAULT,
    TAX_ID,
    SSN,
    ZIP_CODE_US,
    ZIP_CODE_CA,
}

/**
 * Port of `housecall.core.ui.textinput.outlinedTextFieldColors`.
 * Drives the Material 1 [TextFieldDefaults.outlinedTextFieldColors] used by
 * the DS outlined variant.
 */
@Composable
fun outlinedTextFieldColors(
    placeholderColor: Color,
    labelColor: Color,
): TextFieldColors {
    val isLight = MaterialTheme.colors.isLight

    val textColor = if (isLight) HcColors.Text.OnSurfacePrimary else HcColors.Common.White
    val disabledTextColor = if (isLight) HcColors.Text.OnSurfaceDisabled else HcColors.Grey.c400
    val disabledBorderColor = if (isLight) HcColors.Text.OnSurfaceDisabled else HcColors.Grey.c600
    val backgroundColor = if (isLight) HcColors.Surface.Default else HcColors.Surface.Inverse
    val cursorColor = if (isLight) HcColors.Primary.Main else HcColors.Primary.Light

    return TextFieldDefaults.outlinedTextFieldColors(
        textColor = textColor,
        disabledTextColor = disabledTextColor,
        backgroundColor = backgroundColor,
        placeholderColor = placeholderColor,
        focusedBorderColor = Color.Transparent,
        unfocusedBorderColor = Color.Transparent,
        disabledBorderColor = disabledBorderColor,
        errorBorderColor = HcColors.Error.Main,
        focusedLabelColor = labelColor,
        unfocusedLabelColor = labelColor,
        errorLabelColor = HcColors.Error.Main,
        disabledLabelColor = disabledTextColor,
        cursorColor = cursorColor,
    )
}

/**
 * Port of `housecall.core.ui.textinput.textFieldColors`.
 * Drives the Material 1 [TextFieldDefaults.textFieldColors] used by the DS
 * filled variant.
 */
@Composable
fun textFieldColors(
    placeholderColor: Color,
    labelColor: Color,
    hasWideLeadingContent: Boolean,
    underlineBorderColor: Color,
    underlineColor: Color,
): TextFieldColors {
    val isLight = MaterialTheme.colors.isLight

    val textColor = if (isLight) HcColors.Text.OnSurfacePrimary else HcColors.Common.White
    val disabledTextColor = if (isLight) HcColors.Text.OnSurfaceDisabled else HcColors.Grey.c400
    val disabledIndicatorColor = if (isLight) HcColors.Text.OnSurfaceDisabled else HcColors.Grey.c600
    val backgroundColor = if (isLight) HcColors.Surface.Default else HcColors.Surface.Inverse
    val cursorColor = if (isLight) HcColors.Primary.Main else HcColors.Primary.Light

    return TextFieldDefaults.textFieldColors(
        textColor = textColor,
        disabledTextColor = disabledTextColor,
        backgroundColor = backgroundColor,
        placeholderColor = placeholderColor,
        focusedLabelColor = labelColor,
        unfocusedLabelColor = labelColor,
        errorLabelColor = HcColors.Error.Main,
        focusedIndicatorColor = if (hasWideLeadingContent) Color.Transparent else underlineBorderColor,
        unfocusedIndicatorColor = if (hasWideLeadingContent) Color.Transparent else underlineColor,
        disabledIndicatorColor = if (hasWideLeadingContent) Color.Transparent else disabledIndicatorColor,
        errorIndicatorColor = if (hasWideLeadingContent) Color.Transparent else HcColors.Error.Main,
        disabledLabelColor = disabledTextColor,
        disabledPlaceholderColor = disabledTextColor,
        cursorColor = cursorColor,
    )
}

/**
 * Port of `housecall.core.ui.textinput.TrailingTextAdornment`.
 */
@Suppress("ModifierMissing")
@Composable
internal fun TrailingTextAdornment(trailingText: String, enabled: Boolean, padding: Dp = 0.dp) {
    Text(
        text = trailingText,
        color = if (enabled) HcColors.Text.OnSurfaceSecondary else HcColors.Text.OnSurfaceDisabled,
        style = HcTypography.Body1,
        modifier = Modifier.padding(horizontal = padding),
    )
}
