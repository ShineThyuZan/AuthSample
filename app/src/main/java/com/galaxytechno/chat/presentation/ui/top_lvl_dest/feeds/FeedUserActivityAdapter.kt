package com.galaxytechno.chat.presentation.ui.top_lvl_dest.feeds

import android.content.Context
import android.view.ViewGroup
import com.galaxytechno.chat.R
import com.galaxytechno.chat.model.dto.DataObj
import com.galaxytechno.chat.presentation.base.BaseRecyclerAdapter
import com.galaxytechno.chat.presentation.base.BaseViewHolder

class FeedUserActivityAdapter (context: Context, private val delegate: FeedUserActivityDelegate) :
    BaseRecyclerAdapter<FeedUserActivityViewHolder, DataObj>(context) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<DataObj> {
        val view = mLayoutInflator.inflate(R.layout.feed_user_activity_view_pod, parent, false)
        return FeedUserActivityViewHolder(view, delegate)
    }
}