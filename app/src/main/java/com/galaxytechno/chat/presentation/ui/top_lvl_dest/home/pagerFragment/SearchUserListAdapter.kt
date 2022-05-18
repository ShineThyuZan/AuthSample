package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.pagerFragment

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.galaxytechno.chat.R
import com.galaxytechno.chat.model.dto.UserVos
import com.galaxytechno.chat.presentation.base.BaseRecyclerAdapter
import com.galaxytechno.chat.presentation.base.BaseViewHolder

class SearchUserListAdapter(context: Context, val delegate: UserListDelegate ) :
    BaseRecyclerAdapter<UserListViewHolder, UserVos>(context) {
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1
    private var lastPage = false
    private var isLoading = false
    var proBar: ProgressBar? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<UserVos> {

        return if (viewType == VIEW_TYPE_ITEM) {
            val v = mLayoutInflator.inflate(R.layout.layout_fri_list_view_pod, parent, false)
            UserListViewHolder(v, delegate)
        } else {
            val view = mLayoutInflator.inflate(R.layout.layout_load_more, parent, false)
            LoadMoreViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<UserVos>, position: Int) {
        if (holder is UserListViewHolder) {
            super.onBindViewHolder(holder, position)
        } else {
            if (lastPage || position == 0) {

                proBar = holder.itemView.findViewById<View>(R.id.progressBar) as ProgressBar
                proBar!!.visibility = View.GONE
            } else {
                proBar!!.visibility = View.VISIBLE
            }
        }
        if (position == mData!!.size - 30 && !lastPage && !isLoading) {
            delegate.loadMore()
        }
    }

    fun isLastPage(lastPage: Boolean) {
        this.lastPage = lastPage
        notifyDataSetChanged()
    }

    fun isLoading(isLoading: Boolean) {
        this.isLoading = isLoading
    }

    override fun getItemViewType(position: Int): Int {
        return if (mData!!.size == position) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun getItemCount(): Int {
        return if (mData == null && mData!!.size == 0) 0 else mData!!.size + 1
    }

    class LoadMoreViewHolder(v: View) : BaseViewHolder<UserVos>(v) {
        override fun setData(data: UserVos) {
        }

        override fun onClick(v: View?) {
        }
    }
}