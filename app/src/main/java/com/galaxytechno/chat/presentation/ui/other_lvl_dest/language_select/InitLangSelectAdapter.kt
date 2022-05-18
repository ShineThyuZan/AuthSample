package com.galaxytechno.chat.presentation.ui.other_lvl_dest.language_select

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.galaxytechno.chat.R
import com.galaxytechno.chat.model.dto.LanguageVos

import com.galaxytechno.chat.model.dto.LanguagesResponse
import com.galaxytechno.chat.presentation.base.BaseRecyclerAdapter
import com.galaxytechno.chat.presentation.base.BaseViewHolder

class InitLangSelectAdapter(context: Context, private val delegate: InitLangSelectDelegate) :
BaseRecyclerAdapter<InitLangSelectViewHolder, LanguageVos>(context){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<LanguageVos> {
        val view = mLayoutInflator.inflate(R.layout.language_select_view_pod, parent, false)
        return InitLangSelectViewHolder(view, delegate)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(
        holder: BaseViewHolder<LanguageVos>,
        position: Int
    ) {
        super.onBindViewHolder(holder, position)
        val size = mData?.size
        if (size != null) {
            if(position == size-1) {
                var divider : View? = null
                divider = holder.itemView.findViewById(R.id.div_language) as View
                divider.visibility = View.GONE
            }
        }
    }
}