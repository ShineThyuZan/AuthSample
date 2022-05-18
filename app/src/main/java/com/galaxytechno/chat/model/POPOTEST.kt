package com.galaxytechno.chat.model


class CountryListResponse(
    val status: Boolean,
    val messageCode: Int,
    val message: String? = null,
    val data: CountryListObj,
    val error: String? = null
)

data class CountryListObj(
    val countryList: List<CountryVos>
)
class CountryVos(
    val id: Int = -1,
    val countryCode: String = "",
    val country: String = "",
    val countryFlagUrl: String = "",
    val countryNick: String = "",
    val countryPrefix: String = "",
    val isDelete: Boolean = false
)
