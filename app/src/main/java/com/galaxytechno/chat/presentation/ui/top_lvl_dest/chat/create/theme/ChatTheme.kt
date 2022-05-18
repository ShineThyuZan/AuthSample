package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val LightColorPalette = lightColors(
    primary = Green500,
    onPrimary = Gray50,
    primaryVariant = Green700,
    secondary = Green600,
    onSecondary = Gray100,
    secondaryVariant = Green800,
    error = Red500,
    onError = Gray50,

    surface = Light,
    onSurface = Gray900,
    background = Gray100,
    onBackground = Dark,
)
private val DarkColorPalette = darkColors(
    primary = Green500,
    onPrimary = Gray900,
    primaryVariant = Green700,
    secondary = Green600,
    onSecondary = NaturalGray800,
    secondaryVariant = Green800,
    error = Red500,
    onError = NaturalGray900,

    surface = NaturalGray900,
    onSurface = NaturalGray50,
    background = NaturalGray800,
    onBackground = Light,
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else
        LightColorPalette
    CompositionLocalProvider(
        LocalSpacing provides ChatSpacing(),
        LocalElevation provides ChatElevation(),
        LocalSize provides ChatSizing(),
        LocalOverScrollConfiguration provides null,
    ) {
        MaterialTheme(
            colors = colors,
            typography = chatTypography,
            shapes = chatShapes,
            content = content,
        )
    }
}
