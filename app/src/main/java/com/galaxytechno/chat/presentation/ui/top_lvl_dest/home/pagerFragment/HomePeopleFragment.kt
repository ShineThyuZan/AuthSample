package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.pagerFragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.galaxytechno.chat.R
import com.galaxytechno.chat.databinding.FragmentHomePeopleBinding
import com.galaxytechno.chat.model.dto.UserVos
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.presentation.extension.displaySnack
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.HomeSearchFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomePeopleFragment :
    OtherLvlFragment<FragmentHomePeopleBinding>(FragmentHomePeopleBinding::inflate) {

    private val viewModel: HomeFeatureViewModel by activityViewModels()
    private var offset = 0
    private var limit = 30
    private var firstTime = true
    private var isLoading = false
    private var friListAdapter: SearchUserListAdapter? = null
    private val rvView by lazy {
        requireActivity().findViewById<RecyclerView>(R.id.rv_home_people)
    }

    override fun initialize() {
        super.initialize()
        offset = 0
        isLoading = true
        firstTime = true
        viewModel.getFriendSearch(offset++, limit, viewModel.searchText.toString())
        friListAdapter = SearchUserListAdapter(requireContext(), object : UserListDelegate {
            override fun onClickedFriList(data: UserVos) {
                val directions =
                    HomeSearchFragmentDirections.actionHomeSearchToFriendProfileFragment(
                        data.userId,
                        "fromHomePeople"
                    )
                findNavController().navigate(directions)
            }

            override fun loadMore() {
                viewModel.getFriendSearch(offset++, limit, viewModel.searchText.toString())
                isLoading = true
                friListAdapter!!.isLoading(isLoading)
            }
        })
    }

    override fun observe() {
        super.observe()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.friListEvent.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                    }
                    is RemoteEvent.LoadingEvent -> {
                    }

                    is RemoteEvent.SuccessEvent -> {

                        friListAdapter!!.clearData()
                        isLoading = false
                        friListAdapter!!.isLoading(isLoading)

                        if (it.data!!.data.profileInfoList!!.isNullOrEmpty()) {
                            friListAdapter!!.isLastPage(true)
                            if (firstTime) {
                                isLoading = true
                                friListAdapter!!.isLoading(isLoading)

                            }
                        } else {
                            firstTime = false
                            if (it.data.data.profileInfoList!!.size < limit) {
                                friListAdapter!!.isLastPage(true)
                            } else {
                                friListAdapter!!.isLastPage(false)
                            }
                            rvView.adapter = friListAdapter
                            rvView.layoutManager =
                                LinearLayoutManager(context)
                            friListAdapter!!.clearData()
                            friListAdapter!!.isLoading(isLoading)
                            friListAdapter!!.appendNewData(it.data.data.profileInfoList)
                        }
                    }
                }
            }
        }
    }
}