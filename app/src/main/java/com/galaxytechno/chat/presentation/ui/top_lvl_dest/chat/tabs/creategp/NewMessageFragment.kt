package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.tabs.creategp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Surface
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.galaxytechno.chat.R
import com.galaxytechno.chat.databinding.FragmentNewMessageBinding
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.CreateRoomViewModel
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.newmessage.NewMessageScreen
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.ChatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewMessageFragment : Fragment() {
    private var _binding: FragmentNewMessageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateRoomViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewMessageBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.newMessage.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ChatTheme {
                    Surface {
                        NewMessageScreen(
                            vm = viewModel,
                            goToInitMember = {
                                findNavController().navigate(R.id.action_newMessage_to_initMember)
                            },
                            goBack = {
                                findNavController().popBackStack()
                            }
                        )
                    }
                }
            }
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

/*private val vm: CreateGroupViewModel by activityViewModels()
private var offset = 0
private var limit = 30
private var firstTime = true
private var isLoading = false
private var friListAdapter: GpFriendListAdapter? = null
private val rvView by lazy {
    requireActivity().findViewById<RecyclerView>(R.id.rvFriendList)
}

override fun initialize() {
    super.initialize()
    offset = 0
    isLoading = true
    firstTime = true
    vm.getFriendList(offset, limit, "")

    friListAdapter = GpFriendListAdapter(requireContext(), object : FriendListDelegate {
        override fun onClickedFriList(data: FriListVos) {

            var url = if (data.headUrl.isNullOrEmpty()) {
                Constant.SERVER_IMAGE_URL
            } else {
                data.headUrl
            }
            val directions =
                NewMessageFragmentDirections.actionChatNewMessageFragmentToPersonalChatRoomFragment(
                    data.friendName.toString(),
                    data.friendId!!.toLong(),
                    10001,
                    url,
                    "fromChatNewMsg"
                )
            findNavController().navigate(directions)
        }

        override fun loadMore() {
            vm.addOffset()
            isLoading = true
            friListAdapter!!.isLoading(isLoading)
        }
    })
}

override fun setupView() {
    super.setupView()

    shouldShowShimmer(true)
    binding.toolbarCreateGroup.tvToolbarTitle.text = getString(R.string.new_message)

    binding.toolbarCreateGroup.ivBackArrow.setOnClickListener {
        findNavController().navigate(R.id.action_chatNewMessageFragment_to_dest_top_chat)
    }
}

override fun setupListener() {
    super.setupListener()
    binding.toolbarCreateGroup.ivBackArrow.setOnClickListener {
        findNavController().popBackStack()
    }

    */
/** search view Text change listener *//*
        binding.svFriend.setOnQueryTextListener(
            (object : SearchView.OnQueryTextListener,
                android.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    */
/** saving search text to view model  *//*
                    vm.getFriendList(0, limit, query.toString())

                    return true
                }

                override fun onQueryTextChange(inputSearchText: String?): Boolean {
                    if (inputSearchText.toString().isEmpty()) {
                        vm.getFriendList(0, limit, "")
                    }
                    return false
                }
            })
        )

        binding.btnCreateGroup.setOnClickListener {
            findNavController().navigate(R.id.action_createGroupFragment_to_chatNewGroupFragment)
        }
    }

    private fun shouldShowShimmer(flag: Boolean) {
        if (flag) {
            binding.shimmerLayout.visibility = View.VISIBLE
            binding.rvFriendList.visibility = View.GONE
            binding.shimmerLayout.startShimmer()
        } else {
            binding.shimmerLayout.visibility = View.GONE
            binding.rvFriendList.visibility = View.VISIBLE
            binding.shimmerLayout.stopShimmer()
        }
    }

    override fun observe() {
        super.observe()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vm.memberList.collectLatest {
                delay(500)
                shouldShowShimmer(false)

                friListAdapter!!.clearData()
                isLoading = false
                friListAdapter!!.isLoading(isLoading)

                if (it.isEmpty()) {
                    friListAdapter!!.isLastPage(true)
                    if (firstTime) {
                        isLoading = true
                        friListAdapter!!.isLoading(isLoading)
                    }
                } else {

                    firstTime = false
                    if (it.size < limit) {
                        friListAdapter!!.isLastPage(true)
                    } else {
                        friListAdapter!!.isLastPage(false)
                    }
                    rvView.adapter = friListAdapter
                    rvView.layoutManager =
                        LinearLayoutManager(context)
                    friListAdapter!!.clearData()
                    friListAdapter!!.isLoading(isLoading)
                    friListAdapter!!.appendNewData(it)
                }
            }
        }
    }*/