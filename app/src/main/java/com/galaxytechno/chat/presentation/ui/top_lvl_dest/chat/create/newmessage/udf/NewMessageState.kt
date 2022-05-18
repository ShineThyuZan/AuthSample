package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.newmessage.udf

import androidx.paging.PagingData
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.initmember.components.shimmer.FriendsUiModel
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.newmessage.components.shimmer.SearchWidgetState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class NewMessageState(
    val searchFriends: Flow<PagingData<FriendsUiModel>> = emptyFlow(),
    val isLoading: Boolean = false,
    val searchWidgetState: SearchWidgetState = SearchWidgetState.CLOSED,
    val searchQuery: String = "",
)
