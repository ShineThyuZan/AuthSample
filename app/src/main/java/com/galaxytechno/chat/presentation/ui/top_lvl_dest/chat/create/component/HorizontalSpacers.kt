package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.sizing

@Composable
fun HorizontalSpacerTiny() {
    Spacer(modifier = Modifier.width(MaterialTheme.sizing.spacerTiny))
}

@Composable
fun HorizontalSpacerExtraSmall() {
    Spacer(modifier = Modifier.width(MaterialTheme.sizing.spacerExtraSmall))
}

@Composable
fun HorizontalSpacerSmall() {
    Spacer(modifier = Modifier.width(MaterialTheme.sizing.spacerSmall))
}

@Composable
fun HorizontalSpacerMedium() {
    Spacer(modifier = Modifier.width(MaterialTheme.sizing.spacerMedium))
}

@Composable
fun HorizontalSpacerRegular() {
    Spacer(modifier = Modifier.width(MaterialTheme.sizing.spacerRegular))
}

@Composable
fun HorizontalSpacerLarge() {
    Spacer(modifier = Modifier.width(MaterialTheme.sizing.spacerLarge))
}

@Composable
fun HorizontalSpacerExtraLarge() {
    Spacer(modifier = Modifier.width(MaterialTheme.sizing.spacerExtraLarge))
}

@Composable
fun HorizontalSpacerHuge() {
    Spacer(modifier = Modifier.width(MaterialTheme.sizing.spacerHuge))
}