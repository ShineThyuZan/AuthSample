package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.tabs.all

import com.galaxytechno.chat.model.dto.UserVos
import com.galaxytechno.chat.model.vos.FriListVos

interface ChatRecentMsgDelegate {
    fun onClickedRecentMsg(data: UserVos)
}