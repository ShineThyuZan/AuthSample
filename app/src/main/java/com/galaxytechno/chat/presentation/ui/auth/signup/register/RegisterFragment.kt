package com.galaxytechno.chat.presentation.ui.auth.signup.register

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.galaxytechno.chat.R
import com.galaxytechno.chat.common.InternetChecker
import com.galaxytechno.chat.common.NetworkStatus
import com.galaxytechno.chat.databinding.FragmentSignupBinding
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.galaxytechno.chat.presentation.extension.*
import com.galaxytechno.chat.presentation.single_activity.MainActivity
import com.galaxytechno.chat.presentation.ui.auth.AuthViewModel
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : OtherLvlFragment<FragmentSignupBinding>
    (FragmentSignupBinding::inflate) {
    private val vm: AuthViewModel by activityViewModels()


    @Inject
    lateinit var internetChecker: InternetChecker
    private var internetStatus: Boolean = false

    override fun initialize() {
        super.initialize()
        binding.toolbar.tvToolbarTitle.text = "Sign Up"
        binding.toolbar.ivBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun setupListener() {
        super.setupListener()
        binding.btnRegionSelect.setOnClickListener {
            findNavController().navigate(R.id.action_dest_signup_to_selected_country)
        }

        binding.etIdOrPhone.addTextChangedListener { text ->
            text?.let {
                if(binding.tilIdOrPhone.isErrorEnabled) {
                    binding.tilIdOrPhone.isErrorEnabled = false
                    normalPhoneView()
                }
                binding.tilIdOrPhone.apply {
                    endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                    setEndIconDrawable(R.drawable.ic_close)
                    setEndIconTintList(ContextCompat.getColorStateList(context, R.color.textinputlayout_endicon_normal_state))
                    isEndIconCheckable = true
                    setEndIconOnClickListener {
                        text.clear()
                        this.endIconMode = TextInputLayout.END_ICON_NONE
                    }
                }
            } ?: run {
                binding.tilIdOrPhone.endIconMode = TextInputLayout.END_ICON_NONE
            }
        }

        binding.etSignupName.addTextChangedListener { text ->
            text?.let {
                if(binding.tilSignupName.isErrorEnabled) {
                    binding.tilSignupName.isErrorEnabled = false
                    normalPhoneView()
                }
                binding.tilSignupName.apply {
                    endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                    setEndIconDrawable(R.drawable.ic_close)
                    setEndIconTintList(ContextCompat.getColorStateList(context, R.color.textinputlayout_endicon_normal_state))
                    isEndIconCheckable = true
                    setEndIconOnClickListener {
                        text.clear()
                        this.endIconMode = TextInputLayout.END_ICON_NONE
                    }
                }
            } ?: run {
                binding.tilSignupName.endIconMode = TextInputLayout.END_ICON_NONE
            }
        }

        binding.etPassword.addTextChangedListener { text ->
            text?.let {
                if (binding.tilPassword.isErrorEnabled) {
                    binding.tilPassword.isErrorEnabled = false
                    normalPasswordView()
                }
            }
        }
        binding.etConfirmPassword.addTextChangedListener { text ->
            text?.let {
                if (binding.tilConfirmPassword.isErrorEnabled) {
                    binding.tilConfirmPassword.isErrorEnabled = false
                    normalConfirmPasswordView()
                }
            }
        }

        binding.btnNext.setOnClickListener {
            clearErrorOnFocus()
            if (validate()) {
                if (!internetStatus) {
                    binding.root.displaySnack(getString(R.string.snack_no_internet))
                } else {

                    val phoneNumber = binding.etIdOrPhone.text?.trim().toString()
                    val userName = binding.etSignupName.text?.trim().toString()
                    val password = binding.etPassword.text?.trim().toString()

                    vm.setSelectedName(userName)
                    vm.setSelectedPhone(phoneNumber)
                    vm.setSelectedPassword(password)


                    if (vm.signUpOtpMobile.value != binding.etIdOrPhone.text?.trim().toString()) {
                        /** Call Api request for OTP  */
                        vm.setLoadingState(true)
                        vm.getOtpCode()
                    } else {
                        if (vm.signUpOtpTimber.value == 0) {
                            /** Call Api request for OTP  */
                            vm.setLoadingState(true)
                            vm.getOtpCode()
                        } else {
                            findNavController().navigate(R.id.action_dest_signup_to_dest_signup_verify)
                        }
                    }
                }
            }
        }
    }

    override fun setupView() {
        super.setupView()
        clearErrorOnFocus()
        Timber.tag("Phoooone").d("PhoneNo${vm.selectedPhone.value}")
        binding.etIdOrPhone.setText(vm.selectedPhone.value)
        /** set prefix text view to gravity center */
        val prefixView =
            binding.tilIdOrPhone.findViewById<AppCompatTextView>(com.google.android.material.R.id.textinput_prefix_text)
        prefixView.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        prefixView.gravity = Gravity.CENTER
    }

    private fun validate(): Boolean {

        var errorCount = 0

        if (!binding.etSignupName.isVerifiedName()) {
            binding.tilSignupName.error = getString(R.string.error_register_name)
            errorCount++
        }
        if (vm.selectedCountry.value!!.countryNick.isNullOrEmpty()) {
            errorButtonView()
            errorCount++
        }

        if (binding.etIdOrPhone.text.isNullOrEmpty()) {
            binding.tilIdOrPhone.error = getString(R.string.error_register_phone_empty)
            errorPhoneView()
            errorCount++
        } else {
            if (!binding.etIdOrPhone.isVerifiedPhone()) {
                binding.tilIdOrPhone.error = getString(R.string.error_register_phone)
                errorPhoneView()
                errorCount++
            }
        }

        if (binding.etPassword.text.isNullOrEmpty()) {
            binding.tilPassword.error = getString(R.string.error_register_pwd_empty)
            errorPasswordView()
            errorCount++
        } else {
            if (!binding.etPassword.isVerifiedPwd()) {
                binding.tilPassword.error = getString(R.string.error_register_pwd)
                errorPasswordView()
                errorCount++
            }
        }

        if (binding.etConfirmPassword.text.isNullOrEmpty()) {
            binding.tilConfirmPassword.error = getString(R.string.error_register_confirm_pwd_empty)
            errorConfirmPasswordView()
            errorCount++
        } else {
            if (!binding.etConfirmPassword.isVerifiedPwd()) {
                binding.tilConfirmPassword.error = getString(R.string.error_register_pwd)
                errorConfirmPasswordView()
                errorCount++
            }
        }

        if(binding.etPassword.text!!.isNotEmpty() && binding.etConfirmPassword.text!!.isNotEmpty()) {
            if (!binding.etConfirmPassword.isIdentifiedPwd(
                    binding.etPassword.text?.trim().toString()
                )
            ) {
                binding.tilConfirmPassword.error = getString(R.string.error_register_pwd_confirm)
                errorConfirmPasswordView()
                errorCount++
            }
        }

        return errorCount == 0
    }

    override fun observe() {
        super.observe()

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
        /** loading in navigation */
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            /** use StateFlow with collectLatest EVER */
            vm.isLoading.collectLatest {
                if (it) {
                    (activity as MainActivity).showLoadingDialog(getString(R.string.saving))
                } else {
                    (activity as MainActivity).hideLoadingDialog()
                }
            }
        }

        /** set country obj to text */
            vm.selectedCountry.observe(viewLifecycleOwner) {
                val regionText = it.countryNick.ifEmpty { getString(R.string.region) }
                    normalButtonView()
                    binding.btnRegionSelect.text = regionText
                    binding.tilIdOrPhone.prefixText = it.countryPrefix
            }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            vm.otpResponseEvent.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> TODO()
                    is RemoteEvent.LoadingEvent -> TODO()
                    is RemoteEvent.SuccessEvent -> {
                        vm.setSignUpOtpTimber(1)  //todo add this later it.data!!.data.expireTimeMin current value is for testing purpose
                        Timber.tag("SuccessResponse").d("value${vm.signUpOtpTimber.value}")
                        if(it.data!!.data!!.isRegister) {
                            displayToast(getString(R.string.already_register))
                        } else {
                            findNavController().navigate(R.id.action_dest_signup_to_dest_signup_verify)
                        }
                    }
                }
            }
        }
    }


    private fun clearErrorOnFocus() {
        binding.etSignupName.setOnFocusChangeListener { v, hasFocus ->
            if (v == binding.etSignupName && hasFocus) {
                binding.tilSignupName.isErrorEnabled = !hasFocus
            }
        }

        binding.etIdOrPhone.setOnFocusChangeListener { v, hasFocus ->
            if (v == binding.etIdOrPhone && hasFocus) {
                binding.tilIdOrPhone.isErrorEnabled = !hasFocus
                normalPhoneView()
            }
        }

        binding.etPassword.setOnFocusChangeListener { v, hasFocus ->
            if (v == binding.etPassword && hasFocus) {
                binding.tilPassword.isErrorEnabled = !hasFocus
                normalPasswordView()
            }
        }

        binding.etConfirmPassword.setOnFocusChangeListener { v, hasFocus ->
            if (v == binding.etConfirmPassword && hasFocus) {
                binding.tilConfirmPassword.isErrorEnabled = !hasFocus
                normalConfirmPasswordView()
            }
        }
    }

    private fun normalButtonView() {
        binding.btnRegionSelect.setStrokeColorResource(R.color.colorOnSecondary)
        binding.btnRegionSelect.setTextColor(resources.getColor(R.color.colorOnSelect))
        binding.btnRegionSelect.setIconTintResource(R.color.colorPrimaryContainer)
    }

    private fun errorButtonView() {
        binding.btnRegionSelect.setStrokeColorResource(R.color.colorError)
        binding.btnRegionSelect.setTextColor(resources.getColor(R.color.colorError))
        binding.btnRegionSelect.setIconTintResource(R.color.colorError)
    }

    private fun normalPhoneView() {
        binding.tilIdOrPhone.setBoxStrokeColorStateList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_boxstroke_normal_state)!!)
        binding.tilIdOrPhone.setEndIconTintList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_endicon_normal_state))
    }

    private fun errorPhoneView() {
        binding.tilIdOrPhone.setBoxStrokeColorStateList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_boxstroke_error_state)!!)
        binding.tilIdOrPhone.setEndIconTintList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_endicon_error_state))
    }
    private fun normalPasswordView() {
        binding.tilPassword.setBoxStrokeColorStateList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_boxstroke_normal_state)!!)
        binding.tilPassword.setEndIconTintList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_endicon_normal_state))
    }

    private fun errorPasswordView() {
        binding.tilPassword.setBoxStrokeColorStateList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_boxstroke_error_state)!!)
        binding.tilPassword.setEndIconTintList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_endicon_error_state))
    }
    private fun normalConfirmPasswordView() {
        binding.tilConfirmPassword.setBoxStrokeColorStateList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_boxstroke_normal_state)!!)
        binding.tilConfirmPassword.setEndIconTintList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_endicon_normal_state))
    }

    private fun errorConfirmPasswordView() {
        binding.tilConfirmPassword.setBoxStrokeColorStateList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_boxstroke_error_state)!!)
        binding.tilConfirmPassword.setEndIconTintList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_endicon_error_state))
    }

}