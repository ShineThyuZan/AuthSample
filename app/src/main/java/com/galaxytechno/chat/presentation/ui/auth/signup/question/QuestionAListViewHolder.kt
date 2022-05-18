package com.galaxytechno.chat.presentation.ui.auth.signup.question

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.galaxytechno.chat.R
import com.galaxytechno.chat.model.dto.SecurityQuestObj
import com.galaxytechno.chat.presentation.base.BaseViewHolder

class QuestionAListViewHolder(
    itemView: View,
    private val delegate: QuestAListDelegate
) :
    BaseViewHolder<SecurityQuestObj>(itemView) {
    var chatRecentMsg: TextView? = null
    var imageViewRemove: ImageButton? = null

    override fun setData(data: SecurityQuestObj) {
        mData = data
        chatRecentMsg = itemView.findViewById<View>(R.id.tv_security_question) as TextView
        chatRecentMsg!!.text = mData.question


    }

    override fun onClick(v: View?) {
        delegate.onQuestAClicked(mData)

    }


}