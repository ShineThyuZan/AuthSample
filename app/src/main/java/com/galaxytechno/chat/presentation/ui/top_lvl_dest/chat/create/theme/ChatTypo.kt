package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.galaxytechno.chat.R

val chatTypography = Typography()

val interFontFamily = FontFamily(
    Font(resId = R.font.inter_regular),
)

val Typography.displayLarge: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontFamily = interFontFamily,
            fontSize = 57.sp,
            lineHeight = 64.sp,
            letterSpacing = 0.sp,
            fontWeight = FontWeight.Normal
        )
    }
val Typography.displayMedium: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontFamily = interFontFamily,
            fontSize = 45.sp,
            lineHeight = 52.sp,
            letterSpacing = 0.sp,
            fontWeight = FontWeight.Normal
        )
    }
val Typography.displaySmall: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontFamily = interFontFamily,
            fontSize = 36.sp,
            lineHeight = 44.sp,
            letterSpacing = 0.sp,
            fontWeight = FontWeight.Normal
        )
    }

val Typography.headlineLarge: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontFamily = interFontFamily,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = 0.sp,
            fontWeight = FontWeight.Normal
        )
    }

val Typography.headlineMedium: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontFamily = interFontFamily,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            letterSpacing = 0.sp,
            fontWeight = FontWeight.Normal
        )
    }

val Typography.headlineSmall: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontFamily = interFontFamily,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.sp,
            fontWeight = FontWeight.Normal
        )
    }

val Typography.titleLarge: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontFamily = interFontFamily,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp,
            fontWeight = FontWeight.Normal
        )
    }

val Typography.titleMedium: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontFamily = interFontFamily,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.009375.sp,
            fontWeight = FontWeight.Medium
        )
    }

val Typography.titleSmall: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontFamily = interFontFamily,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.0071428571.sp,
            fontWeight = FontWeight.Medium
        )
    }

val Typography.labelLarge: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontFamily = interFontFamily,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.0071428571.sp,
            fontWeight = FontWeight.Medium
        )
    }

val Typography.labelMedium: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontFamily = interFontFamily,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.0416666667.sp,
            fontWeight = FontWeight.Medium
        )
    }

val Typography.labelSmall: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontFamily = interFontFamily,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.0454545455.sp,
            fontWeight = FontWeight.Medium
        )
    }

val Typography.bodyLarge: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontFamily = interFontFamily,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.009375.sp,
            fontWeight = FontWeight.Normal
        )
    }

val Typography.bodyMedium: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontFamily = interFontFamily,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.0178571429.sp,
            fontWeight = FontWeight.Normal
        )
    }

val Typography.bodySmall: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontFamily = interFontFamily,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.033333333.sp,
            fontWeight = FontWeight.Normal,
        )
    }





