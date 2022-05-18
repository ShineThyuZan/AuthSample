package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.newmessage.components.shimmer

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.sizing
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.spacing

@Composable
fun NewMessageShimmerList(
    modifier: Modifier = Modifier
) {

    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.5f),
        Color.LightGray.copy(alpha = 0.3f),
        Color.LightGray.copy(alpha = 0.5f),
    )

    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.extraSmall),
    ) {

        FriendsHeaderItem(brush = brush)

        ContactsShimmerItem(
            brush = brush,
            textHeight = MaterialTheme.sizing.spacerRegular,
            fraction = 0.3f
        )
        ContactsShimmerItem(
            brush = brush,
            textHeight = MaterialTheme.sizing.spacerRegular,
            fraction = 0.5f
        )
        ContactsShimmerItem(
            brush = brush,
            textHeight = MaterialTheme.sizing.spacerRegular,
            fraction = 0.4f
        )

        FriendsHeaderItem(brush = brush)

        ContactsShimmerItem(
            brush = brush,
            textHeight = MaterialTheme.sizing.spacerRegular,
            fraction = 0.6f
        )
        ContactsShimmerItem(
            brush = brush,
            textHeight = MaterialTheme.sizing.spacerRegular,
            fraction = 0.5f
        )

        FriendsHeaderItem(brush = brush)
        ContactsShimmerItem(
            brush = brush,
            textHeight = MaterialTheme.sizing.spacerRegular,
            fraction = 0.8f
        )
        ContactsShimmerItem(
            brush = brush,
            textHeight = MaterialTheme.sizing.spacerRegular,
            fraction = 0.4f
        )
        ContactsShimmerItem(
            brush = brush,
            textHeight = MaterialTheme.sizing.spacerRegular,
            fraction = 0.3f
        )
        ContactsShimmerItem(
            brush = brush,
            textHeight = MaterialTheme.sizing.spacerRegular,
            fraction = 0.5f
        )
        ContactsShimmerItem(
            brush = brush,
            textHeight = MaterialTheme.sizing.spacerRegular,
            fraction = 0.6f
        )
    }
}

@Composable
fun FriendsHeaderItem(
    modifier: Modifier = Modifier,
    brush: Brush
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(MaterialTheme.spacing.regular)
            .background(brush = brush),
    )
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    NewMessageShimmerList()
}