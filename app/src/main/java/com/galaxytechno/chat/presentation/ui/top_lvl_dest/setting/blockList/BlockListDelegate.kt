package com.galaxytechno.chat.presentation.ui.top_lvl_dest.setting.blockList

import com.galaxytechno.chat.model.vos.FriListVos

interface BlockListDelegate {
    fun onClickedName(data: FriListVos)
    fun onCLickUnblock(data: FriListVos)
    fun loadMore()
}