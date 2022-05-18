package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.pagerFragment

import com.galaxytechno.chat.model.dto.UserVos
import com.galaxytechno.chat.model.vos.FriListVos

interface UserListDelegate {
    fun onClickedFriList(data : UserVos)
    fun loadMore()
}