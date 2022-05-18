package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.notification

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.galaxytechno.chat.R
import com.galaxytechno.chat.databinding.FragmentNotiFriendRequestBinding
import com.galaxytechno.chat.model.vos.FriReqVos
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class NotiFriendRequestFragment :
    OtherLvlFragment<FragmentNotiFriendRequestBinding>(FragmentNotiFriendRequestBinding::inflate) {
    private val vm: NotificationViewModel by viewModels()
    private var offset = 0
    private var limit = 30
    private var firstTime = true
    private var isLoading = false
    private lateinit var friReqListAdapter: FriendRequestListAdapter
    private var friRequestVosList: MutableList<FriReqVos> = mutableListOf()
    private val rvView by lazy {
        requireActivity().findViewById<RecyclerView>(R.id.rvFriRequestList)
    }

    override fun initialize() {
        super.initialize()
        binding.swipeRefresh.isRefreshing = true
        binding.swipeRefresh.setOnRefreshListener {
            vm.getNotiFriRequestList(offset)
            isLoading = true
            friReqListAdapter.isLoading(isLoading)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupView() {
        super.setupView()
        vm.getNotiFriRequestList(offset)
        observe()
        offset = 0
        isLoading = true
        firstTime = true
        friReqListAdapter =
            FriendRequestListAdapter(requireContext(), object : FriRequestListDelegate {
                override fun onClickedName(data: FriReqVos) {
                    val directions =
                        NotificationFragmentDirections.actionNotificationFragmentToFriendRequestConfirmationFragment(
                            data.requesterId, data.friendRequestId.toString()
                        )
                    findNavController().navigate(directions)
                }

                override fun loadMore() {
                    vm.addOffset()
                    isLoading = true
                    friReqListAdapter.isLoading(isLoading)
                }
            })
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun observe() {
        super.observe()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vm.requestFriListChannel.collectLatest { list ->
                when (list) {
                    is RemoteEvent.ErrorEvent -> {
                    }
                    is RemoteEvent.LoadingEvent -> {
                    }
                    is RemoteEvent.SuccessEvent -> {
                        friRequestVosList =
                            list.data!!.data.friendRequestList as MutableList<FriReqVos>
                        binding.swipeRefresh.isRefreshing = false
                        isLoading = false
                        friReqListAdapter.isLoading(isLoading)
                        if (friRequestVosList.isEmpty()) {
                            binding.clFindPeople.visibility = View.VISIBLE
                            binding.rvFriRequestList.visibility = View.GONE

                            friReqListAdapter.isLastPage(true)
                            if (firstTime) {
                                isLoading = true
                                friReqListAdapter.isLoading(isLoading)
                            }
                        } else {
                            binding.clFindPeople.visibility = View.GONE
                            binding.rvFriRequestList.visibility = View.VISIBLE
                            firstTime = false
                            if (friRequestVosList.size < limit) {
                                friReqListAdapter.isLastPage(true)
                            } else {
                                friReqListAdapter.isLastPage(false)
                            }
                            rvView.adapter = friReqListAdapter
                            rvView.layoutManager =
                                LinearLayoutManager(context)
                            friReqListAdapter.clearData()
                            friReqListAdapter.isLoading(isLoading)
                            friReqListAdapter.appendNewData(friRequestVosList)

                        }
                    }
                }
            }
        }
    }
}