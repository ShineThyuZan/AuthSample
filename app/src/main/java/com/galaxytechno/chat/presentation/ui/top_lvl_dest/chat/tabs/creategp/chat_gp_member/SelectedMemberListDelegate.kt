package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.tabs.creategp.chat_gp_member

import com.galaxytechno.chat.model.vos.FriListVos

interface SelectedMemberListDelegate {
    fun onRemoveMemberList(data: FriListVos)
}