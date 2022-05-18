package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.tabs.all

import android.content.Context
import android.view.ViewGroup
import com.galaxytechno.chat.R
import com.galaxytechno.chat.model.dto.Country
import com.galaxytechno.chat.model.dto.UserVos
import com.galaxytechno.chat.model.vos.FriListVos
import com.galaxytechno.chat.presentation.base.BaseRecyclerAdapter
import com.galaxytechno.chat.presentation.base.BaseViewHolder

class ChatRecentMsgAdapter (context: Context, private val delegate: ChatRecentMsgDelegate) :
    BaseRecyclerAdapter<ChatRecentMsgViewHolder, UserVos>(context) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<UserVos> {
        val view = mLayoutInflator.inflate(R.layout.chat_recent_msg, parent, false)
        return ChatRecentMsgViewHolder(view, delegate)
    }
}