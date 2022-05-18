package com.galaxytechno.chat.model.dto

import com.galaxytechno.chat.model.response.BaseResponse

class ContactProfileInfoResponse (
        override var status: String,
        override var messageCode: Int,
    ) : BaseResponse<ProfileInfo>()