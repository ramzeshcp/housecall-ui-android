package com.housecall.designsystem.typography

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.housecall.housecall_ui_android.generated.resources.Res
import com.housecall.housecall_ui_android.generated.resources.manrope_bold
import com.housecall.housecall_ui_android.generated.resources.manrope_extrabold
import com.housecall.housecall_ui_android.generated.resources.manrope_regular
import com.housecall.housecall_ui_android.generated.resources.manrope_semibold
import org.jetbrains.compose.resources.Font

/**
 * Port of `housecall.core.compose.HcTypography` for the externalized
 * `housecall-ui-android` KMP library.
 *
 * **Migration note:** the original Android-side version defined fonts as
 * `FontFamily(Font(R.font.manrope_X, FontWeight.X))` — references to
 * `R.font.*` resources. In commonMain we use the cross-platform Compose
 * Resources API: `Font(Res.font.manrope_X, FontWeight.X)`.
 *
 * The `.ttf` files themselves are bundled under
 * `commonMain/composeResources/font/manrope_*.ttf` (copied verbatim from
 * `core/ui/src/main/res/font/`). They get packaged into the AAR's
 * `assets/composeResources/.../font/` directory and resolved at runtime.
 *
 * Because `Font(...)` here is a `@Composable` function (not a constant),
 * the FontFamily declarations are wrapped in `@Composable` getters.
 */
@Suppress("MagicNumber", "unused")
object HcTypography {

    val ManropeRegular: FontFamily
        @Composable get() = FontFamily(Font(Res.font.manrope_regular, FontWeight.Normal))

    val ManropeSemiBold: FontFamily
        @Composable get() = FontFamily(Font(Res.font.manrope_semibold, FontWeight.SemiBold))

    val ManropeBold: FontFamily
        @Composable get() = FontFamily(Font(Res.font.manrope_bold, FontWeight.Bold))

    val ManropeExtraBold: FontFamily
        @Composable get() = FontFamily(Font(Res.font.manrope_extrabold, FontWeight.ExtraBold))

    val DisplayXl: TextStyle
        @Composable get() = TextStyle(
            fontFamily = ManropeExtraBold,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = 0.sp,
        )

    val DisplayLg: TextStyle
        @Composable get() = TextStyle(
            fontFamily = ManropeExtraBold,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 28.sp,
            lineHeight = 36.sp,
        )

    val DisplayDefault: TextStyle
        @Composable get() = TextStyle(
            fontFamily = ManropeBold,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            lineHeight = 32.sp,
        )

    val HeadingLg: TextStyle
        @Composable get() = TextStyle(
            fontFamily = ManropeBold,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            lineHeight = 28.sp,
        )

    val HeadingDefault: TextStyle
        @Composable get() = TextStyle(
            fontFamily = ManropeBold,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 28.sp,
        )

    val HeadingSm: TextStyle
        @Composable get() = TextStyle(
            fontFamily = ManropeSemiBold,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            lineHeight = 24.sp,
        )

    val HeadingXs: TextStyle
        @Composable get() = TextStyle(
            fontFamily = ManropeSemiBold,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 22.sp,
        )

    val HeadingSmBold: TextStyle
        @Composable get() = TextStyle(
            fontFamily = ManropeBold,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            lineHeight = 24.sp,
        )

    val HeadingXsBold: TextStyle
        @Composable get() = TextStyle(
            fontFamily = ManropeBold,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            lineHeight = 22.sp,
        )

    val SubtitleDefault: TextStyle
        @Composable get() = TextStyle(
            fontFamily = ManropeBold,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 20.sp,
        )

    val SubtitleSm: TextStyle
        @Composable get() = TextStyle(
            fontFamily = ManropeBold,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            lineHeight = 18.sp,
        )

    val BodyDefault: TextStyle
        @Composable get() = TextStyle(
            fontFamily = ManropeRegular,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
        )

    val BodySm: TextStyle
        @Composable get() = TextStyle(
            fontFamily = ManropeRegular,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
        )

    val LabelDefault: TextStyle
        @Composable get() = TextStyle(
            fontFamily = ManropeSemiBold,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 18.sp,
        )

    val LabelSm: TextStyle
        @Composable get() = TextStyle(
            fontFamily = ManropeSemiBold,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            lineHeight = 14.sp,
        )

    // ===== Legacy Material 2 aliases mapped to MML typescale =====
    val H1: TextStyle @Composable get() = DisplayXl
    val H2: TextStyle @Composable get() = HeadingLg
    val H3: TextStyle @Composable get() = HeadingDefault
    val H4: TextStyle @Composable get() = DisplayXl
    val H5: TextStyle @Composable get() = HeadingLg
    val H6: TextStyle @Composable get() = HeadingDefault
    val Subtitle1: TextStyle @Composable get() = HeadingSm
    val Subtitle2: TextStyle @Composable get() = HeadingXs
    val Body1: TextStyle @Composable get() = BodyDefault
    val Body2: TextStyle @Composable get() = BodySm
    val Button: TextStyle @Composable get() = LabelDefault
    val Caption: TextStyle
        @Composable get() = SubtitleSm.copy(
            fontWeight = FontWeight.Normal,
            fontFamily = ManropeRegular,
        )
    val Toolbar: TextStyle @Composable get() = HeadingDefault

    val Typography: Typography
        @Composable get() = Typography(
            h1 = H1,
            h2 = H2,
            h3 = H3,
            h4 = H4,
            h5 = H5,
            h6 = H6,
            subtitle1 = Subtitle1,
            subtitle2 = Subtitle2,
            body1 = Body1,
            body2 = Body2,
            caption = Caption,
        )
}
