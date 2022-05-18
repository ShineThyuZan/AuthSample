package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.newmessage

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.collectAsLazyPagingItems
import com.galaxytechno.chat.R
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.CreateRoomViewModel
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.newmessage.components.NewMessageDataContent
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.newmessage.components.NewMessageSearchContent
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.newmessage.components.NewMessageTopBar
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.newmessage.udf.NewMessageAction
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.newmessage.udf.NewMessageEvent
import kotlinx.coroutines.flow.collect

@Composable
fun NewMessageScreen(
    vm: CreateRoomViewModel,
    goToInitMember: () -> Unit,
    goBack: () -> Unit,
) {
    NewMessageView(
        vm = vm,
        goToInitMember = goToInitMember,
        goBack = goBack
    )
}

@Composable
fun NewMessageView(
    vm: CreateRoomViewModel,
    goToInitMember: () -> Unit,
    goBack: () -> Unit,
) {

    val scaffoldState = rememberScaffoldState()
    val state = vm.state.value
    val newMessageState = state.newMessageState
    val searchFriends = newMessageState.searchFriends.collectAsLazyPagingItems()
    val friends = state.friends.collectAsLazyPagingItems()


    LaunchedEffect(key1 = true) {
        vm.newMessageEvent.collect {
            when (it) {
                NewMessageEvent.NavigateToAddMember -> {
                    goToInitMember()

                }
                is NewMessageEvent.NavigateToConversation -> {
                    //todo go to chat room
                }
                NewMessageEvent.Popup -> {
                    goBack()
                }
                is NewMessageEvent.SummitSearch -> {
                    //todo this will be with pagination
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            NewMessageTopBar(
                title = stringResource(id = R.string.new_message),
                navIcon = painterResource(id = R.drawable.ic_close),
                onNavIconClicked = {
                    vm.onActionNewMessage(
                        NewMessageAction.ClickBack
                    )
                },
                searchQuery = newMessageState.searchQuery,
                onTextChanged = {
                    vm.onActionNewMessage(
                        NewMessageAction.SearchTextChanged(
                            searchQuery = it
                        )
                    )
                },
                onClearIconClicked = {
                    vm.onActionNewMessage(
                        NewMessageAction.SearchTextChanged(
                            searchQuery = ""
                        )
                    )
                },
                onSearchCommitted = {

                }
            )

        },
        content = {
            if (newMessageState.searchQuery.isNotEmpty()) {
                NewMessageSearchContent(
                    searchFriends = searchFriends,
                    onItemClicked = {
                        vm.onActionNewMessage(NewMessageAction.ClickItem(friend = it))
                    }
                )
            } else {
                NewMessageDataContent(
                    onCreateNewClicked = {
                        goToInitMember()
                    },
                    friends = friends,
                    onItemClicked = {
                        vm.onActionNewMessage(NewMessageAction.ClickItem(friend = it))
                    }
                )
            }
        }
    )
}