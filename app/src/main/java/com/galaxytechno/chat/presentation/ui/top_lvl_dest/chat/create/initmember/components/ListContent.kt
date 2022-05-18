package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.initmember.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.galaxytechno.chat.R
import com.galaxytechno.chat.model.vos.FriListVos
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component.EmptyView
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component.EndOfPagination
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component.ErrorItem
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component.LoadingItem
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.initmember.components.shimmer.FriendsUiModel
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.initmember.components.shimmer.InitMemberShimmerView
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.bodyMedium
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.spacing

@Composable
fun InitMemberSearchView(
    modifier: Modifier = Modifier,
    searchFriends: LazyPagingItems<FriendsUiModel>,
    onCheckedChanged: (Boolean, FriListVos) -> Unit,
    selectedFriends: List<FriListVos>,
) {
    Surface {

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.extraSmall)
        ) {

            items(
                items = searchFriends,
            ) { uiModel ->
                when (uiModel) {
                    is FriendsUiModel.Item -> {
                        val isAdySelected = selectedFriends.contains(uiModel.item)

                        InitMemberListItem(
                            name = uiModel.item.friendName!!,
                            avatar = uiModel.item.headUrl ?: "",
                            isChecked = isAdySelected,
                            onCheckedChanged = {
                                onCheckedChanged(it, uiModel.item)
                            }
                        )
                    }

                    is FriendsUiModel.Header -> {

                        if (uiModel.header != "null") {
                            Box(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .background(color = MaterialTheme.colors.background)
                            ) {
                                Text(
                                    text = uiModel.header,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(
                                        start = MaterialTheme.spacing.medium,
                                        top = MaterialTheme.spacing.extraSmall,
                                        bottom = MaterialTheme.spacing.extraSmall,
                                    )
                                )
                            }
                        }
                    }
                    else -> {
                        val painter =
                            if (isSystemInDarkTheme()) painterResource(id = R.drawable.ic_friend_empty_dark) else painterResource(
                                id = R.drawable.ic_friend_empty
                            )
                        EmptyView(
                            painter = painter,
                            title = stringResource(id = R.string.friends_empty_title),
                            description = stringResource(id = R.string.friends_empty_description),
                        )
                    }
                }
            }

            searchFriends.apply {

                when {
                    loadState.refresh is LoadState.Loading -> {
                        item {
                            InitMemberShimmerView()
                        }
                    }
                    loadState.append is LoadState.Loading -> {
                        item {
                            LoadingItem()
                        }
                    }
                    loadState.refresh is LoadState.Error -> {
                        val e = searchFriends.loadState.refresh as LoadState.Error
                        item {
                            ErrorItem(
                                message = e.error.localizedMessage!!,
                                modifier = Modifier.fillParentMaxSize(),
                                onClickRetry = { retry() }
                            )
                        }
                    }
                    loadState.append is LoadState.Error -> {
                        val e = searchFriends.loadState.append as LoadState.Error
                        item {
                            ErrorItem(
                                message = e.error.localizedMessage!!,
                                onClickRetry = { retry() }
                            )
                        }
                    }
                    loadState.append.endOfPaginationReached -> {
                        if (searchFriends.itemCount == 0) {
                            item {
                                val painter =
                                    if (isSystemInDarkTheme()) painterResource(id = R.drawable.ic_friend_empty_dark) else painterResource(
                                        id = R.drawable.ic_friend_empty
                                    )
                                EmptyView(
                                    painter = painter,
                                    title = stringResource(id = R.string.friends_empty_title),
                                    description = stringResource(id = R.string.friends_empty_description),
                                )
                            }
                        } else {
                            item {

                                EndOfPagination()
                            }
                        }
                    }
                }
            }
        }
    }
}