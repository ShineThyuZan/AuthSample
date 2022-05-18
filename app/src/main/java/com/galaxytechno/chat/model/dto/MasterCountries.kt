package com.galaxytechno.chat.model.dto

import com.galaxytechno.chat.model.response.BaseResponse

data class CountriesResponse(
    override val status: String,
    override val messageCode: Int,
) : BaseResponse<CountryList>()

data class CountryList(
    val countryList: List<Country>
)

data class Country(
    val id: Int = -1,
    val countryCode: String = "",
    val country: String = "",
    val countryFlagUrl: String = "",
    val countryNick: String = "",
    val countryPrefix: String = "",
    val isDelete: Boolean = false,
)

