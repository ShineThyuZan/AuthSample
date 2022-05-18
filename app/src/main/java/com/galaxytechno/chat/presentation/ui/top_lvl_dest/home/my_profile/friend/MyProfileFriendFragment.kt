package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.my_profile.friend

import android.os.Handler
import androidx.activity.addCallback
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.galaxytechno.chat.R
import com.galaxytechno.chat.databinding.FragmentMyProfileFriendBinding
import com.galaxytechno.chat.model.vos.FriListVos
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.presentation.extension.displaySnack
import com.galaxytechno.chat.presentation.single_activity.MainActivity
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.my_profile.MyProfileViewModel
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.pagerFragment.FriListAdapter
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.pagerFragment.FriendListDelegate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MyProfileFriendFragment :
    OtherLvlFragment<FragmentMyProfileFriendBinding>(FragmentMyProfileFriendBinding::inflate) {
    private val vm: MyProfileViewModel by activityViewModels()
    private var offset = 0
    private var limit = 30
    private var firstTime = true
    private var isLoading = false
    private var friListAdapter: FriListAdapter? = null
    private var friendList: MutableList<FriListVos> = mutableListOf()
    private val rvView by lazy {
        requireActivity().findViewById<RecyclerView>(R.id.rvMyProfileFriend)
    }

    override fun setupView() {
        super.setupView()
        binding.toolbarFriend.ivBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbarFriend.tvToolbarTitle.text = getString(R.string.friends)
        binding.toolbarFriend.ivSearch.setOnClickListener {
//            binding.toolbarFriend.otherLvlPrimaryToolbar.isVisible = false
//            binding.toolbarSearch.isVisible = true
            (activity as MainActivity).comingSoonDialog()
        }

        binding.ivSearchBack.setOnClickListener {
            binding.toolbarFriend.otherLvlPrimaryToolbar.isVisible = true
            binding.toolbarSearch.isVisible = false
        }

        binding.toolbarFriend.ivBackArrow.setOnClickListener {
            findNavController().navigate(R.id.action_myProfileFriendFragment_to_myProfileFragment)
        }
    }

    override fun setupListener() {
        super.setupListener()
//        binding.svFriend.setOnQueryTextListener(object :
//            SearchView.OnQueryTextListener,
//            android.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                vm.getFriendList(offset, limit, query.toString())
//                return true
//            }
//
//            override fun onQueryTextChange(inputSearchText: String?): Boolean {
//                if (inputSearchText.isNullOrBlank()) {
//                    friListAdapter!!.clearData()
//                    vm.getFriendList(offset, limit, "")
//                } else {
//                    Handler().postDelayed({
//                        run {
//                            inputSearchText.let {
//                                if (it.length >= 3) {
//                                    friListAdapter!!.clearData()
//                                    vm.getFriendList(offset, limit, inputSearchText.trim())
//                                }
//                            }
//                        }
//                    }, 2000)
//                }
//                return true
//            }
//        })

        /** key action on back press */
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
         findNavController().navigate(R.id.action_myProfileFriendFragment_to_myProfileFragment)
        }
    }

    override fun initialize() {
        super.initialize()
        offset = 0
        isLoading = true
        firstTime = true
        vm.getFriendList(offset, limit, "")
        friListAdapter = FriListAdapter(requireContext(), object : FriendListDelegate {
            override fun onClickedFriList(data: FriListVos) {
                val directions =
                    MyProfileFriendFragmentDirections.myProfileFriendToFriendProfileFragment(
                        data.friendId!!.toLong(),
                        "fromMyProfileFriend"
                    )
                findNavController().navigate(directions)
            }

            override fun loadMore() {
                vm.getFriendList(offset++, limit, "")
                isLoading = true
                friListAdapter!!.isLoading(isLoading)
            }
        })
    }


    override fun observe() {
        super.observe()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vm.friListEvent.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                    }
                    is RemoteEvent.LoadingEvent -> {
                    }
                    is RemoteEvent.SuccessEvent -> {
                        friListAdapter!!.clearData()
                        isLoading = false
                        friListAdapter!!.isLoading(isLoading)

                        if (it.data!!.data.friendList!!.isEmpty()) {
                            binding.root.displaySnack("No matching data Sir!")
                            friListAdapter!!.isLastPage(true)
                            if (firstTime) {
                                isLoading = true
                                friListAdapter!!.isLoading(isLoading)
                            }
                        } else {
                            firstTime = false
                            if (it.data.data.friendList!!.size < limit) {
                                friListAdapter!!.isLastPage(true)
                            } else {
                                friListAdapter!!.isLastPage(false)
                            }
                            rvView.adapter = friListAdapter
                            rvView.layoutManager =
                                LinearLayoutManager(context)
                            friListAdapter!!.clearData()
                            friListAdapter!!.isLoading(isLoading)
                            friListAdapter!!.appendNewData(it.data.data.friendList)
                        }
                    }
                }
            }
        }
    }
}