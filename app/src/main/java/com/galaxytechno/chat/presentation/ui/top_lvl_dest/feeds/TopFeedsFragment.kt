package com.galaxytechno.chat.presentation.ui.top_lvl_dest.feeds

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.galaxytechno.chat.databinding.TopLvlFeedBinding
import com.galaxytechno.chat.model.dto.DataObj
import com.galaxytechno.chat.presentation.base.TopFragment
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.presentation.extension.displayToast
import com.galaxytechno.chat.presentation.single_activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class TopFeedsFragment : TopFragment<TopLvlFeedBinding>(TopLvlFeedBinding::inflate),
    FeedUserActivityDelegate {

    private val viewModel: FeedViewModel by activityViewModels()
    private lateinit var feedUserActivityAdapter: FeedUserActivityAdapter
    private lateinit var feedAdapter: FeedAdapter
    private var feedUserActivityList: MutableList<DataObj> = mutableListOf()

    override fun setupListener() {
        super.setupListener()
        shouldShowShimmer(true)

        binding.ivSearchView.setOnClickListener {
            (activity as MainActivity).comingSoonDialog()
            //  findNavController().navigate(R.id.action_dest_top_feeds_to_feedSearchFragment)
        }
        binding.ivProfileBack.setOnClickListener {
            (activity as MainActivity).comingSoonDialog()
        }
        binding.ivProfileMore.setOnClickListener {
            (activity as MainActivity).comingSoonDialog()
        }
    }

    /** user's profile activity recycler view */
    private fun setupFeedsUserActivityRecycler() {
        feedUserActivityAdapter = FeedUserActivityAdapter(requireContext(), this@TopFeedsFragment)
        binding.rvActivity.apply {
            this.adapter = feedUserActivityAdapter
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.setHasFixedSize(true)
        }
    }

    /** user's profile activity recycler view */
    private fun setupFeedRecycler() {
        feedAdapter = FeedAdapter(requireContext(), this@TopFeedsFragment)
        binding.rvFeed.apply {
            this.adapter = feedAdapter
            this.layoutManager = LinearLayoutManager(context)
            this.setHasFixedSize(true)
        }
    }

    private fun shouldShowShimmer(flag: Boolean) {
        if (flag) {
            binding.shimmerFeedLayout.visibility = View.VISIBLE
            binding.rvFeed.visibility = View.GONE
            binding.shimmerFeedLayout.startShimmer()
        } else {
            binding.shimmerFeedLayout.visibility = View.GONE
            binding.rvFeed.visibility = View.VISIBLE
            binding.rvActivity.visibility = View.VISIBLE
            binding.shimmerFeedLayout.stopShimmer()
        }
    }

    override fun observe() {
        super.observe()
        viewModel.bankObj.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {

                when (it) {
                    is RemoteEvent.ErrorEvent -> {}
                    is RemoteEvent.LoadingEvent -> {}
                    is RemoteEvent.SuccessEvent -> {
                        setupFeedsUserActivityRecycler()
                        setupFeedRecycler()
                        it.data.let { bankData ->
                            delay(1000L)
                            shouldShowShimmer(false)
                            feedUserActivityList = bankData!!.data as MutableList<DataObj>
                            feedUserActivityAdapter.setNewData(feedUserActivityList)

                            feedAdapter.setNewData(feedUserActivityList)
                        }
                    }
                }
            }
        }
    }

    override fun onClickedFeedUserActivity(data: DataObj) {
        displayToast(data.name)
    }


}