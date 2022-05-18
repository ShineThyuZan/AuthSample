package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.newmessage.udf

import com.galaxytechno.chat.model.vos.FriListVos

sealed class NewMessageAction {
    object ClickSearchTextCancel : NewMessageAction()
    data class SearchTextChanged(val searchQuery: String) : NewMessageAction()
    data class ClickSummit(val summitText: String) : NewMessageAction()

    object ClickBack : NewMessageAction()
    object ClickCreateButton : NewMessageAction()
    data class ClickItem(val friend: FriListVos) : NewMessageAction()

}
