package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.newmessage.components.shimmer

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component.HorizontalSpacerMedium
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component.HorizontalSpacerSmall
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component.HorizontalSpacerTiny
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component.VerticalSpacerSmall
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.sizing
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.spacing
import kotlin.random.Random

@Composable
fun ContactsLoadingShimmer(
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
        ContactsShimmerItem(
            brush = brush,
            textHeight = MaterialTheme.sizing.spacerMedium,
            fraction = 0.5f
        )
        ContactsShimmerItem(
            brush = brush,
            textHeight = MaterialTheme.sizing.spacerMedium,
            fraction = 0.6f
        )
        ContactsShimmerItem(
            brush = brush,
            textHeight = MaterialTheme.sizing.spacerMedium,
            fraction = 0.4f
        )
        ContactsShimmerItem(
            brush = brush,
            textHeight = MaterialTheme.sizing.spacerMedium,
            fraction = 0.8f
        )
        ContactsShimmerItem(
            brush = brush,
            textHeight = MaterialTheme.sizing.spacerMedium,
            fraction = 0.5f
        )
        ContactsShimmerItem(
            brush = brush,
            textHeight = MaterialTheme.sizing.spacerMedium,
            fraction = 0.9f
        )
        ContactsShimmerItem(
            brush = brush,
            textHeight = MaterialTheme.sizing.spacerMedium,
            fraction = 0.4f
        )
        ContactsShimmerItem(
            brush = brush,
            textHeight = MaterialTheme.sizing.spacerMedium,
            fraction = 0.5f
        )
        ContactsShimmerItem(
            brush = brush,
            textHeight = MaterialTheme.sizing.spacerMedium,
            fraction = 0.4f
        )
        ContactsShimmerItem(
            brush = brush,
            textHeight = MaterialTheme.sizing.spacerMedium,
            fraction = 0.5f
        )
    }
}


@Composable
fun ContactsShimmerItem(
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
            .padding(MaterialTheme.spacing.small)
            .clip(RoundedCornerShape(MaterialTheme.spacing.extraSmall)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalSpacerMedium()
        Spacer(
            modifier = modifier
                .size(MaterialTheme.sizing.avatarList)
                .fillMaxWidth()
                .clip(CircleShape)
                .background(brush = brush)
        )
        HorizontalSpacerTiny()
        Column(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(MaterialTheme.spacing.small),
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(
                modifier = modifier
                    .fillMaxWidth(fraction = fraction)
                    .height(textHeight)
                    .clip(CircleShape)
                    .background(brush = brush)
            )
            VerticalSpacerSmall()
            Spacer(
                modifier = modifier
                    .fillMaxWidth(fraction = 0.4f)
                    .height(MaterialTheme.sizing.spacerSmall)
                    .clip(CircleShape)
                    .background(brush = brush)
            )
        }


        HorizontalSpacerSmall()
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
fun ContactsShimmerPreview() {
    ContactsShimmerItem(
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

@Composable
@Preview(showBackground = true)
fun ContactsLoadingPreview() {
    ContactsLoadingShimmer()
}