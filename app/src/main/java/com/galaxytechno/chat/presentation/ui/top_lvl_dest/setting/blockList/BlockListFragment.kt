package com.galaxytechno.chat.presentation.ui.top_lvl_dest.setting.blockList

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.galaxytechno.chat.R
import com.galaxytechno.chat.common.Constant
import com.galaxytechno.chat.databinding.FragmentBlockListBinding
import com.galaxytechno.chat.model.vos.FriListVos
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.presentation.extension.displayToast
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.setting.TopSettingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class BlockListFragment :
    OtherLvlFragment<FragmentBlockListBinding>(FragmentBlockListBinding::inflate) {

    private val viewModel: TopSettingViewModel by activityViewModels()
    private var offset = 0
    private var firstTime = true
    private var isLoading = false
    private lateinit var blockListAdapter: BlockListAdapter
    private var blockList: MutableList<FriListVos> = mutableListOf()
    private lateinit var dialog: Dialog
    private val rvView by lazy {
        requireActivity().findViewById<RecyclerView>(R.id.rvBlockList)
    }

    private fun customUnblockDialog(friendId: Long) {
        dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_unblock)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        /** animate dialog with window animation */
        dialog.window!!.setWindowAnimations(R.style.AnimationForDialog)
        dialog.setCanceledOnTouchOutside(false)
        val unblock: Button = dialog.findViewById(R.id.btn_unblock) as Button
        val cancel: Button = dialog.findViewById(R.id.btn_cancel) as Button

        unblock.setOnClickListener {
            viewModel.blockRequest(friendId)
        }
        cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun shouldShowShimmer(flag: Boolean) {
        if (flag) {
            binding.shimmerLayout.visibility = View.VISIBLE
            binding.rvBlockList.visibility = View.GONE
            binding.shimmerLayout.startShimmer()
        } else {
            binding.shimmerLayout.visibility = View.GONE
            binding.rvBlockList.visibility = View.VISIBLE
            binding.shimmerLayout.stopShimmer()
        }
    }

    override fun setupView() {
        super.setupView()
        shouldShowShimmer(true)
        binding.toolbarBlock.tvToolbarTitle.text = getString(R.string.block)
        isLoading = true
        firstTime = true
        viewModel.getBlockListRequest(offset)

        blockListAdapter = BlockListAdapter(requireContext(), object : BlockListDelegate {
            override fun onCLickUnblock(data: FriListVos) {
                customUnblockDialog(data.friendId!!)
            }

            override fun onClickedName(data: FriListVos) {
                viewModel.setFriendData(data)
//                val directions =
//                    BlockListFragmentDirections.actionBlockListFragmentToBlockProfileFragment(
//                        data.friendName!!, data.headUrl!!, data.friendId.toString()
//                    )
//                findNavController().navigate(directions)
                findNavController().navigate(R.id.action_blockListFragment_to_blockProfileFragment)
            }

            override fun loadMore() {
                viewModel.addOffset()
                isLoading = true
                blockListAdapter.isLoading(isLoading)
            }
        })

        rvView.adapter = blockListAdapter
        rvView.layoutManager =
            LinearLayoutManager(context)
        blockListAdapter.clearData()
        blockListAdapter.isLoading(isLoading)
    }

    override fun setupListener() {
        super.setupListener()
        binding.toolbarBlock.ivBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun observe() {
        super.observe()
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.requestBlockList.collectLatest {
                when (it) {
                    is RemoteEvent.LoadingEvent -> {
                        Unit
                    }
                    is RemoteEvent.ErrorEvent -> {
                        //show error
                    }

                    is RemoteEvent.SuccessEvent -> {
                        delay(500)
                        shouldShowShimmer(false)

                        if (it.data!!.data.friendList.isNullOrEmpty()) {
                            binding.rvBlockList.visibility = View.GONE
                            binding.constBlockImg.visibility = View.VISIBLE
                        } else {
                            isLoading = false
                            blockListAdapter.isLoading(isLoading)
                            if (it.data.data.friendList.isEmpty()) {

                                blockListAdapter.isLastPage(true)
                                if (firstTime) {
                                    isLoading = true
                                    blockListAdapter.isLoading(isLoading)
                                }
                            } else {
                                firstTime = false
                                if (it.data.data.friendList.size < 30) {
                                    blockListAdapter.isLastPage(true)
                                } else {
                                    blockListAdapter.isLastPage(false)
                                }
                                blockList = it.data.data.friendList as MutableList<FriListVos>
                                blockListAdapter.setNewData(blockList)
                            }
                        }
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.blockResponse.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {}
                    is RemoteEvent.LoadingEvent -> {}
                    is RemoteEvent.SuccessEvent -> {
                        if (it.data!!.status == Constant.STATUS_SUCCESS) {
                            dialog.dismiss()
                            displayToast("Unblock Successful")
                            viewModel.getBlockListRequest(offset)
                        }
                    }
                }
            }
        }


    }
}