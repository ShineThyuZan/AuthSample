package com.galaxytechno.chat.presentation.ui.other_lvl_dest.language_select

import android.view.View
import android.widget.TextView
import com.galaxytechno.chat.R
import com.galaxytechno.chat.model.dto.LanguageVos
import com.galaxytechno.chat.model.dto.LanguagesResponse
import com.galaxytechno.chat.presentation.base.BaseViewHolder
import timber.log.Timber

class InitLangSelectViewHolder(
    itemView: View,
    private val delegate: InitLangSelectDelegate
) :
    BaseViewHolder<LanguageVos>(itemView) {
    var languageNameTextView: TextView? = null
    var languageDescriptionTextView: TextView? = null

    override fun setData(data: LanguageVos) {
        mData = data
        Timber.tag("RecyclerData").d("Data$mData")
        languageNameTextView = itemView.findViewById<View>(R.id.tv_name) as TextView
        languageNameTextView!!.text = mData.name
        languageDescriptionTextView =
            itemView.findViewById<View>(R.id.tv_description) as TextView
        languageDescriptionTextView!!.text = mData.description
    }

    override fun onClick(v: View?) {
        delegate.onClickSelectedLanguage(mData)
    }
}