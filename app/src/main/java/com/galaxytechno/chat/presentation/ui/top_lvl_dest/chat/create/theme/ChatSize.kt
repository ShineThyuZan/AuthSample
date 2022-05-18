package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ChatSizing(
    val smallShape: Dp = 4.dp,
    val mediumShape: Dp = 8.dp,
    val largeShape: Dp = 16.dp,
    val splashImageSize: Dp = 120.dp,
    val authImageSize: Dp = 32.dp,
    val countryImageHeight: Dp = 24.dp,
    val countryImageWidth: Dp = 32.dp,


    val avatarDefault: Dp = 48.dp,
    val avatarDetail: Dp = 80.dp,
    val avatarList: Dp = 40.dp,
    val avatarTopBar: Dp = 32.dp,

    val serviceSize: Dp = 120.dp,
    val detailActionSize: Dp = 64.dp,

    //spacer
    val spacerDefault: Dp = 12.dp,
    val spacerTiny: Dp = 2.dp,
    val spacerExtraSmall: Dp = 4.dp,
    val spacerSmall: Dp = 8.dp,
    val spacerMedium: Dp = 16.dp,
    val spacerRegular: Dp = 24.dp,
    val spacerLarge: Dp = 32.dp,
    val spacerExtraLarge: Dp = 64.dp,
    val spacerHuge: Dp = 128.dp,

    //text fields
    val textFieldHeight: Dp = 48.dp,
    val bioTextFieldHeight: Dp = 128.dp,

    //button
    val socialButtonSize: Dp = 48.dp,

    //chat
    val chatImageWidth: Dp = 160.dp,
    val chatImageHeight: Dp = 220.dp,

    val topBarHeight: Dp = 56.dp,

    val emptyImageSize: Dp = 160.dp,

    val voiceRecorderSize: Dp = 80.dp,
    val voiceIconSize: Dp = 48.dp,
    val voiceActionIconSize: Dp = 56.dp,

    val iconButtonSize: Dp = 20.dp,
)

val LocalSize = compositionLocalOf {
    ChatSizing()
}
val MaterialTheme.sizing: ChatSizing
    @Composable
    @ReadOnlyComposable
    get() = LocalSize.current