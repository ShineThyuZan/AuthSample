package com.galaxytechno.chat.presentation.ui.top_lvl_dest.feeds

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.galaxytechno.chat.R
import com.galaxytechno.chat.model.dto.DataObj
import com.galaxytechno.chat.presentation.base.BaseViewHolder
import com.google.android.material.imageview.ShapeableImageView

class FeedViewHolder(
    itemView: View,
    private val delegate: FeedUserActivityDelegate
) :
    BaseViewHolder<DataObj>(itemView) {
    var userName: TextView? = null
    var profilePhoto: ShapeableImageView? = null
    var feedPhoto: ImageView? = null
    var location: TextView? = null
    var commentCount: TextView? = null
    var loveCount: TextView? = null
    var description: TextView? = null
    var timeAgo: TextView? = null

    override fun setData(data: DataObj) {
        mData = data

        userName = itemView.findViewById<View>(R.id.tvFeedName) as TextView
        userName!!.text = mData.name

        profilePhoto = itemView.findViewById<View>(R.id.ivProfileFeed) as ShapeableImageView
        Glide.with(itemView.context)
            .load(mData.photo)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(profilePhoto!!)

        feedPhoto = itemView.findViewById(R.id.ivFeedPhoto) as ImageView
//
//        Glide.with(itemView.context)
//            .load("https://galaxyshopbucket.s3.ap-southeast-1.amazonaws.com/PROFILE_IMAGES/fdd0d562-6a37-47c9-b1ac-3b6241459d87.jpg")
//            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//            .into(feedPhoto!!)

        when(mData.name)  {
            "America" -> {
                Glide.with(itemView.context)
                    .load("https://galaxyshopbucket.s3.ap-southeast-1.amazonaws.com/PROFILE_IMAGES/612f03f9-5bd4-4ddc-97c5-be41380d5cb3.jpg")
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(feedPhoto!!)
            }
            "India"-> {
                Glide.with(itemView.context)
                    .load("https://galaxyshopbucket.s3.ap-southeast-1.amazonaws.com/PROFILE_IMAGES/717469a8-8b57-4ea3-b3e6-01453070c936.jpg")
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(feedPhoto!!)
            }
            "Myanmar"-> {
                Glide.with(itemView.context)
                    .load("https://galaxyshopbucket.s3.ap-southeast-1.amazonaws.com/PROFILE_IMAGES/90f85ead-846e-489c-87e9-3a5347768164.jpg")
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(feedPhoto!!)
            }

            "Brunei"-> {
                Glide.with(itemView.context)
                    .load("https://galaxyshopbucket.s3.ap-southeast-1.amazonaws.com/PROFILE_IMAGES/49e2b797-678c-4196-84b1-cb78a95c2a92.jpg")
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(feedPhoto!!)
            }
            "Indonesia"-> {
                Glide.with(itemView.context)
                    .load("https://galaxyshopbucket.s3.ap-southeast-1.amazonaws.com/PROFILE_IMAGES/14b27865-84d6-4366-9c37-f58a7ee32c5a.jpg")
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(feedPhoto!!)
            }

            "Germany"-> {
                Glide.with(itemView.context)
                    .load("https://galaxyshopbucket.s3.ap-southeast-1.amazonaws.com/PROFILE_IMAGES/ec929fd7-e612-4d0a-b317-fc264ba0a460.jpg")
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(feedPhoto!!)
            }

            "Argentina"-> {
                Glide.with(itemView.context)
                    .load("https://galaxyshopbucket.s3.ap-southeast-1.amazonaws.com/PROFILE_IMAGES/ba672242-0916-40e2-a19a-c4cb1783e07c.jpg")
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(feedPhoto!!)
            }

                "Thailand"-> {
            Glide.with(itemView.context)
                .load("https://galaxyshopbucket.s3.ap-southeast-1.amazonaws.com/PROFILE_IMAGES/00b61b6b-d1e7-4773-9b91-71d353db9df4.jpg")
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(feedPhoto!!)
        }
            else -> {
                Glide.with(itemView.context)
                    .load("https://galaxyshopbucket.s3.ap-southeast-1.amazonaws.com/PROFILE_IMAGES/d4810fec-5878-4088-98ee-427fde15ada7.jpg")
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(feedPhoto!!)
            }
        }

        location = itemView.findViewById(R.id.tvFeedLocation) as TextView
        location!!.text = mData.id

        commentCount = itemView.findViewById(R.id.tvFeedCommentCount) as TextView
        commentCount!!.text = "5 comments"

        loveCount = itemView.findViewById(R.id.tvFeedLoveCount) as TextView
        loveCount!!.text = "21 likes"

        description = itemView.findViewById(R.id.tvFeedDescription) as TextView
//        description!!.text = mData.photo

        timeAgo = itemView.findViewById(R.id.tvTimeAgo) as TextView
        timeAgo!!.text = "3 hours ago"


    }

    override fun onClick(v: View?) {
        delegate.onClickedFeedUserActivity(mData)
    }
}