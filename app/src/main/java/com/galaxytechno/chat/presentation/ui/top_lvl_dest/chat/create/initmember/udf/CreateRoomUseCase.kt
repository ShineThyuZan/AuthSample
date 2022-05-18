package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.initmember.udf

import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.GetAllFriendsForCreateRoomUseCase
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.SearchAppUsersForCreateRoomUseCase
import javax.inject.Inject

data class CreateRoomUseCase @Inject constructor(
    val getAllFriendsForCreateRoom: GetAllFriendsForCreateRoomUseCase,
    val searchAppUsersForCreateRoom : SearchAppUsersForCreateRoomUseCase,
)
