package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.tabs.creategp.chat_gp_member

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import com.galaxytechno.chat.R
import com.galaxytechno.chat.model.vos.FriListVos
import com.galaxytechno.chat.presentation.base.BaseRecyclerAdapter
import com.galaxytechno.chat.presentation.base.BaseViewHolder
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.feeds.FeedUserActivityDelegate

class SelectedMemberListAdapter(
    context: Context,
    private val delegate: SelectedMemberListDelegate
) :
    BaseRecyclerAdapter<SelectedMemberListViewHolder, FriListVos>(context) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<FriListVos> {
        val view = mLayoutInflator.inflate(R.layout.layout_selected_member_list_view_pod, parent, false)
        return SelectedMemberListViewHolder(view, delegate)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<FriListVos>, position: Int) {
        if (holder is SelectedMemberListViewHolder) {
            super.onBindViewHolder(holder, position)
         holder.removeMember!!.setOnClickListener {
                delegate.onRemoveMemberList(mData!![position])
            }
        }
    }
}