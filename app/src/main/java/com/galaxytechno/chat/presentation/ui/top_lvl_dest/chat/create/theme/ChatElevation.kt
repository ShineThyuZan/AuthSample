package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ChatElevation(
    val default: Dp = 0.dp,
    val tiny: Dp = 2.dp,
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val regular: Dp = 0.dp,
    val large: Dp = 0.dp,
    val extraLarge: Dp = 0.dp
)

val LocalElevation = compositionLocalOf {
    ChatElevation()
}
val MaterialTheme.chatElevation: ChatElevation
    @Composable
    @ReadOnlyComposable
    get() = LocalElevation.current