package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.labelSmall
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.spacing

@Composable
fun VisibilityAnimator(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    errorMessage: String,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(500)) +
                expandVertically(animationSpec = tween(500)),
        exit = fadeOut(animationSpec = tween(500)) +
                shrinkVertically(animationSpec = tween(500))
    ) {
        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    start = MaterialTheme.spacing.medium,
                    end = MaterialTheme.spacing.medium
                ),
            text = errorMessage,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colors.error
        )
    }
}

@Composable
fun VisibilityAnimatorOtp(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    errorMessage: String,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(500)) +
                expandVertically(animationSpec = tween(500)),
        exit = fadeOut(animationSpec = tween(500)) +
                shrinkVertically(animationSpec = tween(500))
    ) {
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = modifier
                    .padding(
                        start = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.medium
                    ),
                text = errorMessage,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colors.error
            )
        }
    }
}

@Composable
fun VisibilityAnimatorPhone(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    errorMessage: String,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(500)) +
                expandVertically(animationSpec = tween(500)),
        exit = fadeOut(animationSpec = tween(500)) +
                shrinkVertically(animationSpec = tween(500))
    ) {
        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    start = MaterialTheme.spacing.default,
                    end = MaterialTheme.spacing.medium
                ),
            text = errorMessage,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colors.error
        )
    }
}

@Composable
fun VisibilityAnimatorRow(
    modifier: Modifier = Modifier,
    isErrorRegion: Boolean,
    isErrorPhone: Boolean,
    errorMessageRegion: String,
    errorMessagePhone: String,
) {

    Row(modifier = modifier.fillMaxWidth()) {

        VisibilityAnimator(
            modifier = modifier.fillMaxWidth(0.35f),
            isVisible = isErrorRegion || isErrorPhone,
            errorMessage = if (isErrorRegion) errorMessageRegion else ""
        )
        HorizontalSpacerSmall()
        VisibilityAnimatorPhone(
            isVisible = isErrorPhone || isErrorRegion,
            errorMessage = if (isErrorPhone) errorMessagePhone else ""
        )
    }
}
