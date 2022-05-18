package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.initmember.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.galaxytechno.chat.R
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component.AvatarText
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component.AvatarUrl
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.*

@Composable
fun SelectedItem(
    modifier: Modifier = Modifier,
    name: String,
    avatar: String,
    onDeleteItem: () -> Unit,
) {
    Column(
        modifier = modifier
            .width(MaterialTheme.sizing.topBarHeight),
        verticalArrangement = Arrangement.Center
    ) {
        Box(modifier = modifier.size(MaterialTheme.sizing.topBarHeight)) {

            Box(
                modifier = modifier
                    .size(MaterialTheme.sizing.topBarHeight),
                contentAlignment = Alignment.Center
            ) {
                if (avatar.isNotEmpty()) {
                    AvatarUrl(url = avatar, size = MaterialTheme.sizing.textFieldHeight)

                } else {
                    AvatarText(
                        name = if (name.isEmpty()) ' ' else name.first(),
                        size = MaterialTheme.sizing.textFieldHeight,
                        textSize = MaterialTheme.typography.titleSmall
                    )
                }
            }

            Box(
                modifier = modifier
                    .size(MaterialTheme.sizing.iconButtonSize)
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .clickable { onDeleteItem() }
                    .background(color = Red100)

            ) {
                Icon(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.tiny),
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = "delete item",
                    tint = Red900
                )
            }

        }
        Text(
            modifier = modifier.fillMaxWidth(),
            text = name,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f),
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}