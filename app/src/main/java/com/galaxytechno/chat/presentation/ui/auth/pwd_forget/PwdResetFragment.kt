package com.galaxytechno.chat.presentation.ui.auth.pwd_forget

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.galaxytechno.chat.R
import com.galaxytechno.chat.databinding.FragmentPasswordResetBinding
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.presentation.extension.displayToast
import com.galaxytechno.chat.presentation.extension.isIdentifiedPwd
import com.galaxytechno.chat.presentation.extension.isVerifiedPwd
import com.galaxytechno.chat.presentation.single_activity.MainActivity
import com.galaxytechno.chat.presentation.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class PwdResetFragment :
    OtherLvlFragment<FragmentPasswordResetBinding>(FragmentPasswordResetBinding::inflate) {
    private val viewModel: AuthViewModel by activityViewModels()

    override fun setupView() {
        super.setupView()
        clearErrorOnFocus()
    }

    override fun setupListener() {
        super.setupListener()

        binding.toolbarForgetPwdConfirm.tvToolbarTitle.text =
            getString(R.string.toolbar_forget_password)
        binding.toolbarForgetPwdConfirm.ivBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnChange.setOnClickListener {
            if(validate()) {
                clearErrorOnFocus()
                viewModel.setLoadingState(true)
                viewModel.resetPassword(
                    newPassword = binding.etPwdNew.text!!.trim().toString(),
                    confirmPassword = binding.etPwdConfirm.text!!.trim().toString()
                )
            }
        }

        binding.etPwdNew.addTextChangedListener { text ->
            text?.let {
                if (binding.tilPwdNew.isErrorEnabled) {
                    binding.tilPwdNew.isErrorEnabled = false
                    normalPasswordView()
                }
            }
        }
        binding.etPwdConfirm.addTextChangedListener { text ->
            text?.let {
                if (binding.tilPwdConfirm.isErrorEnabled) {
                    binding.tilPwdConfirm.isErrorEnabled = false
                    normalConfirmPasswordView()
                }
            }
        }
    }

    override fun observe() {
        super.observe()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            //use StateFlow with collectLatest EVER
            viewModel.isLoading.collectLatest {
                if (it) {
                    (activity as MainActivity).showLoadingDialog(getString(R.string.updating))
                } else {
                    (activity as MainActivity).hideLoadingDialog()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.forgetPwdChangeEvent.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        displayToast(it.message.toString())
                    }
                    is RemoteEvent.LoadingEvent -> {
                        displayToast(it.message.toString())
                    }
                    is RemoteEvent.SuccessEvent -> {
                        val authNavOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.dest_login, true)
                            .build()
                        findNavController().navigate(
                            R.id.action_dest_pwd_reset_to_pwdRestSuccessfulFragment,
                            Bundle(),
                            authNavOptions
                        )
                    }
                }
            }
        }
    }

    private fun validate(): Boolean {
        var errorCount = 0
        if (binding.etPwdNew.text.isNullOrEmpty()) {
            binding.tilPwdNew.error = getString(R.string.error_register_pwd_empty)
            errorPasswordView()
            errorCount++
        } else {
            if (!binding.etPwdNew.isVerifiedPwd()) {
                binding.tilPwdNew.error = getString(R.string.error_register_pwd)
                errorPasswordView()
                errorCount++
            }
        }

        if (binding.etPwdConfirm.text.isNullOrEmpty()) {
            binding.tilPwdConfirm.error = getString(R.string.error_register_confirm_pwd_empty)
            errorConfirmPasswordView()
            errorCount++
        } else {
            if (!binding.etPwdConfirm.isVerifiedPwd()) {
                binding.tilPwdConfirm.error = getString(R.string.error_register_pwd)
                errorConfirmPasswordView()
                errorCount++
            }
        }

        if(binding.etPwdNew.text!!.isNotEmpty() && binding.etPwdConfirm.text!!.isNotEmpty()) {
            if (!binding.etPwdConfirm.isIdentifiedPwd(
                    binding.etPwdNew.text?.trim().toString()
                )
            ) {
                binding.tilPwdConfirm.error = getString(R.string.error_register_pwd_confirm)
                errorConfirmPasswordView()
                errorCount++
            }
        }
        return errorCount == 0
    }

    private fun clearErrorOnFocus() {

        binding.etPwdNew.setOnFocusChangeListener { v, hasFocus ->
            if (v == binding.etPwdNew && hasFocus) {
                binding.tilPwdNew.isErrorEnabled = !hasFocus
                normalPasswordView()
            }
        }

        binding.etPwdConfirm.setOnFocusChangeListener { v, hasFocus ->
            if (v == binding.etPwdConfirm && hasFocus) {
                binding.tilPwdConfirm.isErrorEnabled = !hasFocus
                normalConfirmPasswordView()
            }
        }
    }

    private fun normalPasswordView() {
        binding.tilPwdNew.setBoxStrokeColorStateList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_boxstroke_normal_state)!!)
        binding.tilPwdNew.setEndIconTintList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_endicon_normal_state))
    }

    private fun errorPasswordView() {
        binding.tilPwdNew.setBoxStrokeColorStateList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_boxstroke_error_state)!!)
        binding.tilPwdNew.setEndIconTintList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_endicon_error_state))
    }
    private fun normalConfirmPasswordView() {
        binding.tilPwdConfirm.setBoxStrokeColorStateList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_boxstroke_normal_state)!!)
        binding.tilPwdConfirm.setEndIconTintList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_endicon_normal_state))
    }

    private fun errorConfirmPasswordView() {
        binding.tilPwdConfirm.setBoxStrokeColorStateList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_boxstroke_error_state)!!)
        binding.tilPwdConfirm.setEndIconTintList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_endicon_error_state))
    }

}