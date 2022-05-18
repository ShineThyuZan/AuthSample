package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.creategroup.components

import android.graphics.Bitmap
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.galaxytechno.chat.R
import com.galaxytechno.chat.model.vos.FriListVos
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component.*
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.labelLarge
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.sizing
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.spacing

@Composable
fun CreateGroupContent(
    modifier: Modifier = Modifier,
    groupPhoto: Bitmap?,
    onEditClicked: () -> Unit,
    groupNamePlaceholder: String,
    groupNameValue: String,
    groupNameValueChanged: (String) -> Unit,
    groupNameValueCleared: () -> Unit,
    isEmptyGroupName: Boolean,
    groupNameErrorMessage: String,
    totalMember: Int,
    memberLimit: Int,
    selectedFriends: List<FriListVos>,
    onItemDeleted: (FriListVos) -> Unit,
) {
    Surface {
        Column(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            VerticalSpacerSmall()
            ProfileEditAvatarText(
                avatarBitmap = groupPhoto,
                size = MaterialTheme.sizing.avatarDetail,
                onEditClicked = onEditClicked
            )
            VerticalSpacerSmall()
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(
                        start = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.medium,
                    )
            ) {

                GroupNameTextField(
                    placeholder = groupNamePlaceholder,
                    value = groupNameValue,
                    onValueChanged = groupNameValueChanged,
                    onValueCleared = groupNameValueCleared,
                    isError = isEmptyGroupName,
                    errorMessage = groupNameErrorMessage,
                    groupNameCountInput = groupNameValue.length,
                )
            }

            VerticalSpacerMedium()
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = MaterialTheme.spacing.medium),
                text = stringResource(id = R.string.chat_group_member, totalMember, memberLimit),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
            )
            VerticalSpacerTiny()

            if (selectedFriends.isNotEmpty()) {
                LazyColumn(
                    modifier = modifier
                        .animateContentSize()
                        .fillMaxSize()
                ) {

                    items(selectedFriends) { vo ->
                        CreateGroupDeletableMemberItem(
                            name = vo.friendName!!,
                            avatar = vo.headUrl ?: "",
                            onDeleteItem = {
                                onItemDeleted(vo)
                            }
                        )
                    }
                }

            } else {

                val painter =
                    if (isSystemInDarkTheme()) painterResource(id = R.drawable.ic_friend_empty_dark) else painterResource(
                        id = R.drawable.ic_friend_empty
                    )
                EmptyView(
                    painter = painter,
                    title = stringResource(id = R.string.create_group_empty_title),
                    description = stringResource(id = R.string.create_group_empty_description),
                )
            }

        }
    }
}



