package com.housecall.designsystem.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

/**
 * Direct port of `housecall.core.compose.utils.Modifier.conditional`.
 * Pure Compose, no Android-only deps.
 */
fun Modifier.conditional(
    condition: Boolean,
    modifier: @Composable Modifier.() -> Modifier,
): Modifier = composed {
    if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}
