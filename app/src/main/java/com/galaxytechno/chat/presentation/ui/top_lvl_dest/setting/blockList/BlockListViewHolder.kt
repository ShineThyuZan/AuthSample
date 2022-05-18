package com.galaxytechno.chat.presentation.ui.top_lvl_dest.setting.blockList

import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.galaxytechno.chat.R
import com.galaxytechno.chat.model.vos.FriListVos
import com.galaxytechno.chat.presentation.base.BaseViewHolder
import com.google.android.material.imageview.ShapeableImageView

class BlockListViewHolder(itemView: View, val delegate: BlockListDelegate) :
    BaseViewHolder<FriListVos>(itemView) {
    var imageView: ShapeableImageView? = null
    var requestFriName: TextView? = null
    var tvUnblock: TextView? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setData(data: FriListVos) {
        imageView = itemView.findViewById<View>(R.id.ivProfile) as ShapeableImageView
        requestFriName = itemView.findViewById<View>(R.id.tvName) as TextView
        mData = data
        Glide.with(itemView.context)
            .load(mData.headUrl)
            .placeholder(R.drawable.place_holder)
            .into(imageView!!)
        requestFriName!!.text = mData.friendName
        tvUnblock = itemView.findViewById<View>(R.id.tv_unblock) as TextView
        tvUnblock!!.setOnClickListener {
            delegate.onCLickUnblock(mData)
        }
        requestFriName!!.setOnClickListener {
            delegate.onClickedName(mData)
        }
    }

    override fun onClick(v: View?) {
    }


}