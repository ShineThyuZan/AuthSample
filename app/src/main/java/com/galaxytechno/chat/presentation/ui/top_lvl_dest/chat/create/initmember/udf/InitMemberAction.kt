package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.initmember.udf

import com.galaxytechno.chat.model.vos.FriListVos


sealed class InitMemberAction {
    object ClickSearchTextCancel : InitMemberAction()
    data class SearchTextChanged(val searchQuery: String) : InitMemberAction()
    data class ClickSummit(val summitText: String) : InitMemberAction()

    object ClickBack : InitMemberAction()
    object ClickNext : InitMemberAction()
    data class CheckItem(val friend: FriListVos) : InitMemberAction()

    object ShowSnack : InitMemberAction()
}