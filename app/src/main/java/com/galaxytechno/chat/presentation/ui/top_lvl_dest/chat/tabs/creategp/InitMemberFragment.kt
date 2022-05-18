package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.tabs.creategp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.galaxytechno.chat.R
import com.galaxytechno.chat.databinding.FragmentIntitMemberBinding
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.CreateRoomViewModel
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.initmember.InitMemberScreen
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.newmessage.NewMessageScreen
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.ChatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InitMemberFragment : Fragment() {
    private var _binding: FragmentIntitMemberBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateRoomViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntitMemberBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.initMember.apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ChatTheme {
                    Surface {
                        InitMemberScreen(
                            vm = viewModel,
                            goBack = {
                                findNavController().popBackStack()
                            },
                            goToCreateGroup = {
                                findNavController().navigate(R.id.action_initMember_to_createGroup)
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


/*:
OtherLvlFragment<FragmentIntitMemberBinding>(FragmentIntitMemberBinding::inflate) {

private var memberList = listOf<FriListVos>()
private var selectedList = listOf<FriListVos>()
private val vm: CreateGroupViewModel by activityViewModels()
private var offset = 0
private var limit = 30
private var firstTime = true
private var isLoading = false
private var memberAdapter: MemberAdapter? = null
private var selectedMemberAdapter: SelectedMemberAdapter? = null

private fun onCheckedClicked(item: FriListVos, isChecked: Boolean) {
    if (isChecked) {
        vm.addSelectedItem(item = item)
    } else {
        vm.deleteSelectedItem(item = item)
        memberAdapter!!.notifyDataSetChanged()
    }
}
private fun onDeleteItem(item: FriListVos) {
    vm.deleteSelectedItem(item = item)
}

override fun initialize() {
    super.initialize()
    offset = 0
    isLoading = true
    firstTime = true
    vm.getFriendList(offset, limit, "")
    setupSelectedMemberAdapter()
    setupMemberAdapter()
}

override fun setupView() {
    super.setupView()
    shouldShowShimmer(true)
    binding.tvToolbarTitle.text = getString(R.string.new_group)

    */
/** physical back key event for
 * *//*
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val authNavOptions = NavOptions.Builder()
                .setPopUpTo(R.id.newMessage, true)
                .build()
            findNavController().navigate(
                R.id.action_chatNewGroupFragment_to_chatNewMessageFragment,
                Bundle(),
                authNavOptions
            )
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

    override fun setupListener() {
        super.setupListener()
        */
/** ui back arrow
 * *//*
        binding.ivBackArrow.setOnClickListener {
            val authNavOptions = NavOptions.Builder()
                .setPopUpTo(R.id.newMessage, true)
                .build()
            findNavController().navigate(
                R.id.action_chatNewGroupFragment_to_chatNewMessageFragment,
                Bundle(),
                authNavOptions
            )
        }

        */
/**
 * search people with search View
 *//*
        binding.svPeople.setOnQueryTextListener(
            (object : SearchView.OnQueryTextListener,
                android.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    */
/** call api with search text
 * *//*
                    vm.getFriendList(0, limit, query.toString())
                    return true
                }

                override fun onQueryTextChange(inputSearchText: String?): Boolean {
                    */
/** call api with empty string
 * *//*
                    if (inputSearchText.toString().isEmpty()) {
                        vm.getFriendList(0, limit, "")
                    }
                    return false
                }
            })
        )
        binding.ivCreateGp.setOnClickListener {
            findNavController().navigate(R.id.action_chatNewGroupFragment_to_chatNewGroupCreateFragment)
        }
    }

    override fun observe() {
        super.observe()

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            vm.memberList.collectLatest {
                memberList = it
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            vm.selectedList.collectLatest {
                selectedList = it
                memberAdapter?.updateSelectedList(it)
                selectedMemberAdapter?.setNewData(it.toMutableList())

                binding.tvMember.text =
                    getString(R.string.chat_group_member, vm.selectedList.value.size, 50)
            }
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vm.memberList.collectLatest {
                delay(500)
                shouldShowShimmer(false)


                memberAdapter!!.clearData()
                isLoading = false
                memberAdapter!!.isLoading(isLoading)

                if (it.isEmpty()) {
                    memberAdapter!!.isLastPage(true)
                    if (firstTime) {
                        isLoading = true
                        memberAdapter!!.isLoading(isLoading)
                    }
                } else {

                    firstTime = false
                    if (it.size < limit) {
                        memberAdapter!!.isLastPage(true)
                    } else {
                        memberAdapter!!.isLastPage(false)
                    }
                    memberAdapter!!.clearData()
                    memberAdapter!!.isLoading(isLoading)
                    memberAdapter!!.appendNewData(it)
                }
            }
        }
    }


    private fun setupMemberAdapter() {
        memberAdapter = MemberAdapter(
            context = requireContext(),
            selectedList = selectedList,
            onClickedChecked = { item, isChecked ->
                onCheckedClicked(item = item, isChecked = isChecked)
            }
        )
        binding.rvFriendList.apply {
            this.adapter = memberAdapter
            this.layoutManager = LinearLayoutManager(context)
            this.setHasFixedSize(true)
        }
    }

    private fun setupSelectedMemberAdapter() {
        selectedMemberAdapter = SelectedMemberAdapter(
            context = requireContext(),
            onDeleteItem = {
                onDeleteItem(it)
            })

        binding.rvSelectedUser.apply {
            this.adapter = selectedMemberAdapter
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.setHasFixedSize(true)
        }
    }

}*/