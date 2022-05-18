package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.initmember.udf

import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.creategroup.udf.CreateGroupEvent

sealed class InitMemberEvent{
    object ShowSnack : InitMemberEvent()
    object Popup : InitMemberEvent()
    object NavigateToCreateGroup : InitMemberEvent()
    data class SummitSearch(val query : String) : InitMemberEvent()
    data class ShowSnackBar(val message: String) : InitMemberEvent()
}
