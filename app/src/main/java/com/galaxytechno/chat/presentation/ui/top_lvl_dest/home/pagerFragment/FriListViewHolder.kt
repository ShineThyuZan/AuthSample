package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.pagerFragment

import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.galaxytechno.chat.R
import com.galaxytechno.chat.model.dto.UserVos
import com.galaxytechno.chat.model.vos.FriListVos
import com.galaxytechno.chat.presentation.base.BaseViewHolder
import com.google.android.material.imageview.ShapeableImageView

class FriListViewHolder(itemView: View, val delegate: FriendListDelegate) :
    BaseViewHolder<FriListVos>(itemView) {
    var imageView: ShapeableImageView? = null
    var friname: TextView? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun setData(data: FriListVos) {
        imageView = itemView.findViewById<View>(R.id.ivFriList) as ShapeableImageView
        friname = itemView.findViewById<View>(R.id.tvFriName) as TextView
        mData = data
        Glide.with(itemView.context)
            .load(mData.headUrl)
            .placeholder(R.drawable.place_holder)
            .into(imageView!!)

        friname!!.text = mData.friendName

    }

    override fun onClick(v: View?) {
        delegate.onClickedFriList(mData)

    }

}