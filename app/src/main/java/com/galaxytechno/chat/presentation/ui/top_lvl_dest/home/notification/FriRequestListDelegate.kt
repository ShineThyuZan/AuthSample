package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.notification

import com.galaxytechno.chat.model.vos.FriReqVos

interface FriRequestListDelegate {
    fun onClickedName(data: FriReqVos)
    fun loadMore()
}