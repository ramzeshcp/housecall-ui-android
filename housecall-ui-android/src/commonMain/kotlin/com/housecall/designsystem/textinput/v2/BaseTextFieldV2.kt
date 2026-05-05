@file:Suppress("MatchingDeclarationName", "MagicNumber", "LongParameterList", "LongMethod", "CyclomaticComplexMethod")

package com.housecall.designsystem.textinput.v2

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TextFieldDefaults.indicatorLine
import androidx.compose.material.TextFieldDefaults.textFieldWithLabelPadding
import androidx.compose.material.TextFieldDefaults.textFieldWithoutLabelPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.semantics.SemanticsModifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.housecall.designsystem.color.HcColors
import com.housecall.designsystem.textinput.TrailingTextAdornment
import com.housecall.designsystem.textinput.outlinedTextFieldColors
import com.housecall.designsystem.textinput.textFieldColors
import com.housecall.designsystem.typography.HcTypography
import com.housecall.designsystem.utils.conditional

/**
 * Port of `housecall.core.ui.textinput.v2.BaseTextFieldV2` (private file
 * `TextFieldComponentV2.kt`).
 *
 * Public-internal: invoked from `HcTextFieldV2`, `HcSelectTextFieldV2`,
 * `HcCounterTextFieldV2`, `HcNumberInputFieldV2`. The whole file is pure
 * Compose — Compose Multiplatform's `compose.material` exposes `indicatorLine`,
 * `TextFieldDecorationBox`, `textFieldWithLabelPadding` and
 * `textFieldWithoutLabelPadding` identically to androidx.compose.material.
 */
