package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.tabs.creategp.chat_gp_member

import android.content.Context
import android.view.ViewGroup
import com.galaxytechno.chat.R
import com.galaxytechno.chat.model.vos.FriListVos
import com.galaxytechno.chat.presentation.base.BaseRecyclerAdapter
import com.galaxytechno.chat.presentation.base.BaseViewHolder
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.tabs.creategp.SelectedFriListViewHolder
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.pagerFragment.FriendListDelegate

class SelectedMemberAdapter(
    context: Context,
    val onDeleteItem : (FriListVos) ->Unit
    ) :
    BaseRecyclerAdapter<SelectedFriViewHolder, FriListVos>(context) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<FriListVos> {
        val view = mLayoutInflator.inflate(R.layout.selected_user_view_pod, parent, false)
        return SelectedFriViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<FriListVos>, position: Int) {
        if (holder is SelectedFriViewHolder) {
            super.onBindViewHolder(holder, position)
            holder.deleteUser?.setOnClickListener {
                onDeleteItem(mData!![position])
            }
        }
    }
}