package com.galaxytechno.chat.presentation.ui.auth.pwd_forget

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.galaxytechno.chat.R
import com.galaxytechno.chat.common.InternetChecker
import com.galaxytechno.chat.common.NetworkStatus
import com.galaxytechno.chat.databinding.FragmentPasswordForgetVerifySelectBinding
import com.galaxytechno.chat.presentation.base.AuthFragment
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.presentation.extension.displaySnack
import com.galaxytechno.chat.presentation.single_activity.MainActivity
import com.galaxytechno.chat.presentation.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class PwdForgotVerifySelectFragment : AuthFragment<FragmentPasswordForgetVerifySelectBinding>(
    FragmentPasswordForgetVerifySelectBinding::inflate
) {

    private val viewModel: AuthViewModel by activityViewModels()
    private val args: PwdForgotVerifySelectFragmentArgs by navArgs()

    override fun initialize() {
        super.initialize()
        (activity as MainActivity).shouldShowToolbar(false)
    }

    @Inject
    lateinit var internetChecker: InternetChecker
    private var internetStatus: Boolean = false

    override fun setupListener() {
        super.setupListener()
        binding.tbPwdVerifySelect.ivBackArrow.setOnClickListener {
            findNavController().popBackStack()
            binding.root.displaySnack("Back Going")
        }
        binding.btnNext.setOnClickListener {
            if (validate()) {
                if (!internetStatus) {
                    binding.root.displaySnack(getString(R.string.snack_no_internet))
                } else {
                    if (binding.rbOtp.isChecked) {
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
                        Timber.tag("pass").d("${viewModel.forgotPwdOtpMobile.value+""+viewModel.mobile.value}")

                    } else {
                        viewModel.getConfirmedQuestions()
                        findNavController().navigate(R.id.action_dest_pwd_forget_verify_select_to_dest_forget_pwd_question)
                    }
                }

            } else {
                binding.root.displaySnack("Please select a method")
            }

        }
    }

    override fun setupView() {
        super.setupView()
        binding.tvName.text = viewModel.selectedAccountName.value ?: ""
        Glide.with(this)
            .load(viewModel.selectedAccountUrl.value ?: "")
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(binding.ivForgotPwdProfile)
        binding.tbPwdVerifySelect.tvToolbarTitle.text = getString(R.string.forget_pwd)
    }

    override fun observe() {
        super.observe()

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


        internetChecker.observe(viewLifecycleOwner) {
            internetStatus = when (it) {
                NetworkStatus.Available -> {
                    true
                }
                NetworkStatus.UnAvailable -> {
                    false
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.otpResponseEvent.collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> TODO()
                    is RemoteEvent.LoadingEvent -> TODO()
                    is RemoteEvent.SuccessEvent -> {
                        viewModel.setForgotPwdOtpTimber(1)  //todo add this later it.data!!.data.expireTimeMin current value is for testing purpose
                        viewModel.setMobileNumber(args.phone.trim())
                        viewModel.setForgotPwdOtpMobileNumber(args.phone.trim())
                        navigateToVerifyOtp()
                    }
                }
            }
        }
    }

    private fun validate(): Boolean {
        return binding.rbOtp.isChecked or binding.rbQuestion.isChecked
    }

    private fun navigateToVerifyOtp() {
        val directions =
            PwdForgotVerifySelectFragmentDirections.actionDestPwdForgetVerifySelectToDestPwdForgetVerifyOtp(
                viewModel.mobile.value.toString()
            )
        findNavController().navigate(directions)
    }

}