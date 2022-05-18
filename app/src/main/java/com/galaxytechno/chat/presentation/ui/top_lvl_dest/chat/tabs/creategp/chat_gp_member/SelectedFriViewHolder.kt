package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.tabs.creategp.chat_gp_member

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.galaxytechno.chat.R
import com.galaxytechno.chat.model.vos.FriListVos
import com.galaxytechno.chat.presentation.base.BaseViewHolder
import com.google.android.material.imageview.ShapeableImageView

class SelectedFriViewHolder(
    itemView: View,
) :
    BaseViewHolder<FriListVos>(itemView) {
    var userName: TextView? = null
    var profilePhoto: ShapeableImageView? = null
    var deleteUser: ImageView? = null

    override fun setData(data: FriListVos) {
        mData = data
        userName = itemView.findViewById<View>(R.id.tv_selected_user_name) as TextView
        userName!!.text = mData.friendName
        profilePhoto = itemView.findViewById<View>(R.id.iv_selected_user) as ShapeableImageView
        deleteUser = itemView.findViewById<View>(R.id.iv_delete_from_selected) as ImageView
        Glide.with(itemView.context)
            .load(mData.headUrl)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(profilePhoto!!)


    }

    override fun onClick(v: View?) {

    }
}