@Composable
internal fun BaseTextFieldV2(
    value: TextFieldValue,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    wideLeadingContent: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    trailingText: String? = null,
    helperText: String? = "",
    errorText: String? = "",
    readOnly: Boolean = false,
    singleLine: Boolean = false,
    showTopDivider: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    isPlaceholderHighlighted: Boolean = false,
    isCharLimited: Boolean = false,
    charLimit: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        capitalization = KeyboardCapitalization.Sentences,
        imeAction = if (singleLine) ImeAction.Next else ImeAction.Default,
    ),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    underlineColor: Color = HcColors.Border.Subtle,
    focusedUnderlineColor: Color = HcColors.Primary.Main,
    isUnderlineActive: Boolean = false,
    outlined: Boolean = false,
    textStyle: TextStyle = TextStyle.Default,
    inputType: KeyboardType? = null,
    contentPadding: PaddingValues? = null,
    onFocusChangeListener: ((Boolean) -> Unit)? = null,
    focusRequester: FocusRequester? = null,
    onValueChange: (TextFieldValue) -> Unit = {},
    horizontalScroll: Boolean = false,
    fillHeight: Boolean = false,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
) {
    var isFocused by remember { mutableStateOf(false) }

    val showError = isError || (isCharLimited && value.text.length > charLimit)
    val labelColor = if (showError) {
        HcColors.Error.Main
    } else if (!enabled) {
        HcColors.Text.OnSurfaceDisabled
    } else if (isFocused) {
        HcColors.Primary.Main
    } else {
        HcColors.Text.OnSurfaceSecondary
    }

    val placeholderColor = if (isPlaceholderHighlighted && showError) {
        HcColors.Error.Main
    } else if (!isPlaceholderHighlighted) {
        HcColors.Text.OnSurfaceSecondary
    } else {
        HcColors.Text.OnSurfacePrimary
    }

    val underlineBorderColor = when {
        showError -> HcColors.Error.Main
        isFocused -> focusedUnderlineColor
        else -> underlineColor
    }
    val showPlaceHolderText = !placeholder.isNullOrEmpty() || (label.isNullOrEmpty() && placeholder.isNullOrEmpty())
    val isWide = wideLeadingContent != null

    Column(modifier = modifier) {
        if (showTopDivider && !outlined) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = HcColors.Border.Subtle,
            )
        }
        Box(modifier = if (fillHeight) Modifier.weight(1f, fill = false) else Modifier) {
            CoreTextFieldV2(
                value = value,
                modifier = if (fillHeight) Modifier.fillMaxSize() else Modifier.fillMaxWidth()
                    .then(modifier.extractSemanticsOnly()),
                readOnly = readOnly,
                label = label,
                minLines = minLines,
                maxLines = maxLines,
                charLimit = charLimit,
                showError = showError,
                isUnderlineActive = isUnderlineActive,
                showPlaceHolderText = showPlaceHolderText,
                placeholder = placeholder,
                visualTransformation = visualTransformation,
                wideLeadingContent = wideLeadingContent,
                leadingIcon = leadingIcon,
                trailingText = trailingText,
                trailingIcon = trailingIcon,
                enabled = enabled,
                keyboardActions = keyboardActions,
                keyboardOptions = keyboardOptions,
                interactionSource = interactionSource,
                inputType = inputType,
                onValueChange = onValueChange,
                onFocusChangeListener = {
                    isFocused = it
                    onFocusChangeListener?.invoke(it)
                },
                focusRequester = focusRequester,
                placeholderColor = placeholderColor,
                labelColor = labelColor,
                hasWideLeadingContent = isWide,
                underlineBorderColor = underlineBorderColor,
                underlineColor = underlineColor,
                singleLine = singleLine,
                outlined = outlined,
                textStyle = textStyle,
                contentPadding = contentPadding,
                horizontalScroll = horizontalScroll,
                verticalAlignment = verticalAlignment,
            )
            if (!outlined) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (isFocused || isUnderlineActive) 2.dp else 1.dp)
                        .align(Alignment.BottomStart),
                    color = if (!enabled) HcColors.Text.OnSurfaceDisabled else underlineBorderColor,
                )
            }
        }

        val (helperTextValue, helperTextColor) = if ((isError && !errorText.isNullOrEmpty()) || showError) {
            Pair(errorText, HcColors.Error.Main)
        } else if (!helperText.isNullOrEmpty() || isCharLimited) {
            Pair(helperText, HcColors.Text.OnSurfaceSecondary)
        } else {
            Pair("", HcColors.Text.OnSurfaceDisabled)
        }

        if (enabled) {
            Row(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
                if (!helperTextValue.isNullOrEmpty()) {
                    Text(
                        text = helperTextValue,
                        color = helperTextColor,
                        style = HcTypography.Caption,
                        modifier = Modifier.padding(vertical = 2.dp),
                    )
                }
                if (isCharLimited && charLimit > 0) {
                    Spacer(Modifier.weight(1f))
                    Text(
                        "${value.text.length}/$charLimit",
                        color = helperTextColor,
                        style = HcTypography.Caption,
                        modifier = Modifier.padding(top = 2.dp, bottom = 2.dp, start = 16.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun CoreTextFieldV2(
    value: TextFieldValue,
    readOnly: Boolean,
    showError: Boolean,
    showPlaceHolderText: Boolean,
    enabled: Boolean,
    singleLine: Boolean,
    placeholderColor: Color,
    labelColor: Color,
    hasWideLeadingContent: Boolean,
    underlineBorderColor: Color,
    underlineColor: Color,
    keyboardActions: KeyboardActions,
    keyboardOptions: KeyboardOptions,
    interactionSource: MutableInteractionSource,
    visualTransformation: VisualTransformation,
    label: String?,
    placeholder: String?,
    trailingText: String?,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    charLimit: Int = Int.MAX_VALUE,
    inputType: KeyboardType? = null,
    onFocusChangeListener: ((Boolean) -> Unit)? = null,
    focusRequester: FocusRequester? = null,
    wideLeadingContent: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    outlined: Boolean = false,
    isUnderlineActive: Boolean = false,
    textStyle: TextStyle = TextStyle.Default,
    contentPadding: PaddingValues? = null,
    horizontalScroll: Boolean = false,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
) {
    var isFocused by remember { mutableStateOf(false) }
    val customTextSelectionColors = TextSelectionColors(
        handleColor = HcColors.Primary.Main,
        backgroundColor = HcColors.Primary.Main.copy(alpha = 0.2f),
    )

    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        InternalTextFieldV2(
            value = value,
            readOnly = readOnly,
            outlined = outlined,
            isUnderlineActive = isUnderlineActive,
            onValueChange = { newValue ->
                if (newValue.text.length <= charLimit) {
                    onValueChange(newValue)
                }
            },
            label = if (!label.isNullOrEmpty()) {
                {
                    val labelText = if (showError && isFocused) "$label*" else label
                    Text(
                        text = labelText,
                        fontWeight = if (isFocused || value.text.isNotEmpty()) {
                            FontWeight.SemiBold
                        } else {
                            FontWeight.Normal
                        },
                        fontFamily = HcTypography.ManropeRegular,
                    )
                }
            } else null,
            placeholder = if (showPlaceHolderText && placeholder?.isNotEmpty() == true) {
                {
                    Text(
                        text = placeholder,
                        style = HcTypography.Body1,
                    )
                }
            } else null,
            visualTransformation = visualTransformation,
            leadingIcon = if (wideLeadingContent == null) leadingIcon else null,
            wideLeadingContent = wideLeadingContent,
            trailingIcon = if (!trailingText.isNullOrEmpty() && trailingIcon == null) {
                { TrailingTextAdornment(trailingText, enabled, 16.dp) }
            } else trailingIcon,
            keyboardActions = keyboardActions,
            keyboardOptions = if (inputType != null) {
                keyboardOptions.copy(keyboardType = inputType)
            } else {
                keyboardOptions
            },
            interactionSource = interactionSource,
            modifier = modifier
                .onFocusChanged {
                    if (isFocused != it.isFocused) {
                        isFocused = it.isFocused
                    }
                }
                .conditional(focusRequester != null) {
                    focusRequester(focusRequester!!)
                },
            onFocusChangeListener = onFocusChangeListener,
            focusRequester = focusRequester,
            underlineBorderColor = underlineBorderColor,
            underlineColor = underlineColor,
            contentPadding = contentPadding,
            colors = if (outlined) {
                outlinedTextFieldColors(placeholderColor, labelColor)
            } else {
                textFieldColors(
                    placeholderColor,
                    labelColor,
                    hasWideLeadingContent,
                    underlineBorderColor,
                    underlineColor,
                )
            },
            enabled = enabled,
            isError = showError,
            textStyle = textStyle.copy(fontSize = 16.sp),
            singleLine = singleLine,
            minLines = minLines,
            maxLines = maxLines,
            horizontalScroll = horizontalScroll,
            verticalAlignment = verticalAlignment,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun InternalTextFieldV2(
    value: TextFieldValue,
    keyboardActions: KeyboardActions,
    keyboardOptions: KeyboardOptions,
    visualTransformation: VisualTransformation,
    onValueChange: (TextFieldValue) -> Unit,
    onFocusChangeListener: ((Boolean) -> Unit)?,
    focusRequester: FocusRequester?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    showError: Boolean = false,
    underlineBorderColor: Color = HcColors.Primary.Main,
    underlineColor: Color = HcColors.Border.Subtle,
    outlined: Boolean = false,
    isUnderlineActive: Boolean = false,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    wideLeadingContent: @Composable (() -> Unit)? = null,
    contentPadding: PaddingValues? = null,
    horizontalScroll: Boolean = false,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
) {
    var isFocused by remember { mutableStateOf(false) }
    val textColor = textStyle.color.takeOrElse {
        colors.textColor(enabled).value
    }
    val shape = if (outlined) {
        RoundedCornerShape(8.dp)
    } else {
        MaterialTheme.shapes.small.copy(bottomEnd = ZeroCornerSize, bottomStart = ZeroCornerSize)
    }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))
    val currentUnderlineBorderColor = when {
        showError || isError -> HcColors.Error.Main
        (isFocused || isUnderlineActive) && !readOnly -> underlineBorderColor
        else -> underlineColor
    }

    BasicTextField(
        value = value,
        modifier = modifier
            .background(colors.backgroundColor(enabled).value, shape)
            .border(
                width = if (outlined) {
                    if ((isFocused || isUnderlineActive) && !readOnly) 2.dp else 1.dp
                } else {
                    0.dp
                },
                color = if (outlined) currentUnderlineBorderColor else Color.Transparent,
                shape = shape,
            )
            .indicatorLine(
                enabled = enabled,
                isError = isError,
                interactionSource = interactionSource,
                colors = colors,
                focusedIndicatorLineThickness = if (outlined) 0.dp else 2.dp,
                unfocusedIndicatorLineThickness = if (outlined) 0.dp else 1.dp,
            )
            .defaultMinSize(
                minWidth = TextFieldDefaults.MinWidth,
                minHeight = TextFieldDefaults.MinHeight,
            )
            .onFocusChanged {
                if (isFocused != it.isFocused) {
                    isFocused = it.isFocused
                    onFocusChangeListener?.invoke(it.isFocused)
                }
            }
            .let {
                if (focusRequester != null) {
                    it.focusRequester(focusRequester)
                }
                it
            }
            .conditional(horizontalScroll) {
                horizontalScroll(rememberScrollState(), enabled = false)
            },
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = mergedTextStyle,
        cursorBrush = SolidColor(colors.cursorColor(isError).value),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        decorationBox = @Composable { innerTextField ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = if (singleLine) verticalAlignment else Alignment.Top,
            ) {
                if (wideLeadingContent != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 16.dp)
                            .then(if (!enabled) Modifier.alpha(0.25f) else Modifier),
                    ) {
                        wideLeadingContent.invoke()
                    }
                }
                Box(modifier = Modifier.weight(1f)) {
                    TextFieldDefaults.TextFieldDecorationBox(
                        value = value.text,
                        visualTransformation = visualTransformation,
                        innerTextField = innerTextField,
                        placeholder = placeholder,
                        label = label,
                        leadingIcon = if (wideLeadingContent == null) leadingIcon else null,
                        trailingIcon = null,
                        singleLine = singleLine,
                        enabled = enabled,
                        isError = isError,
                        interactionSource = interactionSource,
                        colors = colors,
                        contentPadding = contentPadding ?: if (label == null) {
                            textFieldWithoutLabelPadding()
                        } else if (value.annotatedString.isNotEmpty() || isFocused) {
                            textFieldWithLabelPadding()
                        } else {
                            PaddingValues(16.dp)
                        },
                    )
                }
                if (trailingIcon != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 16.dp),
                    ) {
                        trailingIcon.invoke()
                    }
                }
            }
        },
    )
}

// Extension function to extract only SemanticsModifier
private fun Modifier.extractSemanticsOnly(): Modifier {
    var result: Modifier = Modifier
    this.foldOut(Unit) { element, _ ->
        if (element is SemanticsModifier) {
            result = result.then(element)
        }
    }
    return result
}
