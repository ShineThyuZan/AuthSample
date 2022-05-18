package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.sizing
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.titleMedium

import com.google.accompanist.coil.rememberCoilPainter

@Composable
fun AvatarUrl(
    url: String,
    size: Dp,
) {
    val image = rememberCoilPainter(
        request = url,
        fadeIn = true
    )
    Image(
        painter = image,
        contentDescription = "Avatar Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(size = size)
            .clip(CircleShape)
    )
}

@Composable
fun AvatarText(
    name: Char,
    size: Dp,
    textSize: TextStyle
) {
    Box(
        modifier = Modifier
            .size(size = size)
            .clip(CircleShape)
            .background(
                color = MaterialTheme.colors.primary.copy(
                    alpha = 0.5f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.toString(),
            style = textSize
        )
    }
}

@Composable
@Preview
private fun AvatarTextPreview() {
    Surface {
        AvatarText(
            name = 'K',
            size = MaterialTheme.sizing.avatarDefault,
            textSize = MaterialTheme.typography.titleMedium
        )
    }
}