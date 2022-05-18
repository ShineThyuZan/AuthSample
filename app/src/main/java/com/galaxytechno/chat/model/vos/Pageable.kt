package com.galaxytechno.chat.model.vos
data class Pageable(
    val prev: Int?,
    val next: Int?,
    val totalElements: Int,
    val numberOfElements: Int
)
