package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.sizing


@Composable
fun ShimmerAvatarList(
    brush: Brush
) {
    Spacer(
        modifier = Modifier
            .size(size = MaterialTheme.sizing.avatarList)
            .fillMaxWidth()
            .clip(CircleShape)
            .background(brush = brush)
    )
}

@Composable
fun ShimmerAvatarDetail(
    brush: Brush
) {
    Spacer(
        modifier = Modifier
            .size(size = MaterialTheme.sizing.avatarDetail)
            .fillMaxWidth()
            .clip(CircleShape)
            .background(brush = brush)
    )
}

@Composable
fun ShimmerAvatarDefault(
    brush: Brush
) {
    Spacer(
        modifier = Modifier
            .size(size = MaterialTheme.sizing.avatarDefault)
            .fillMaxWidth()
            .clip(CircleShape)
            .background(brush = brush)
    )
}


@Composable
fun ShimmerText(
    fraction: Float,
    brush: Brush,
    height: Dp = MaterialTheme.sizing.spacerDefault,
) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth(fraction = fraction)
            .height(height = height)
            .clip(CircleShape)
            .background(brush = brush)
    )
}

@Composable
fun ShimmerTextFixed(
    brush: Brush,
    width: Dp = MaterialTheme.sizing.spacerDefault,
    height: Dp = MaterialTheme.sizing.spacerDefault,
) {
    Spacer(
        modifier = Modifier
            .width(width = width)
            .height(height = height)
            .clip(CircleShape)
            .background(brush = brush)
    )
}

@Composable
@Preview
fun ShimmerTextPreview() {
    val brush = Brush.linearGradient(
        listOf(
            Color.LightGray.copy(alpha = 0.5f),
            Color.LightGray.copy(alpha = 0.3f),
            Color.LightGray.copy(alpha = 0.5f),
        )
    )
    Surface {
        ShimmerText(
            fraction = 0.85f,
            brush = brush
        )
    }
}