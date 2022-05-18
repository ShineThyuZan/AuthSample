package com.galaxytechno.chat.presentation.ui.auth.pwd_forget

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.galaxytechno.chat.R
import com.galaxytechno.chat.databinding.FragmentForgetPwdTwoFactorBinding
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.presentation.single_activity.MainActivity
import com.galaxytechno.chat.presentation.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class ForgetPwdTwoFactorFragment :
    OtherLvlFragment<FragmentForgetPwdTwoFactorBinding>(FragmentForgetPwdTwoFactorBinding::inflate) {
    private val viewModel: AuthViewModel by activityViewModels()
    private val args: ForgetPwdTwoFactorFragmentArgs by navArgs()


    override fun setupView() {
        super.setupView()
        binding.toolbarForgetPwd.tvToolbarTitle.text = "Forget Password"
        binding.tvProfileName.text = viewModel.selectedAccountName.value ?: ""
        Glide.with(this)
            .load(viewModel.selectedAccountUrl.value ?: "")
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(binding.ivProfileImage)
    }

    override fun setupListener() {
        super.setupListener()
        binding.toolbarForgetPwd.ivBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnNotYou.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnContinue.setOnClickListener {

            if(viewModel.forgotPwdOtpMobile.value != viewModel.mobile.value) {
                viewModel.setLoadingState(true)
                viewModel.getOtpCode()
            } else {
                if (viewModel.forgotPwdOtpTimber.value == 0) {
                    viewModel.setLoadingState(true)
                    viewModel.getOtpCode()
                } else {
                    navigateToVerifyOtp()
                }
            }
            Timber.tag("Comparrrr").d("${viewModel.forgotPwdOtpMobile.value+""+viewModel.mobile.value}")


    }
    }

    override fun observe() {
        super.observe()
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.otpResponseEvent.collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> TODO()
                    is RemoteEvent.LoadingEvent -> TODO()
                    is RemoteEvent.SuccessEvent -> {
                        viewModel.setForgotPwdOtpTimber(1)  //todo add this later it.data!!.data.expireTimeMin current value is for testing purpose
                        viewModel.setMobileNumber(viewModel.mobile.value!!)
                        viewModel.setForgotPwdOtpMobileNumber(viewModel.mobile.value!!)
                        Timber.tag("Moooobile").d("nois${viewModel.forgotPwdOtpMobile.value}")
                        navigateToVerifyOtp()

                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            //use StateFlow with collectLatest EVER
            viewModel.isLoading.collectLatest {
                if (it) {
                    (activity as MainActivity).showLoadingDialog(getString(R.string.loading))
                } else {
                    (activity as MainActivity).hideLoadingDialog()
                }
            }
        }
    }

    private fun navigateToVerifyOtp() {
        findNavController()
            .navigate(R.id.action_forget_pwd_two_factor_to_dest_pwd_forget_verify_otp)
    }
}
