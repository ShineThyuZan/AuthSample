package com.galaxytechno.chat.presentation.ui.auth.pwd_forget

import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.galaxytechno.chat.R
import com.galaxytechno.chat.model.dto.Country
import com.galaxytechno.chat.presentation.base.BaseViewHolder
import com.google.android.material.imageview.ShapeableImageView

class CountrySelectedViewHolder(
    itemView: View,
    private val delegate: CountrySelectedDelegate
) :
    BaseViewHolder<Country>(itemView) {
    var countryNameTextView: TextView? = null
    var countryCodeTextView: TextView? = null
    var countryNickTextView: TextView? = null
    var countryImageView: ShapeableImageView? = null

    override fun setData(data: Country) {
        mData = data
        countryNameTextView = itemView.findViewById<View>(R.id.tvCountryName) as TextView
        countryNameTextView!!.text = mData.country

        countryCodeTextView = itemView.findViewById<View>(R.id.tvCountryCode) as TextView
        countryCodeTextView!!.text = mData.countryCode

        countryNickTextView = itemView.findViewById<View>(R.id.tv_country_nick) as TextView
        ("(" + mData.countryNick +")").also { countryNickTextView!!.text = it }

        countryImageView = itemView.findViewById<View>(R.id.ivCountry) as ShapeableImageView
        Glide.with(itemView.context)
            .load(mData.countryFlagUrl)
            .placeholder(R.drawable.ic_yk_logo)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(countryImageView!!)
    }

    override fun onClick(v: View?) {
        delegate.onClickSelectedCountry(mData)
    }
}