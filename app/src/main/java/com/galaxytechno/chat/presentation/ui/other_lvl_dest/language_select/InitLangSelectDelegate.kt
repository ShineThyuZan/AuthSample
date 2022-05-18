package com.galaxytechno.chat.presentation.ui.other_lvl_dest.language_select

import com.galaxytechno.chat.model.dto.LanguageVos
import com.galaxytechno.chat.model.dto.LanguagesResponse

interface InitLangSelectDelegate {
    fun onClickSelectedLanguage(data: LanguageVos)
}