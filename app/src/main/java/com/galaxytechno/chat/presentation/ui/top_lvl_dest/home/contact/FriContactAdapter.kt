package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.contact

import android.content.Context
import android.view.ViewGroup
import com.galaxytechno.chat.R
import com.galaxytechno.chat.model.dto.ContactFriObj
import com.galaxytechno.chat.presentation.base.BaseRecyclerAdapter
import com.galaxytechno.chat.presentation.base.BaseViewHolder

class FriContactAdapter(context: Context, private val delegate: FriContactDelegate) :
    BaseRecyclerAdapter<FriContactViewHolder, ContactFriObj>(context) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ContactFriObj> {
        val view = mLayoutInflator.inflate(R.layout.contact_fri_item_view_pod, parent, false)
        return FriContactViewHolder(view, delegate)
    }
}