package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat

import com.galaxytechno.chat.model.dto.MessageObj

interface MessageSelectedDelegate {
    fun onClickSelectedMessage(data: MessageObj)
}