package com.galaxytechno.chat.presentation.ui.top_lvl_dest.setting.blockList

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Button
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.galaxytechno.chat.R
import com.galaxytechno.chat.common.Constant
import com.galaxytechno.chat.databinding.FragmentBlockProfileBinding
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.presentation.extension.displayToast
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.setting.TopSettingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class BlockProfileFragment :
    OtherLvlFragment<FragmentBlockProfileBinding>(FragmentBlockProfileBinding::inflate) {

    // private val args: BlockProfileFragmentArgs by navArgs()
    private val viewModel: TopSettingViewModel by activityViewModels()
    private lateinit var dialog: Dialog


    override fun setupListener() {
        super.setupListener()
        binding.ivBackArrow.setOnClickListener {
            val authNavOptions = NavOptions.Builder()
                .setPopUpTo(R.id.blockListFragment, true)
                .build()
            findNavController().navigate(
                R.id.action_blockProfileFragment_to_blockListFragment,
                Bundle(),
                authNavOptions
            )
        }
        binding.btnUnblockUser.setOnClickListener {
            customUnblockDialog(viewModel.friendListVos.value!!.friendId!!.toLong())
        }

    }

    override fun setupView() {
        super.setupView()
        binding.tvBlockUserName.text = viewModel.friendListVos.value!!.friendName

        viewModel.friendListVos.value!!.headUrl.let {
            Glide.with(requireActivity())
                .load(viewModel.friendListVos.value!!.headUrl)
                .placeholder(R.drawable.ic_profile)
                .into(binding.ivBlockUserProfile)
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val authNavOptions = NavOptions.Builder()
                .setPopUpTo(R.id.blockListFragment, true)
                .build()
            findNavController().navigate(
                R.id.action_blockProfileFragment_to_blockListFragment,
                Bundle(),
                authNavOptions
            )
        }
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

    override fun observe() {
        super.observe()
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.blockResponse.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {}
                    is RemoteEvent.LoadingEvent -> {}
                    is RemoteEvent.SuccessEvent -> {
                        if (it.data!!.status == Constant.STATUS_SUCCESS) {
                            dialog.dismiss()
                            displayToast("Unblock Successful")
                            val authNavOptions = NavOptions.Builder()
                                .setPopUpTo(R.id.blockListFragment, true)
                                .build()
                            findNavController().navigate(
                                R.id.action_blockProfileFragment_to_blockListFragment,
                                Bundle(),
                                authNavOptions
                            )
                        }
                    }
                }
            }
        }

    }


}