package com.galaxytechno.chat.presentation.ui.top_lvl_dest.setting

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.galaxytechno.chat.R
import com.galaxytechno.chat.common.InternetChecker
import com.galaxytechno.chat.databinding.FragmentTopProfileSettingBinding
import com.galaxytechno.chat.presentation.base.TopFragment
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.presentation.extension.displayToast
import com.galaxytechno.chat.presentation.single_activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class TopSettingFragment :
    TopFragment<FragmentTopProfileSettingBinding>(FragmentTopProfileSettingBinding::inflate) {

    @Inject
    lateinit var internetChecker: InternetChecker
    private val viewModel: TopSettingViewModel by activityViewModels()
    private lateinit var dialog: Dialog

    override fun initialize() {
        super.initialize()
        binding.toolbarHome.tvToolbarTitle.text = getString(R.string.setting)
        binding.tvLogout.isEnabled = MainActivity.isInternetAvailable.value!!
    }

    override fun setupListener() {
        super.setupListener()
        onHandleClicks()
    }

    /** for click event for setting feature */
    private fun onHandleClicks() {
        binding.tvPrivacyAndSecurity.setOnClickListener {
            findNavController().navigate(R.id.action_dest_top_setting_to_settingPrivacyFragment)
        }
        binding.tvAccSecurity.setOnClickListener {
            (activity as MainActivity).comingSoonDialog()
        }
        binding.tvNotification.setOnClickListener {
            (activity as MainActivity).comingSoonDialog()
        }
        binding.tvAppearance.setOnClickListener {
            (activity as MainActivity).comingSoonDialog()
        }
        binding.tvHelpAndSupport.setOnClickListener {
            (activity as MainActivity).comingSoonDialog()
        }
        binding.tvAbout.setOnClickListener {
            (activity as MainActivity).comingSoonDialog()
        }

        binding.tvLogout.setOnClickListener {
            logoutConfirmationDialog()
        }

//        binding.toolbarSetting.ivSearch.setOnClickListener {
//            (activity as MainActivity).comingSoonDialog()
//        }
    }

    private fun logoutConfirmationDialog() {
        dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_logout_confirmation)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        /** animate dialog with window animation */
        dialog.window!!.setWindowAnimations(R.style.AnimationForDialog)
        dialog.setCanceledOnTouchOutside(false)
        val btnLogout: Button = dialog.findViewById(R.id.btn_logout_confirmation) as Button
        val cancel: Button = dialog.findViewById(R.id.btn_cancel) as Button
        btnLogout.setOnClickListener {
            /** request form server to logout*/
            if (MainActivity.isInternetAvailable.value == true) {
                viewModel.setLoadingState(true)
                viewModel.requestLogout()
                dialog.dismiss()
            } else {
                displayToast("Please check internet connection")
            }
        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun observe() {
        super.observe()
        /** internet checking state  */
//        internetChecker.observe(viewLifecycleOwner) {
//            when (it) {
//                NetworkStatus.Available -> {
//
//                }
//                NetworkStatus.UnAvailable -> {
//                    binding.tvLogout.isEnabled = false
//                }
//            }
//        }

        /** loading in navigation */
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            /** use StateFlow with collectLatest EVER */
            viewModel.isLoading.collectLatest {
                if (it) {
                    (activity as MainActivity).showLoadingDialog(getString(R.string.loading))
                } else {
                    (activity as MainActivity).hideLoadingDialog()
                }
            }
        }

        /** getting event form server for logout */
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.logoutEvent.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {}
                    is RemoteEvent.LoadingEvent -> {}
                    is RemoteEvent.SuccessEvent -> {
                        /** clear db and DS form android section  and nav to login screen */
                        findNavController().navigate(R.id.action_logout_to_auth_navigation)
                    }
                }
            }
        }

    }

}