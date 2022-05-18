package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.notification

import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.galaxytechno.chat.R
import com.galaxytechno.chat.model.vos.FriReqVos
import com.galaxytechno.chat.presentation.base.BaseViewHolder
import com.google.android.material.imageview.ShapeableImageView

class FriRequestListViewHolder(itemView: View, val delegate: FriRequestListDelegate) :
    BaseViewHolder<FriReqVos>(itemView) {
    var imageView: ShapeableImageView? = null
    var requestFriName: TextView? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setData(data: FriReqVos) {
        imageView = itemView.findViewById<View>(R.id.ivProfile) as ShapeableImageView
        requestFriName = itemView.findViewById<View>(R.id.tvName) as TextView
        mData = data
        Glide.with(itemView.context)
            .load(mData.headUrl)
            .placeholder(R.drawable.place_holder)
            .into(imageView!!)
        requestFriName!!.text = mData.name


    }

    override fun onClick(v: View?) {
        delegate.onClickedName(mData)
    }

}