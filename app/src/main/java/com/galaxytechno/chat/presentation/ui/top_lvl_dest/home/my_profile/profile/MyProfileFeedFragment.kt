package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.my_profile.profile

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.galaxytechno.chat.databinding.FragmentMyProfileFeedBinding
import com.galaxytechno.chat.model.dto.DataObj
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.presentation.extension.displayToast
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.feeds.FeedAdapter
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.feeds.FeedUserActivityDelegate
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.feeds.FeedViewModel
import kotlinx.coroutines.delay

class MyProfileFeedFragment :
    OtherLvlFragment<FragmentMyProfileFeedBinding>(FragmentMyProfileFeedBinding::inflate),
    FeedUserActivityDelegate {

    private val viewModel: FeedViewModel by activityViewModels()
    private lateinit var feedAdapter: FeedAdapter
    private var feedUserActivityList: MutableList<DataObj> = mutableListOf()

    private fun setupFeedRecycler() {
        feedAdapter = FeedAdapter(requireContext(), this@MyProfileFeedFragment)
        binding.rvMyProfileFeed.apply {
            this.adapter = feedAdapter
            this.layoutManager = LinearLayoutManager(context)
            this.setHasFixedSize(true)
        }
    }

    override fun observe() {
        super.observe()
        viewModel.bankObj.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {

                when (it) {
                    is RemoteEvent.ErrorEvent -> {}
                    is RemoteEvent.LoadingEvent -> {

                    }
                    is RemoteEvent.SuccessEvent -> {
                        setupFeedRecycler()
                        it.data.let { bankData ->
                            delay(1000L)
                            feedUserActivityList = bankData!!.data as MutableList<DataObj>
                            feedAdapter.setNewData(feedUserActivityList)
                        }
                    }
                }
            }
        }
    }

    override fun onClickedFeedUserActivity(data: DataObj) {
//        displayToast(data.name)
    }


}