package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.contact

import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.galaxytechno.chat.R
import com.galaxytechno.chat.common.Constant
import com.galaxytechno.chat.model.dto.ContactFriObj
import com.galaxytechno.chat.presentation.base.BaseViewHolder
import com.google.android.material.imageview.ShapeableImageView

class FriContactViewHolder(
    itemView: View,
    private val delegate: FriContactDelegate
) :
    BaseViewHolder<ContactFriObj>(itemView) {
    var contactFriName: TextView? = null
    var profilePhoto: ShapeableImageView? = null
    var textPhone: TextView? = null

    override fun setData(data: ContactFriObj) {
        mData = data
        contactFriName = itemView.findViewById<View>(R.id.tvContactFriName) as TextView
        contactFriName!!.text = mData.name

        textPhone = itemView.findViewById<View>(R.id.tv_contact_fri_phone) as TextView
        textPhone!!.text = mData.mobile

        profilePhoto = itemView.findViewById<View>(R.id.ivContactFriend) as ShapeableImageView

        Glide.with(itemView.context)
            .load(mData.headUrl)
            .error(Constant.SERVER_IMAGE_URL)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(profilePhoto!!)
    }

    override fun onClick(v: View?) {
        delegate.onClickedFriContact(mData)
    }
}