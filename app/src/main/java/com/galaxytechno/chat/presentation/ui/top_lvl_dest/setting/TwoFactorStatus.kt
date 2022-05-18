package com.galaxytechno.chat.presentation.ui.top_lvl_dest.setting

enum class TwoFactorStatus(
    val status: Int
) {
    Loading(0),
    Success(1),
    Fail(-1)
}
