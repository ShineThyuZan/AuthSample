package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.tabs.creategp

//class GpFriendListAdapter(context: Context, val delegate: FriendListDelegate) :
//    BaseRecyclerAdapter<FriListViewHolder, FriListVos>(context) {
//    private val VIEW_TYPE_ITEM = 0
//    private val VIEW_TYPE_LOADING = 1
//    private var lastPage = false
//    private var isLoading = false
//    var proBar: ProgressBar? = null

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<FriListVos> {
//
////        return if (viewType == VIEW_TYPE_ITEM) {
////            val v = mLayoutInflator.inflate(R.layout.layout_gp_fri_list, parent, false)
////            FriListViewHolder(v, delegate)
////        } else {
////            val view = mLayoutInflator.inflate(R.layout.layout_load_more, parent, false)
////            LoadMoreViewHolder(view)
////        }
//    }
//
//    override fun onBindViewHolder(holder: BaseViewHolder<FriListVos>, position: Int) {
//        if (holder is FriListViewHolder) {
//            super.onBindViewHolder(holder, position)
//        } else {
//
//            if (lastPage || position == 0) {
//                proBar = holder.itemView.findViewById<View>(R.id.progressBar) as ProgressBar
//                proBar!!.visibility = View.GONE
//            } else {
//                proBar!!.visibility = View.VISIBLE
//
//            }
//        }
//        if (position == mData!!.size - 30 && !lastPage && !isLoading) {
//            delegate.loadMore()
//        }
//    }
//
//    fun isLastPage(lastPage: Boolean) {
//        this.lastPage = lastPage
//        notifyDataSetChanged()
//    }
//
//    fun isLoading(isLoading: Boolean) {
//        this.isLoading = isLoading
////        Handler().postDelayed({ this.isLoading = false }, 5000)
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return if (mData!!.size == position) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
//    }
//
//    override fun getItemCount(): Int {
//        return if (mData == null && mData!!.size == 0) 0 else mData!!.size + 1
//    }
//
//    class LoadMoreViewHolder(v: View) : BaseViewHolder<FriListVos>(v) {
//        override fun setData(data: FriListVos) {
//        }
//
//        override fun onClick(v: View?) {
//        }
//    }
//}