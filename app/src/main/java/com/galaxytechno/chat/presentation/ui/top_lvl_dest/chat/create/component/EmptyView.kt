package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.bodyMedium
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.spacing
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.titleMedium

@Composable
fun EmptyView(
    modifier: Modifier = Modifier,
    painter: Painter,
    title: String,
    description: String
) {
    Surface {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                modifier = modifier.size(160.dp),
                painter = painter,
                contentDescription = "empty image"
            )
            VerticalSpacerRegular()
            AuthHeader(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.small),
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            VerticalSpacerExtraSmall()
            AuthHeader(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.small),
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )

        }
    }
}
