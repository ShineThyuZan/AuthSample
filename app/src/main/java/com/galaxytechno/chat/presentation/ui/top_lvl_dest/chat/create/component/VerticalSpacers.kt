package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.sizing

@Composable
fun VerticalSpacerTiny() {
    Spacer(modifier = Modifier.height(MaterialTheme.sizing.spacerTiny))
}

@Composable
fun VerticalSpacerExtraSmall() {
    Spacer(modifier = Modifier.height(MaterialTheme.sizing.spacerExtraSmall))
}

@Composable
fun VerticalSpacerSmall() {
    Spacer(modifier = Modifier.height(MaterialTheme.sizing.spacerSmall))
}

@Composable
fun VerticalSpacerMedium() {
    Spacer(modifier = Modifier.height(MaterialTheme.sizing.spacerMedium))
}

@Composable
fun VerticalSpacerRegular() {
    Spacer(modifier = Modifier.height(MaterialTheme.sizing.spacerRegular))
}

@Composable
fun VerticalSpacerLarge() {
    Spacer(modifier = Modifier.height(MaterialTheme.sizing.spacerLarge))
}

@Composable
fun VerticalSpacerExtraLarge() {
    Spacer(modifier = Modifier.height(MaterialTheme.sizing.spacerExtraLarge))
}

@Composable
fun VerticalSpacerHuge() {
    Spacer(modifier = Modifier.height(MaterialTheme.sizing.spacerHuge))
}