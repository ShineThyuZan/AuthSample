package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.newmessage.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.galaxytechno.chat.R
import com.galaxytechno.chat.model.vos.FriListVos
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component.HorizontalSpacerSmall
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.initmember.components.shimmer.FriendsUiModel
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.spacing

@Composable
fun NewMessageDataContent(
    modifier: Modifier = Modifier,
    onCreateNewClicked: () -> Unit,
    friends: LazyPagingItems<FriendsUiModel>,
    onItemClicked: (FriListVos) -> Unit,
) {
    Surface {
        Column(
            modifier = modifier.fillMaxSize()
        ) {

            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(
                        top = MaterialTheme.spacing.small,
                        bottom = MaterialTheme.spacing.small,

                        ), contentAlignment = Alignment.Center
            ) {
                OutlinedButton(
                    onClick = onCreateNewClicked,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colors.primary,
                    ),
                    shape = CircleShape,
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colors.primary
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_group),
                        contentDescription = "create new group"
                    )
                    HorizontalSpacerSmall()
                    Text(text = stringResource(id = R.string.create_new_group))
                }
            }
            NewMessageListView(
                friends = friends,
                onItemClicked = onItemClicked
            )

        }
    }
}