package com.galaxytechno.chat.presentation.ui.auth.signup.question

import android.content.Context
import android.view.ViewGroup
import com.galaxytechno.chat.R
import com.galaxytechno.chat.model.dto.SecurityQuestObj
import com.galaxytechno.chat.presentation.base.BaseRecyclerAdapter
import com.galaxytechno.chat.presentation.base.BaseViewHolder

class QuestionABottomSheetAdapter(
    context: Context,
    private val delegate: QuestAListDelegate
) :
    BaseRecyclerAdapter<QuestionAListViewHolder, SecurityQuestObj>(context) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<SecurityQuestObj> {
        val view = mLayoutInflator.inflate(R.layout.simple_spinner_item, parent, false)
        return QuestionAListViewHolder(view, delegate)
    }
}