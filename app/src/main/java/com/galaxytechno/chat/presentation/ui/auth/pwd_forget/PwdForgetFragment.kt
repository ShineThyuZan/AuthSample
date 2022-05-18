package com.galaxytechno.chat.presentation.ui.auth.pwd_forget

import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.galaxytechno.chat.R
import com.galaxytechno.chat.common.Constant
import com.galaxytechno.chat.databinding.FragmentPasswordForgetBinding
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.presentation.extension.isVerifiedPhone
import com.galaxytechno.chat.presentation.single_activity.MainActivity
import com.galaxytechno.chat.presentation.ui.auth.AuthViewModel
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class PwdForgetFragment : OtherLvlFragment<FragmentPasswordForgetBinding>(
    FragmentPasswordForgetBinding::inflate
) {
    private val viewModel: AuthViewModel by activityViewModels()
    private val args: PwdForgetFragmentArgs by navArgs()

    override fun setupView() {
        super.setupView()
        clearErrorOnFocus()
        binding.toolbarForgetPwd.tvToolbarTitle.text = getString(R.string.froget_password)
        binding.etPhone.setText(args.phone)
        binding.toolbarForgetPwd.ivBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        /** setUp prefix text view to gravity center */
        val prefixView =
            binding.tilPhone.findViewById<AppCompatTextView>(com.google.android.material.R.id.textinput_prefix_text)
        prefixView.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        prefixView.gravity = Gravity.CENTER

    }

    override fun setupListener() {
        super.setupListener()
        binding.btnRegionSelect.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_selected_country)
        }

        binding.btnForgetPwdContinue.setOnClickListener {
            /** call user profile api to check two factor validation ,
             * have associated account with phone no
             */
            clearErrorOnFocus()
            if (validate()) {
                viewModel.setLoadingState(true)
                viewModel.setSelectedPhone(binding.etPhone.text!!.trim().toString())
                viewModel.checkAccountStatus()
            }

        }

        binding.etPhone.addTextChangedListener { text ->
            text?.let {
                if (binding.tilPhone.isErrorEnabled) {
                    binding.tilPhone.isErrorEnabled = false
                    normalPhoneView()
                }
                binding.tilPhone.apply {
                    endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                    setEndIconDrawable(R.drawable.ic_close)
                    isEndIconCheckable = true
                    setEndIconOnClickListener {
                        text.clear()
                        this.endIconMode = TextInputLayout.END_ICON_NONE
                    }
                }
            } ?: run {
                binding.tilPhone.endIconMode = TextInputLayout.END_ICON_NONE
            }
        }
    }

    private fun validate(): Boolean {
        var errorCount = 0

        if (viewModel.selectedCountry.value!!.countryNick.isNullOrEmpty()) {
            errorButtonView()
            errorCount++
        }
        if (binding.etPhone.text.isNullOrEmpty()) {
            binding.tilPhone.error = getString(R.string.error_register_phone_empty)
            errorPhoneView()
            errorCount++
        } else {
            if (!binding.etPhone.isVerifiedPhone()) {
                binding.tilPhone.error = getString(R.string.error_register_phone)
                errorPhoneView()
                errorCount++
            }
        }
        return errorCount == 0 //return true if errorCount is 0 else return false
    }

    private fun clearErrorOnFocus() {
        binding.etPhone.setOnFocusChangeListener { v, hasFocus ->
            if (v == binding.etPhone && hasFocus)
                binding.tilPhone.isErrorEnabled = !hasFocus
            normalPhoneView()
        }
    }

    private fun navigateToVerifySelect() {
        val directions =
            PwdForgetFragmentDirections.actionDestPwdForgetToPwdForgetVerifySelectFragment(
                binding.etPhone.text!!.trim().toString()
            )
        findNavController().navigate(directions)
    }

    private fun navigateToTwoFactor() {
        val directions =
            PwdForgetFragmentDirections.actionForgetPwdToTwoFactor(
                binding.etPhone.text!!.trim().toString()
            )
        findNavController().navigate(directions)
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

        viewModel.selectedCountry.observe(viewLifecycleOwner) {
            val regionText = it.countryNick.ifEmpty { getString(R.string.region) }
            normalButtonView()
            binding.btnRegionSelect.text = regionText
            binding.tilPhone.prefixText = it.countryPrefix
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.checkAccountStatus.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {}
                    is RemoteEvent.LoadingEvent -> {}
                    is RemoteEvent.SuccessEvent -> {
                        when (it.data!!.status) {
                            /** nav have no associated account with mobile Number */
                            Constant.STATUS_FAIL -> {
                                val directions =
                                    PwdForgetFragmentDirections.actionPwdForgetToNoAccountFound(
                                        binding.etPhone.text!!.trim().toString()
                                    )
                                findNavController().navigate(directions)
                            }
                            Constant.STATUS_SUCCESS -> {
                                val twoFactor =
                                    async { viewModel.setTwoFactorState(it.data.data!!.twoFactor) }
                                val mobile = async {
                                    viewModel.setMobileNumber(
                                        binding.etPhone.text!!.trim().toString()
                                    )
                                }
                                twoFactor.await()
                                mobile.await()
                                if (!it.data.data!!.twoFactor) {
                                    /** user have no two factor state */
                                    Timber.tag("selected").d("select state")
                                    navigateToVerifySelect()
                                } else {
                                    navigateToTwoFactor()
                                }
                            }
                        }

                    }
                }

            }
        }

    }

    private fun errorButtonView() {
        binding.btnRegionSelect.setStrokeColorResource(R.color.colorError)
        binding.btnRegionSelect.setTextColor(resources.getColor(R.color.colorError))
        binding.btnRegionSelect.setIconTintResource(R.color.colorError)
    }

    private fun normalButtonView() {
        binding.btnRegionSelect.setStrokeColorResource(R.color.colorOnSecondary)
        binding.btnRegionSelect.setTextColor(resources.getColor(R.color.colorOnSelect))
        binding.btnRegionSelect.setIconTintResource(R.color.colorPrimaryContainer)
    }

    private fun normalPhoneView() {
        binding.tilPhone.setBoxStrokeColorStateList(
            ContextCompat.getColorStateList(
                this.requireContext(),
                R.color.textinputlayout_boxstroke_normal_state
            )!!
        )
        binding.tilPhone.setEndIconTintList(
            ContextCompat.getColorStateList(
                this.requireContext(),
                R.color.textinputlayout_endicon_normal_state
            )
        )
    }

    private fun errorPhoneView() {
        binding.tilPhone.setBoxStrokeColorStateList(
            ContextCompat.getColorStateList(
                this.requireContext(),
                R.color.textinputlayout_boxstroke_error_state
            )!!
        )
        binding.tilPhone.setEndIconTintList(
            ContextCompat.getColorStateList(
                this.requireContext(),
                R.color.textinputlayout_endicon_error_state
            )
        )
    }

}
