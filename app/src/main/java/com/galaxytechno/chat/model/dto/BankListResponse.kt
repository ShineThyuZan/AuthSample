package com.galaxytechno.chat.model.dto

class BankListResponse(
    var success: Boolean,
    val message: String,
    var data: List<DataObj>,
    var paging: String,
    var error: String
)

data class DataObj(
    var id: String,
    var name: String,
    var photo: String

)
