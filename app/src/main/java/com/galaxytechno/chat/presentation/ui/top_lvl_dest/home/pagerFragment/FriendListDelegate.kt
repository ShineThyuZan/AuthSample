package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.pagerFragment

import com.galaxytechno.chat.model.vos.FriListVos

interface FriendListDelegate {
    fun onClickedFriList(data : FriListVos)
    fun loadMore()
}