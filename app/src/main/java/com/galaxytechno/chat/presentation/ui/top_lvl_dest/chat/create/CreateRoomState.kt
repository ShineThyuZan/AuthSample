package com.galaxytechno.chat.main.presentation.screens.chat.presentation.room.create

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.paging.PagingData
import com.galaxytechno.chat.model.vos.FriListVos
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.creategroup.udf.CreateGroupState
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.initmember.components.shimmer.FriendsUiModel
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.initmember.udf.InitMemberState
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.newmessage.udf.NewMessageState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class CreateRoomState(

    val friends: Flow<PagingData<FriendsUiModel>> = emptyFlow(),
    val selectedFriends: MutableState<List<FriListVos>> = mutableStateOf(listOf()),

    val newMessageState: NewMessageState = NewMessageState(),
    val initMemberState: InitMemberState = InitMemberState(),
    val createGroupState: CreateGroupState = CreateGroupState()
)
