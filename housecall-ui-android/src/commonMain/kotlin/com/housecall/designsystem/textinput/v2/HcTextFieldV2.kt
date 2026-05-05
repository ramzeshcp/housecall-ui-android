@file:Suppress("MatchingDeclarationName", "MagicNumber", "LongParameterList", "LongMethod", "CyclomaticComplexMethod")

package com.housecall.designsystem.textinput.v2

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.housecall.designsystem.color.HcColors
import com.housecall.housecall_ui_android.generated.resources.Res
import com.housecall.housecall_ui_android.generated.resources.ic_visibility_off
import com.housecall.housecall_ui_android.generated.resources.ic_visibility_on
import org.jetbrains.compose.resources.painterResource

/**
 * Port of `housecall.core.ui.textinput.v2.HcTextFieldV2`.
 *
 * **Migration changes:**
 * - Dropped `textWatcher: TextWatcher? = null` parameter (decision #3 — Android-only legacy hook).
 * - Replaced `painterResource(R.drawable.ic_visibility_*)` with
 *   `painterResource(Res.drawable.ic_visibility_*)` (Compose Multiplatform Resources).
 * - Dropped `@Preview`-marked demo functions (Android tooling only).
 */
@Composable
fun HcTextFieldV2(
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
    fillHeight: Boolean = false,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
) {
    var passwordHidden by rememberSaveable { mutableStateOf(true) }

    // Taken from BasicTextField.kt (androidx.compose.foundation.text)
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = value)) }
    val textFieldValue = textFieldValueState.copy(text = value)

    SideEffect {
        if (textFieldValue.selection != textFieldValueState.selection ||
            textFieldValue.composition != textFieldValueState.composition
        ) {
            textFieldValueState = textFieldValue
        }
    }
    var lastTextValue by remember(value) { mutableStateOf(value) }

    BaseTextFieldV2(
        value = textFieldValue,
        modifier = modifier,
        onValueChange = { newTextFieldValue ->
            textFieldValueState = newTextFieldValue
            val stringChangedSinceLastInvocation = lastTextValue != newTextFieldValue.text
            val newValue = newTextFieldValue.text
            lastTextValue = newValue
            if (stringChangedSinceLastInvocation) {
                onValueChange(newValue)
            }
        },
        showTopDivider = showTopDivider,
        label = label,
        placeholder = placeholder,
        isError = isError ?: false,
        readOnly = isReadOnly ?: false,
        enabled = enabled,
        minLines = minLines,
        maxLines = maxLines,
        leadingIcon = leadingIcon?.let {
            {
                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center,
                ) { it() }
            }
        },
        trailingIcon = trailingIcon?.let {
            {
                Row {
                    Box(
                        modifier = Modifier.size(24.dp),
                        contentAlignment = Alignment.Center,
                    ) { it() }
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        } ?: if (passwordToggleEnabled) {
            {
                Row {
                    Box(
                        modifier = Modifier.size(24.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        val icon = painterResource(
                            if (passwordHidden) Res.drawable.ic_visibility_off else Res.drawable.ic_visibility_on,
                        )
                        IconButton(onClick = { passwordHidden = !passwordHidden }) {
                            Icon(
                                painter = icon,
                                contentDescription = null,
                                tint = if (enabled) HcColors.Common.Black else HcColors.Text.OnSurfaceDisabled,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        } else {
            null
        },
        trailingText = trailingText,
        helperText = helperText,
        errorText = errorText ?: "",
        singleLine = singleLine,
        isCharLimited = isCharLimited,
        charLimit = charLimit,
        visualTransformation = if (passwordToggleEnabled && passwordHidden) {
            PasswordVisualTransformation()
        } else {
            visualTransformation
        },
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
        fillHeight = fillHeight,
        verticalAlignment = verticalAlignment,
    )
}

/** TextFieldValue overload — see file kdoc for migration notes. */
@Composable
fun HcTextFieldV2(
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
    var passwordHidden by rememberSaveable { mutableStateOf(true) }

    BaseTextFieldV2(
        value = value,
        modifier = modifier,
        onValueChange = onValueChange,
        showTopDivider = showTopDivider,
        label = label,
        placeholder = placeholder,
        isError = isError ?: false,
        readOnly = isReadOnly ?: false,
        enabled = enabled,
        minLines = minLines,
        maxLines = maxLines,
        leadingIcon = leadingIcon?.let {
            {
                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center,
                ) { it() }
            }
        },
        trailingIcon = buildTrailingIcon(
            trailingIcon = trailingIcon,
            passwordToggleEnabled = passwordToggleEnabled,
            passwordHidden = passwordHidden,
            enabled = enabled,
            onPasswordToggle = { passwordHidden = !passwordHidden },
        ),
        trailingText = trailingText,
        helperText = helperText,
        errorText = errorText ?: "",
        singleLine = singleLine,
        isCharLimited = isCharLimited,
        charLimit = charLimit,
        visualTransformation = if (passwordToggleEnabled && passwordHidden) {
            PasswordVisualTransformation()
        } else {
            visualTransformation
        },
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

@Composable
internal fun buildTrailingIcon(
    enabled: Boolean,
    trailingIcon: (@Composable (() -> Unit))?,
    passwordToggleEnabled: Boolean,
    passwordHidden: Boolean,
    onPasswordToggle: () -> Unit,
): (@Composable () -> Unit)? {
    return trailingIcon?.let {
        {
            Row {
                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center,
                ) { it() }
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    } ?: if (passwordToggleEnabled) {
        {
            Row {
                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    val icon = painterResource(
                        if (passwordHidden) Res.drawable.ic_visibility_off else Res.drawable.ic_visibility_on,
                    )
                    IconButton(onClick = onPasswordToggle) {
                        Icon(
                            painter = icon,
                            contentDescription = null,
                            tint = if (enabled) HcColors.Common.Black else HcColors.Text.OnSurfaceDisabled,
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    } else {
        null
    }
}
