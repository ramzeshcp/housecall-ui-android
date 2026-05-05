@file:Suppress("MatchingDeclarationName", "MagicNumber", "LongParameterList", "FunctionNaming")

package com.housecall.designsystem.textinput

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import com.housecall.designsystem.color.HcColors
import com.housecall.designsystem.textinput.v2.HcTextFieldV2

/**
 * Port of `housecall.core.ui.textinput.HcTextField` (V1 façade).
 *
 * **Migration changes:**
 * - Dropped `textWatcher: TextWatcher?` parameter (decision #3 — Android-only legacy hook).
 * - Otherwise: identical signature, identical delegation to [HcTextFieldV2].
 */
@Composable
fun HcTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    label: String? = null,
    placeholder: String? = null,
    isError: Boolean? = false,
    isReadOnly: Boolean? = false,
    errorText: String? = "",
    enabled: Boolean = true,
    helperText: String? = "",
    onValueChange: (String) -> Unit = {},
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    trailingText: String? = null,
    singleLine: Boolean = false,
    showTopDivider: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    passwordToggleEnabled: Boolean = false,
    isCharLimited: Boolean = false,
    charLimit: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        capitalization = KeyboardCapitalization.Sentences,
        imeAction = if (singleLine) ImeAction.Next else ImeAction.Default,
    ),
    underlineColor: Color = HcColors.Border.Subtle,
    focusedUnderlineColor: Color = HcColors.Primary.Main,
    contentPadding: PaddingValues? = null,
    inputType: KeyboardType? = null,
    onFocusChangeListener: ((Boolean) -> Unit)? = { },
    focusRequester: FocusRequester? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    outlined: Boolean = false,
    textStyle: TextStyle = TextStyle.Default,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
) {
    HcTextFieldV2(
        modifier = modifier,
        value = value,
        label = label,
        placeholder = placeholder,
        isError = isError,
        isReadOnly = isReadOnly,
        errorText = errorText,
        enabled = enabled,
        helperText = helperText,
        onValueChange = onValueChange,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        trailingText = trailingText,
        singleLine = singleLine,
        showTopDivider = showTopDivider,
        minLines = minLines,
        maxLines = maxLines,
        passwordToggleEnabled = passwordToggleEnabled,
        isCharLimited = isCharLimited,
        charLimit = charLimit,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        underlineColor = underlineColor,
        focusedUnderlineColor = focusedUnderlineColor,
        contentPadding = contentPadding,
        inputType = inputType,
        onFocusChangeListener = onFocusChangeListener,
        focusRequester = focusRequester,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        outlined = outlined,
        textStyle = textStyle,
        verticalAlignment = verticalAlignment,
    )
}

/** TextFieldValue overload — see file kdoc. */
@Composable
fun HcTextField(
    value: TextFieldValue,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    isError: Boolean? = false,
    isReadOnly: Boolean? = false,
    errorText: String? = "",
    enabled: Boolean = true,
    helperText: String? = "",
    onValueChange: (TextFieldValue) -> Unit = {},
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    trailingText: String? = null,
    singleLine: Boolean = false,
    showTopDivider: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    passwordToggleEnabled: Boolean = false,
    isCharLimited: Boolean = false,
    charLimit: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        capitalization = KeyboardCapitalization.Sentences,
        imeAction = if (singleLine) ImeAction.Next else ImeAction.Default,
    ),
    underlineColor: Color = HcColors.Border.Subtle,
    inputType: KeyboardType? = null,
    onFocusChangeListener: ((Boolean) -> Unit)? = { },
    focusRequester: FocusRequester? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    outlined: Boolean = false,
    textStyle: TextStyle = TextStyle.Default,
) {
    HcTextFieldV2(
        value = value,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        isError = isError,
        isReadOnly = isReadOnly,
        errorText = errorText,
        enabled = enabled,
        helperText = helperText,
        onValueChange = onValueChange,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        trailingText = trailingText,
        singleLine = singleLine,
        showTopDivider = showTopDivider,
        minLines = minLines,
        maxLines = maxLines,
        passwordToggleEnabled = passwordToggleEnabled,
        isCharLimited = isCharLimited,
        charLimit = charLimit,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        underlineColor = underlineColor,
        inputType = inputType,
        onFocusChangeListener = onFocusChangeListener,
        focusRequester = focusRequester,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        outlined = outlined,
        textStyle = textStyle,
    )
}
