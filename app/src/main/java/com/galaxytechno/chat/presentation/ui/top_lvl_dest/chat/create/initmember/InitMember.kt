package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.initmember

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.collectAsLazyPagingItems
import com.galaxytechno.chat.R
import com.galaxytechno.chat.common.Constant
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.CreateRoomViewModel
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.initmember.components.InitMemberDataView
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.initmember.components.InitMemberSearchView
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.initmember.components.InitMemberTopBar
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.initmember.udf.InitMemberAction
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.initmember.udf.InitMemberEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun InitMemberScreen(

    vm: CreateRoomViewModel,
    goToCreateGroup: () -> Unit,
    goBack: () -> Unit,
) {
    InitMemberView(
        vm = vm,
        goToCreateGroup = goToCreateGroup,
        goBack = goBack,
    )
}

@Composable
fun InitMemberView(
    vm: CreateRoomViewModel,
    goToCreateGroup: () -> Unit,
    goBack: () -> Unit,
) {

    val scaffoldState = rememberScaffoldState()
    val state = vm.state.value
    val initMemberState = state.initMemberState
    val searchFriends = initMemberState.searchFriends.collectAsLazyPagingItems()
    val friends = state.friends.collectAsLazyPagingItems()
    val selectedFriends = state.selectedFriends.value
    val limitStatus = stringResource(id = R.string.chat_group_member)


    LaunchedEffect(key1 = true) {
        vm.initMemberEvent.collectLatest {
            when (it) {
                InitMemberEvent.NavigateToCreateGroup -> {
                    goToCreateGroup()
                }
                InitMemberEvent.Popup -> {
                    goBack()
                }
                is InitMemberEvent.SummitSearch -> {

                }
                InitMemberEvent.ShowSnack -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = limitStatus
                    )
                }
            }
        }
    }


    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            InitMemberTopBar(
                title = stringResource(id = R.string.create_new_group),
                navIcon = painterResource(id = R.drawable.ic_close),
                onNavIconClicked = {
                    vm.onActionInitMember(
                        InitMemberAction.ClickBack
                    )
                },
                searchQuery = initMemberState.searchQuery,
                onTextChanged = {
                    vm.onActionInitMember(
                        InitMemberAction.SearchTextChanged(
                            searchQuery = it
                        )
                    )
                },
                onClearIconClicked = {
                    vm.onActionInitMember(
                        InitMemberAction.SearchTextChanged(
                            searchQuery = ""
                        )
                    )
                },
                onSearchCommitted = {

                },
                onMenuIconClicked = {
                    vm.onActionInitMember(
                        InitMemberAction.ClickNext
                    )
                },
                menuIcon = painterResource(id = R.drawable.ic_arrow_right_head)
            )
        },
        content = {
            if (initMemberState.searchQuery.isNotEmpty()) {

                InitMemberSearchView(
                    searchFriends = searchFriends,
                    onCheckedChanged = { isChecked, item ->
                        if (isChecked) {
                            if (selectedFriends.count() == Constant.GROUP_MEMBER_LENGTH) {
                                vm.onActionInitMember(action = InitMemberAction.ShowSnack)
                            } else {
                                vm.saveSelectedItem(
                                    item = item
                                )
                            }
                        } else {
                            vm.deleteSelectedItem(item = item)
                        }
                    },
                    selectedFriends = selectedFriends,
                )
            } else {
                InitMemberDataView(
                    friends = friends,
                    memberLimit = Constant.GROUP_MEMBER_LENGTH,
                    totalMember = state.selectedFriends.value.size,
                    onCheckedChanged = { isChecked, item ->
                        if (isChecked) {
                            if (selectedFriends.count() == Constant.GROUP_MEMBER_LENGTH) {
                                vm.onActionInitMember(action = InitMemberAction.ShowSnack)
                            } else {
                                vm.saveSelectedItem(
                                    item = item
                                )
                            }
                        } else {
                            vm.deleteSelectedItem(
                                item = item
                            )
                        }
                    },
                    selectedFriends = selectedFriends,
                    onItemDeleted = {
                        vm.deleteSelectedItem(it)
                    }
                )
            }
        }
    )
}