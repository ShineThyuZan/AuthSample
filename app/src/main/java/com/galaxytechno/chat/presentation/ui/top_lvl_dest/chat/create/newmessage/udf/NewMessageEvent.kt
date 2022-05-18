package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.newmessage.udf

import com.galaxytechno.chat.model.vos.FriListVos

sealed class NewMessageEvent {
    object Popup : NewMessageEvent()
    object NavigateToAddMember : NewMessageEvent()
    data class NavigateToConversation(val friendVo: FriListVos) : NewMessageEvent()
    data class SummitSearch(val query: String) : NewMessageEvent()
}
