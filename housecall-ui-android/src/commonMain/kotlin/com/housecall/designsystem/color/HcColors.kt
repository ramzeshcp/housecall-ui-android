@file:Suppress("MagicNumber", "unused")

package com.housecall.designsystem.color

import androidx.compose.ui.graphics.Color

/**
 * Port of `housecall.core.compose.HcColors` for the externalized
 * `housecall-ui-android` KMP library.
 *
 * **Migration note:** the original Android-side version defined every color
 * as a `@Composable get()` returning `colorResource(R.color.X)`. That
 * approach is not portable to commonMain (the `R` class is Android AAPT-only,
 * and `colorResource` is part of `androidx.compose.ui.res` which is also
 * Android-only).
 *
 * Hex values were lifted verbatim from `core/ui/src/main/res/values/colors.xml`
 * to preserve exact visual parity. If the source `colors.xml` ever updates,
 * this file must be re-synced.
 */
object HcColors {
    object Primary {
        val Main = Color(0xFF0057FF)
        val Light = Color(0xFFB2CDFF)
        val LightRevamped = Color(0xFFE3ECFF)
        val Dark = Color(0xFF0046CC)
        val OnPrimary = Color(0xFFFFFFFF)
        val Container = Color(0xFFE3ECFF)
        val OnContainer = Color(0xFF15181D)
    }

    object Secondary {
        val Main = Color(0xFF6B7487)
        val Light = Color(0xFFCAD1E0)
        val Dark = Color(0xFF464E5A)
        val OnSecondary = Color(0xFFFFFFFF)
        val Container = Color(0xFFF2F4F7)
        val OnContainer = Color(0xFF15181D)
    }

    object Success {
        val Main = Color(0xFF00A344)
        val Light = Color(0xFFB9F6CA)
        val Dark = Color(0xFF007D33)
        val OnSuccess = Color(0xFFFFFFFF)
    }

    object Warning {
        val Main = Color(0xFFBF8600)
        val Light = Color(0xFFFFE082)
        val Dark = Color(0xFF8A6100)
        val OnWarning = Color(0xFFFFFFFF)
    }

    object Error {
        val Main = Color(0xFFD81860)
        val MainRevamp = Color(0xFFD81860)
        val Light = Color(0xFFFFDDE9)
        val Dark = Color(0xFFA01045)
        val OnError = Color(0xFFFFFFFF)
    }

    object Info {
        val Main = Color(0xFFB2CDFF)
        val Light = Color(0xFFE3ECFF)
        val Dark = Color(0xFF0057FF)
        val OnInfo = Color(0xFF15181D)
    }

    object Surface {
        val Default = Color(0xFFFFFFFF)
        val Background = Color(0xFFFAFBFF)
        val BackgroundSecondary = Color(0xFFF2F6FD)
        val Elevated01 = Color(0xFFFFFFFF)
        val Elevated02 = Color(0xFFFFFFFF)
        val Inverse = Color(0xFF15181D)
        val OnInverse = Color(0xFFFFFFFF)
        val ButtonSecondary = Color(0xFFE3ECFF)
        val ButtonNeutral = Color(0xFFF2F4F7)
    }

    object Text {
        val OnSurfacePrimary = Color(0xFF15181D)
        val OnSurfaceSecondary = Color(0xFF6B7487)
        val OnSurfaceDisabled = Color(0xFFCAD1E0)
        val OnSurfaceHint = Color(0x618C96AA)
    }

    object Border {
        val Subtle = Color(0xFFE1E6F2)
        val Default = Color(0xFFCAD1E0)
        val Emphasis = Color(0xFF8C96AA)
        val Divider = Color(0xFFE1E6F2)
    }

    object ButtonBackground {
        val Primary = Color(0xFF0057FF)
        val Secondary = Color(0xFFE3ECFF)
        val Neutral = Color(0xFFF2F4F7)
        val Hover = Color(0xFF0046CC)
    }

    object Grey {
        val c50 = Color(0xFFFAFBFF)
        val c100 = Color(0xFFF7F9FF)
        val c150 = Color(0xFFEDF1FA)
        val c200 = Color(0xFFEDF1FA)
        val c300 = Color(0xFFE1E6F2)
        val c400 = Color(0xFFCAD1E0)
        val c500 = Color(0xFFAEB8CC)
        val c600 = Color(0xFF8C96AA)
        val c700 = Color(0xFF6B7487)
        val c800 = Color(0xFF464E5A)
        val c900 = Color(0xFF23272F)
    }

    object Common {
        val Black = Color(0xFF15181D)
        val White = Color(0xFFFFFFFF)
    }
}
