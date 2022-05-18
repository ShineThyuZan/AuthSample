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

class SelectedMemberListViewHolder(
    itemView: View,
    private val delegate: SelectedMemberListDelegate
) :
    BaseViewHolder<FriListVos>(itemView) {
    var userName: TextView? = null
    var profilePhoto: ShapeableImageView? = null
    var removeMember: ImageView? = null

    override fun setData(data: FriListVos) {
        mData = data
        userName = itemView.findViewById<View>(R.id.tv_member_name) as TextView
        userName!!.text = mData.friendName
        profilePhoto = itemView.findViewById<View>(R.id.iv_selected_member) as ShapeableImageView
        removeMember = itemView.findViewById<View>(R.id.iv_deleted_selected_member) as ImageView
        Glide.with(itemView.context)
            .load(mData.headUrl)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(profilePhoto!!)

        removeMember!!.setOnClickListener {
            delegate.onRemoveMemberList(mData)
        }

    }

    override fun onClick(v: View?) {}
}