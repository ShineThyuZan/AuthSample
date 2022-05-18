package com.galaxytechno.chat.presentation.ui.auth.login

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.addCallback
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.galaxytechno.chat.R
import com.galaxytechno.chat.common.Constant
import com.galaxytechno.chat.databinding.FragmentLoginBinding
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.galaxytechno.chat.presentation.extension.*
import com.galaxytechno.chat.presentation.single_activity.MainActivity
import com.galaxytechno.chat.presentation.ui.auth.AuthViewModel
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import java.util.*


@AndroidEntryPoint
class LoginFragment : OtherLvlFragment<FragmentLoginBinding>(
    FragmentLoginBinding::inflate
) {
    private val vm: AuthViewModel by activityViewModels()
    private var isMobile: Boolean = false

    override fun initialize() {
        super.initialize()
        (activity as MainActivity).shouldShowToolbar(false)
        val manager =
            requireContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        isMobile = Objects.requireNonNull(manager).phoneType != TelephonyManager.PHONE_TYPE_NONE

    }

    override fun setupView() {
        super.setupView()

        /** prefixText to vertical alignment */
        val prefixView =
            binding.tilPhone.findViewById<AppCompatTextView>(com.google.android.material.R.id.textinput_prefix_text)
        prefixView.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        prefixView.gravity = Gravity.CENTER

        clearErrorOnFocus()

        /** key action on back press */
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val authNavOptions = NavOptions.Builder()
                .setPopUpTo(R.id.initLangSelectFragment, true)
                .build()
            findNavController().navigate(
                R.id.action_login_to_init_lang_navigation,
                Bundle(),
                authNavOptions
            )
        }
    }


    override fun setupListener() {
        super.setupListener()

        /** go to bottom sheet ( choose Country ) */
        binding.btnRegionSelect.setOnClickListener {
            findNavController().navigate(R.id.action_dest_login_to_selected_country)
        }

        /** do login */
        binding.btnLogin.setOnClickListener {
            clearErrorOnFocus()
            hideKeyBoard()
            if (validate()) {
                if (MainActivity.isInternetAvailable.value == false) {
                    shownNoInternetDialog()
                } else {
                    /** login api call */
                    vm.setLoadingState(true)
                    vm.setSelectedPhone(binding.etPhone.text?.trim().toString())
                    vm.requestMobileLogin(
                        phone = binding.etPhone.text?.trim().toString(),
                        password = binding.etPassword.text?.trim().toString(),
                    )
                }
            }
        }

        /** go to signup */
        binding.btnLoginToSignIn.setOnClickListener {
            vm.setSelectedPhone(binding.etPhone.text?.trim().toString())
            findNavController().navigate(R.id.action_dest_login_to_signupFragment)
        }

        /** go to password forget */
        binding.tvForgetPassword.setOnClickListener {
            val directions =
                LoginFragmentDirections.actionLoginToPwdForget(
                    binding.etPhone.text.toString()
                )
            findNavController().navigate(directions)
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

        binding.etPassword.addTextChangedListener { text ->
            text?.let {
                if (binding.tilPassword.isErrorEnabled) {
                    binding.tilPassword.isErrorEnabled = false
                    normalPasswordView()
                }
            }
        }

    }

    override fun observe() {
        super.observe()

        /** just set text form country obj */
        vm.selectedCountry.observe(viewLifecycleOwner) {
            val regionText = it.countryNick.ifEmpty { getString(R.string.region) }
            binding.btnRegionSelect.text = regionText
            binding.tilPhone.prefixText = it.countryPrefix
            normalButtonView()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            /** use StateFlow with collectLatest EVER */
            vm.isLoading.collectLatest {
                if (it) {
                    (activity as MainActivity).showLoadingDialog(getString(R.string.dialog_login))
                } else {
                    (activity as MainActivity).hideLoadingDialog()
                }
            }
        }

        /** observing login data and nav to Home */
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vm.loginEvent.collectLatest {
                when (it) {
                    is RemoteEvent.LoadingEvent -> {

                    }
                    is RemoteEvent.ErrorEvent -> {

                        binding.root.displaySnack(
                            it.message ?: getString(R.string.snack_request_login_fail)
                        )
                    }
                    is RemoteEvent.SuccessEvent ->
                        when (it.data!!.status) {
                            Constant.STATUS_SUCCESS -> {

                                binding.btnLogin.setActive(false)
                                binding.root.displaySnack(getString(R.string.snack_request_login_success))
                                navigateToHome()
                            }
                            Constant.STATUS_FAIL -> {
                                displayToast(it.data.error ?: "Error")
                            }
                        }
                }
            }
        }
    }

    private fun navigateToHome() {
        val authNavOptions = NavOptions.Builder()
            .setPopUpTo(R.id.dest_top_chat, true)
            .build()
        findNavController().navigate(R.id.action_login_to_home_navigation, Bundle(), authNavOptions)
    }

    /** login validation */
    private fun validate(): Boolean {
        var errorCount = 0

        if (vm.selectedCountry.value!!.countryNick.isNullOrEmpty()) {
            errorButtonView()
            errorCount++
        }

        if (binding.etPhone.text.isNullOrEmpty()) {
            binding.tilPhone.error = getString(R.string.error_login_phone_empty)
            errorPhoneView()
            errorCount++
        } else {
            if (!binding.etPhone.isVerifiedPhone()) {
                binding.tilPhone.error = getString(R.string.error_login_phone)
                errorPhoneView()
                errorCount++
            }
        }
        if (binding.etPassword.text.isNullOrEmpty()) {
            binding.tilPassword.error = getString(R.string.error_login_password_empty)
            errorPasswordView()
            errorCount++
        } else {
            if (!binding.etPassword.isVerifiedPwd()) {
                binding.tilPassword.error = getString(R.string.error_login_pwd_8)
                errorPasswordView()
                errorCount++
            }
        }
        return errorCount == 0 //return true if errorCount = 0 else return false
    }

    private fun clearErrorOnFocus() {
        binding.etPhone.setOnFocusChangeListener { v, hasFocus ->
            if (v == binding.etPhone && hasFocus) {
                binding.tilPhone.isErrorEnabled = !hasFocus
                normalPhoneView()
            } else {
                binding.tilPhone.isErrorEnabled = hasFocus
                normalPhoneView()
            }

        }
        binding.etPassword.setOnFocusChangeListener { v, hasFocus ->
            if (v == binding.etPassword && hasFocus) {
                binding.tilPassword.isErrorEnabled = !hasFocus
                normalPasswordView()
            } else {
                binding.tilPassword.isErrorEnabled = hasFocus
                normalPasswordView()
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

    private fun errorPasswordView() {
        binding.tilPassword.setBoxStrokeColorStateList(
            ContextCompat.getColorStateList(
                this.requireContext(),
                R.color.textinputlayout_boxstroke_error_state
            )!!
        )
        binding.tilPassword.setEndIconTintList(
            ContextCompat.getColorStateList(
                this.requireContext(),
                R.color.textinputlayout_endicon_error_state
            )
        )
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

    private fun normalPasswordView() {
        binding.tilPassword.setBoxStrokeColorStateList(
            ContextCompat.getColorStateList(
                this.requireContext(),
                R.color.textinputlayout_boxstroke_normal_state
            )!!
        )
        binding.tilPassword.setEndIconTintList(
            ContextCompat.getColorStateList(
                this.requireContext(),
                R.color.textinputlayout_endicon_normal_state
            )
        )
    }


    private fun shownNoInternetDialog() {
        val tryAgain: Button
        val cancelBtn: Button
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_no_internet)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        /** animate dialog with window animation */
        dialog.window!!.setWindowAnimations(R.style.AnimationForDialog)
        dialog.setCanceledOnTouchOutside(false)
        tryAgain = dialog.findViewById(R.id.btn_try_again) as Button
        cancelBtn = dialog.findViewById(R.id.btn_cancel) as Button

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        tryAgain.setOnClickListener {
            if (MainActivity.isInternetAvailable.value == true) {

            } else {
                //todo delete dataStore userAccessToken key and LoginState
                vm.setSelectedPhone(binding.etPhone.text?.trim().toString())
                vm.requestMobileLogin(
                    phone = binding.etPhone.text?.trim().toString(),
                    password = binding.etPassword.text?.trim().toString(),
                )
                dialog.dismiss()
            }

        }

        dialog.show()
    }

    private fun hideKeyBoard() {
        // Hide the keyboard
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
        Timber.tag("Hide Keyboard").d("Hide True")
    }
}