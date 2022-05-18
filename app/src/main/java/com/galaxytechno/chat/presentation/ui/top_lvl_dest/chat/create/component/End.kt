package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.sizing
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.spacing

@Composable
fun EndOfPagination(modifier: Modifier = Modifier) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.regular),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Box(
            modifier =
            modifier
                .size(MaterialTheme.sizing.spacerSmall)
                .clip(CircleShape)
                .background(color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f))
        )
        HorizontalSpacerMedium()

        Box(
            modifier =
            modifier
                .size(MaterialTheme.sizing.spacerSmall)
                .clip(CircleShape)
                .background(color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f))
        )
        HorizontalSpacerMedium()
        Box(
            modifier =
            modifier
                .size(MaterialTheme.sizing.spacerSmall)
                .clip(CircleShape)
                .background(color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f))
        )

    }
}

@Composable
@Preview
private fun Preview() {
    Surface {
        EndOfPagination()
    }
}