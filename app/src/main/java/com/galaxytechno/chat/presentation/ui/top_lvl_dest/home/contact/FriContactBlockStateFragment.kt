package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.contact

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.galaxytechno.chat.R
import com.galaxytechno.chat.common.Constant
import com.galaxytechno.chat.databinding.FragmentFriContactBlockStateBinding
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.presentation.extension.displayToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FriContactBlockStateFragment :
    OtherLvlFragment<FragmentFriContactBlockStateBinding>(FragmentFriContactBlockStateBinding::inflate) {

    private val args: FriContactBlockStateFragmentArgs by navArgs()
    private val vm: FriContactViewModel by activityViewModels()
    private lateinit var dialog: Dialog

    override fun initialize() {
        super.initialize()
        vm.contactProfileDetail()
    }

    override fun setupView() {
        super.setupView()

        binding.ivBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        when (args.state) {
            FriContactFragment.IS_BEING_BLOCK -> {
                binding.constBlockToYou.visibility = View.VISIBLE
                binding.constBlockToUser.visibility = View.GONE

            }
            FriContactFragment.IS_BLOCK_STATE -> {
                binding.constBlockToUser.visibility = View.VISIBLE
                binding.constBlockToYou.visibility = View.GONE
                binding.btnUnblockUser.setOnClickListener {
                    customUnblockDialog()
                }

            }
            else -> {
                Unit
            }
        }
    }

    private fun customUnblockDialog() {
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
            vm.blockRequest(false)
        }
        cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun observe() {
        super.observe()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vm.contactProfileResponse.collectLatest {
                binding.tvBlockUserName.text = it.data!!.data!!.name
                Glide.with(requireActivity())
                    .load(it.data.data!!.headUrl)
                    .into(binding.ivBlockUserProfile)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            vm.blockResponse.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {}
                    is RemoteEvent.LoadingEvent -> {}
                    is RemoteEvent.SuccessEvent -> {
                        if (it.data!!.status == Constant.STATUS_SUCCESS) {
                            dialog.dismiss()
                            displayToast("Unblock Successful")
                            delay(2000)
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }
    }
}