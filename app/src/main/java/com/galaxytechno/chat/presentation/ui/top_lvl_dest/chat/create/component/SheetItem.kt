package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.bodyMedium
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.spacing

@Composable
fun UploadImageSheetItem(
    modifier: Modifier = Modifier,
    painter: Painter,
    text: String,
    onItemClicked: () -> Unit,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.tiny)
            .clip(MaterialTheme.shapes.large)
            .clickable(
                onClick = onItemClicked
            )
            .padding(
                start = MaterialTheme.spacing.medium,
                end = MaterialTheme.spacing.medium,
                top = 12.dp,
                bottom = 12.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painter = painter, contentDescription = "Upload Sheet Icon")
        HorizontalSpacerSmall()
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun DeleteItem(
    modifier: Modifier = Modifier,
    painter: Painter,
    text: String,
    onItemClicked: () -> Unit,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.tiny)
            .clip(MaterialTheme.shapes.large)
            .clickable(
                onClick = onItemClicked
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painter,
            contentDescription = "Upload Sheet Icon",
            tint = MaterialTheme.colors.error
        )
        HorizontalSpacerSmall()
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colors.error
        )
    }
}

