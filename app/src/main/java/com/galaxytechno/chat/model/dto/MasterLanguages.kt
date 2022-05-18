package com.galaxytechno.chat.model.dto

import com.galaxytechno.chat.model.response.BaseResponse

data class LanguagesResponse(
    override val status: String,
    override val messageCode: Int,
) : BaseResponse<LanguageListObj>()


data class LanguageListObj(
    val languageList: List<LanguageVos>
)

data class LanguageVos(
    val iso: String = "",
    val name: String = "",
    val description: String = "",
    val locale: Int = -1
)











