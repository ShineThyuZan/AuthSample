package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.initmember.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import com.galaxytechno.chat.R
import com.galaxytechno.chat.main.presentation.screens.chat.presentation.room.create.initmember.components.InitMemberListView
import com.galaxytechno.chat.model.vos.FriListVos
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component.HorizontalSpacerSmall
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.initmember.components.shimmer.FriendsUiModel
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.labelLarge
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.labelMedium
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.spacing

@Composable
fun InitMemberDataView(
    modifier: Modifier = Modifier,
    friends: LazyPagingItems<FriendsUiModel>,
    selectedFriends: List<FriListVos>,
    memberLimit: Int,
    totalMember: Int,
    onCheckedChanged: (Boolean, FriListVos) -> Unit,
    onItemDeleted: (FriListVos) -> Unit,
) {
    Surface {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            Column(modifier = modifier.fillMaxWidth()) {
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(all = MaterialTheme.spacing.medium),
                    text = stringResource(
                        id = R.string.chat_group_member,
                        totalMember,
                        memberLimit
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
                )
                if (selectedFriends.isNotEmpty()) {

                    LazyRow(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = MaterialTheme.spacing.medium),
                    ) {

                        itemsIndexed(selectedFriends) { index, item ->
                            SelectedItem(
                                name = item.friendName!!,
                                avatar = item.headUrl ?: "",
                                onDeleteItem = { onItemDeleted(item) })
                            if (index <= selectedFriends.lastIndex) {
                                HorizontalSpacerSmall()
                            }
                        }
                    }
                }
                Text(
                    text = stringResource(id = R.string.friends),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = MaterialTheme.spacing.medium,
                            vertical = MaterialTheme.spacing.small
                        )
                )

                InitMemberListView(
                    friends = friends,
                    onCheckedChanged = { isChecked, item ->
                        onCheckedChanged(isChecked, item)
                    },
                    selectedFriends = selectedFriends
                )
            }
        }
    }
}