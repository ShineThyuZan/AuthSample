package com.galaxytechno.chat.presentation.ui.top_lvl_dest.feeds

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.galaxytechno.chat.R
import com.galaxytechno.chat.model.dto.DataObj
import com.galaxytechno.chat.presentation.base.BaseRecyclerAdapter
import com.galaxytechno.chat.presentation.base.BaseViewHolder

class FeedAdapter(context: Context, private val delegate: FeedUserActivityDelegate) :
    BaseRecyclerAdapter<FeedViewHolder, DataObj>(context) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<DataObj> {
        val view = mLayoutInflator.inflate(R.layout.feed_view_pods, parent, false)
        return FeedViewHolder(view, delegate)
    }
}