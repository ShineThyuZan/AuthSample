package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.newmessage.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.galaxytechno.chat.R
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component.AvatarText
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component.AvatarUrl
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component.HorizontalSpacerSmall
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.bodyMedium
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.sizing
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.spacing
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.titleSmall

@Composable
fun NewMessageListItem(
    modifier: Modifier = Modifier,
    name: String,
    avatar: String,
    onItemClicked: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = MaterialTheme.spacing.tiny,
                bottom = MaterialTheme.spacing.tiny,
            )
            .clip(
                RoundedCornerShape(
                    MaterialTheme.spacing.medium
                )
            )
            .clickable {
                onItemClicked()
            }
            .padding(
                start = MaterialTheme.spacing.medium,
                end = MaterialTheme.spacing.medium,
                top = MaterialTheme.spacing.small,
                bottom = MaterialTheme.spacing.small,
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (avatar.isNotEmpty()) {
            AvatarUrl(url = avatar, size = MaterialTheme.sizing.avatarList)

        } else {
            AvatarText(
                name = if (name.isEmpty()) ' ' else name.first(),
                size = MaterialTheme.sizing.avatarList,
                textSize = MaterialTheme.typography.titleSmall
            )
        }

        HorizontalSpacerSmall()

        Text(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f),
            text = name,
            style = MaterialTheme.typography.bodyMedium
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right_head),
            contentDescription = "message",
            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
@Preview
private fun Preview() {
    Surface {
        NewMessageListItem(
            name = "Kyaw Linn Thant",
            avatar = "",
            onItemClicked = {}
        )
    }
}