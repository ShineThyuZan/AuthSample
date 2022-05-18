package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.newmessage.components.shimmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.sizing
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.spacing
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component.HorizontalSpacerSmall
import kotlin.random.Random

@Composable
fun NewMessageShimmerItem(
    modifier: Modifier = Modifier,
    brush: Brush,
    textHeight: Dp,
    fraction: Float = Random.nextFloat().coerceIn(
        minimumValue = 0.3f,
        maximumValue = 9.0f
    )
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(12.dp))

        Spacer(
            modifier = modifier
                .size(MaterialTheme.sizing.avatarList)
                .fillMaxWidth()
                .clip(CircleShape)
                .background(brush = brush)
        )
        HorizontalSpacerSmall()
        Box(modifier = modifier
            .fillMaxWidth()
            .weight(1f)){
            Spacer(
                modifier = modifier
                    .fillMaxWidth(fraction = fraction)
                    .height(textHeight)
                    .clip(RoundedCornerShape(12.dp))
                    .background(brush = brush)
            )
        }

        Spacer(
            modifier = modifier
                .size(MaterialTheme.sizing.spacerMedium)
                .clip(CircleShape)
                .background(brush = brush)
        )
        HorizontalSpacerSmall()
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    NewMessageShimmerItem(
        brush = Brush.linearGradient(
            listOf(
                Color.LightGray.copy(alpha = 0.5f),
                Color.LightGray.copy(alpha = 0.3f),
                Color.LightGray.copy(alpha = 0.5f),
            )
        ),
        textHeight = MaterialTheme.sizing.spacerRegular
    )
}