package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.tabs.creategp

import android.os.Build
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.galaxytechno.chat.R
import com.galaxytechno.chat.model.vos.FriListVos
import com.galaxytechno.chat.presentation.base.BaseViewHolder
import com.google.android.material.imageview.ShapeableImageView

class SelectedFriListViewHolder(
    itemView: View,
    val onClickedChecked: (item: FriListVos, isChecked: Boolean) -> Unit,
) :
    BaseViewHolder<FriListVos>(itemView) {
    var imageView: ShapeableImageView? = null
    var friname: TextView? = null
    var ivSelected: CheckBox? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setData(data: FriListVos) {
        imageView = itemView.findViewById<View>(R.id.ivFriList) as ShapeableImageView
        friname = itemView.findViewById<View>(R.id.tvFriName) as TextView
        ivSelected = itemView.findViewById<View>(R.id.iv_selectable) as CheckBox
        mData = data
        Glide.with(itemView.context)
            .load(mData.headUrl)
            .placeholder(R.drawable.place_holder)
            .into(imageView!!)
        friname!!.text = mData.friendName

//        ivSelected!!.setOnCheckedChangeListener { buttonView, isChecked ->
//            onClickedChecked(mData, isChecked)
//        }
    }

    override fun onClick(v: View?) {
//        delegate.onClickedFriList(mData)
    }

}