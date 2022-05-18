package com.galaxytechno.chat.presentation.ui.top_lvl_dest.feeds

import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.galaxytechno.chat.R
import com.galaxytechno.chat.model.dto.DataObj
import com.galaxytechno.chat.presentation.base.BaseViewHolder
import com.google.android.material.imageview.ShapeableImageView

class FeedUserActivityViewHolder(
    itemView: View,
    private val delegate: FeedUserActivityDelegate
) :
    BaseViewHolder<DataObj>(itemView) {
    var userName: TextView? = null
    var profilePhoto: ShapeableImageView? = null

    override fun setData(data: DataObj) {
        mData = data
        userName = itemView.findViewById<View>(R.id.tvFeedUserName) as TextView
//        userName!!.text = mData.name
        profilePhoto = itemView.findViewById<View>(R.id.ivFeedUserActivity) as ShapeableImageView

//        Glide.with(itemView.context)
//            .load(mData.photo)
//            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//            .into(profilePhoto!!)

        when(mData.name)  {
            "America" -> {
                userName!!.text = "Amber"
                Glide.with(itemView.context)
                    .load("https://galaxyshopbucket.s3.ap-southeast-1.amazonaws.com/PROFILE_IMAGES/07cc40ec-0360-41a4-93d3-7fe92678469b.jpg")
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(profilePhoto!!)
            }
            "India"-> {
                userName!!.text = "Jisoo"
                Glide.with(itemView.context)
                    .load("https://galaxyshopbucket.s3.ap-southeast-1.amazonaws.com/PROFILE_IMAGES/916e70cf-1c5c-4f48-bd20-c33e79823ae3.jpg")
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(profilePhoto!!)
            }
            "Myanmar"-> {
                userName!!.text = "Lisa"
                Glide.with(itemView.context)
                    .load("https://galaxyshopbucket.s3.ap-southeast-1.amazonaws.com/PROFILE_IMAGES/fe2692cb-0a1c-4c45-86be-66a01ae74860.jpg")
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(profilePhoto!!)
            }

            "Brunei"-> {
                userName!!.text = "Rose"
                Glide.with(itemView.context)
                    .load("https://galaxyshopbucket.s3.ap-southeast-1.amazonaws.com/PROFILE_IMAGES/772e3cb9-8f80-4119-bd0b-713a18e29af3.jpg")
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(profilePhoto!!)
            }
            "Indonesia"-> {
                userName!!.text = "Scarlett"
                Glide.with(itemView.context)
                    .load("https://galaxyshopbucket.s3.ap-southeast-1.amazonaws.com/PROFILE_IMAGES/997c2653-6fe0-40da-96bb-572f2cedbbd0.jpg")
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(profilePhoto!!)
            }

            "Germany"-> {
                userName!!.text = "Lady Ga Ga"
                Glide.with(itemView.context)
                    .load("https://galaxyshopbucket.s3.ap-southeast-1.amazonaws.com/PROFILE_IMAGES/18b725cd-4b5b-468b-83b1-8f087da1e47f.jpg")
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(profilePhoto!!)
            }

            "Argentina"-> {
                userName!!.text = "Angelina"
                Glide.with(itemView.context)
                    .load("https://galaxyshopbucket.s3.ap-southeast-1.amazonaws.com/PROFILE_IMAGES/78abec13-17a9-41e9-9632-7069f484b8fe.jpeg")
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(profilePhoto!!)
            }

            "Thailand"-> {
                userName!!.text = "Joli"
                Glide.with(itemView.context)
                    .load("https://galaxyshopbucket.s3.ap-southeast-1.amazonaws.com/PROFILE_IMAGES/00b61b6b-d1e7-4773-9b91-71d353db9df4.jpg")
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(profilePhoto!!)
            }
            else -> {
                userName!!.text = "Nancy"
                Glide.with(itemView.context)
                    .load("https://galaxyshopbucket.s3.ap-southeast-1.amazonaws.com/PROFILE_IMAGES/6dd0a385-a324-4a49-adba-62d51b76a8fe.jpg")
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(profilePhoto!!)
            }
        }

        profilePhoto!!.setOnClickListener {
//            Toast.makeText(itemView.context, mData.photo, Toast.LENGTH_LONG).show()
        }
    }

    override fun onClick(v: View?) {
        delegate.onClickedFeedUserActivity(mData)
    }
}