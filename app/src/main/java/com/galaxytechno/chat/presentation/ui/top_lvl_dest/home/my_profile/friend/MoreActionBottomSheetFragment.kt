package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.my_profile.friend

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.galaxytechno.chat.R
import com.galaxytechno.chat.common.Constant
import com.galaxytechno.chat.databinding.BottomSheetMoreActionProfileBinding
import com.galaxytechno.chat.presentation.base.BaseBottomSheet
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.presentation.extension.displayToast
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.my_profile.MyProfileViewModel
import kotlinx.coroutines.flow.collectLatest

class MoreActionBottomSheetFragment : BaseBottomSheet<BottomSheetMoreActionProfileBinding>(
    BottomSheetMoreActionProfileBinding::inflate
) {

    val vm: MyProfileViewModel by activityViewModels()

    override fun setupListener() {
        super.setupListener()

        binding.tvCopyProfileLink.setOnClickListener {
            displayToast("copy profile link")
            findNavController().popBackStack()
        }

        binding.tvReportProfile.setOnClickListener {
            displayToast("report profile")
            findNavController().popBackStack()
        }

        binding.tvBlockUser.setOnClickListener {
            displayToast("Block user")
            showConfirmDialog()
        }
    }

    override fun setupView() {
        super.setupView()
    }

    override fun observe() {
        super.observe()

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                vm.blockResponse.collectLatest {
                    when (it) {
                        is RemoteEvent.ErrorEvent -> TODO()
                        is RemoteEvent.LoadingEvent -> TODO()
                        is RemoteEvent.SuccessEvent -> {
                            if (it.data!!.status == Constant.STATUS_SUCCESS) {
                                findNavController().navigate(R.id.action_moreActionBottomSheetFragmentFriend_to_myProfileFriendFragment)
                            }
                        }
                    }
                }
            }
    }

    /** block confirm dialog */
    private fun showConfirmDialog() {
        val confirmBtn: Button
        val cancelBtn: Button
        val titleTextView: TextView
        val warningTextView: TextView
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_confirmation)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        /** animate dialog with window animation */
        dialog.window!!.setWindowAnimations(R.style.AnimationForDialog)
        dialog.setCanceledOnTouchOutside(false)
        titleTextView = dialog.findViewById(R.id.tv_title)
        warningTextView = dialog.findViewById(R.id.tv_warning)
        titleTextView.text = getString(R.string.block_user_title)
        warningTextView.text = getString(R.string.block_warning)
        confirmBtn = dialog.findViewById(R.id.btn_confirm) as Button
        cancelBtn = dialog.findViewById(R.id.btn_cancel) as Button

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        confirmBtn.setOnClickListener {
            vm.blockRequest()
            dialog.dismiss()
        }
        dialog.show()
    }
}