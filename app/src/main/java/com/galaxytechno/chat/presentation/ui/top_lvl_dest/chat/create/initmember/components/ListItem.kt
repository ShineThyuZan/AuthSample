package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.initmember.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component.AvatarText
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component.AvatarUrl
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component.HorizontalSpacerSmall
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.bodyMedium
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.sizing
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.spacing
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.titleSmall

@Composable
fun InitMemberListItem(
    modifier: Modifier = Modifier,
    name: String,
    avatar: String,
    isChecked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
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
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChanged,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colors.primary
            ),
        )
    }
}

@Composable
@Preview
private fun NotCheckPreview() {
    Surface {
        InitMemberListItem(
            name = "Kyaw Linn Thant",
            avatar = "",
            onCheckedChanged = {},
            isChecked = false
        )
    }
}

@Composable
@Preview
private fun CheckedPreview() {
    Surface {
        InitMemberListItem(
            name = "Kyaw Linn Thant",
            avatar = "",
            onCheckedChanged = {},
            isChecked = true
        )
    }
}