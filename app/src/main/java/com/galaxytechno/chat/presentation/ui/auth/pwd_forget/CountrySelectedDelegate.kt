package com.galaxytechno.chat.presentation.ui.auth.pwd_forget

import com.galaxytechno.chat.model.dto.Country

interface CountrySelectedDelegate {
    fun onClickSelectedCountry(data : Country)
}