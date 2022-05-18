package com.galaxytechno.chat.presentation.ui.auth.pwd_forget

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.galaxytechno.chat.R
import com.galaxytechno.chat.model.dto.Country
import com.galaxytechno.chat.model.dto.LanguageVos
import com.galaxytechno.chat.presentation.base.BaseRecyclerAdapter
import com.galaxytechno.chat.presentation.base.BaseViewHolder
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.PersonalChatRoomViewHolder


class CountrySelectedAdapter(context: Context, private val delegate: CountrySelectedDelegate) :
    BaseRecyclerAdapter<CountrySelectedViewHolder, Country>(context) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<Country> {
        val view = mLayoutInflator.inflate(R.layout.country_items_view_pod, parent, false)
        return CountrySelectedViewHolder(view, delegate)
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<Country>,
        position: Int
    ) {
        super.onBindViewHolder(holder, position)
        val size = mData?.size
        if (size != null) {
            if(position == size-1) {
                var divider : View? = null
                divider = holder.itemView.findViewById(R.id.div_country) as View
                divider.visibility = View.GONE
            }
        }
    }

}