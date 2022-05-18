package com.galaxytechno.chat.presentation.ui.auth.pwd_forget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.galaxytechno.chat.R
import com.galaxytechno.chat.common.Constant
import com.galaxytechno.chat.databinding.FragmentPasswordForgetVerifyOtpBinding
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.presentation.extension.displaySnackTop
import com.galaxytechno.chat.presentation.single_activity.MainActivity
import com.galaxytechno.chat.presentation.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class PwdForgotVerifyOtpFragment : OtherLvlFragment<FragmentPasswordForgetVerifyOtpBinding>(
    FragmentPasswordForgetVerifyOtpBinding::inflate
) {

    private var background: Int? = null
    private val viewModelAuth: AuthViewModel by activityViewModels()
    private var otpVerifyCode: String? = null

    override fun initialize() {
        super.initialize()

        binding.toolbarForgetPwdVerify.tvToolbarTitle.text =
            getString(R.string.toolbar_forget_password)
        binding.toolbarForgetPwdVerify.ivBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.tvPhone.text = viewModelAuth.mobile.value!!

        /** focus edittext and showKeyboard */
        binding.etOtp1.requestFocus()

        val imm: InputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.etOtp1, InputMethodManager.SHOW_FORCED)

    }

    override fun setupListener() {
        super.setupListener()

        binding.etOtp1.setOnKeyListener { v, keyCode, event ->
            event?.let {
                clearErrorMessage()
                if (keyCode == KeyEvent.KEYCODE_DEL && it.action == KeyEvent.ACTION_DOWN) {
                    binding.btnVerifyOtp.isClickable = false
                    if (!binding.etOtp1.text.isNullOrEmpty()) {
                        binding.etOtp1.text?.clear()
                        applyColorState(true, 1)
                    }
                }
                if (keyCode == KeyEvent.KEYCODE_SPACE) {
                    //do nothing
                }
            }
            return@setOnKeyListener false
        }

        binding.etOtp2.setOnKeyListener { v, keyCode, event ->
            event?.let {
                clearErrorMessage()
                if (keyCode == KeyEvent.KEYCODE_DEL && it.action == KeyEvent.ACTION_DOWN) {
                    binding.btnVerifyOtp.isClickable = false
                    if (!binding.etOtp2.text.isNullOrEmpty()) {
                        binding.etOtp2.text?.clear()
                        applyColorState(false, 2)
                    } else {
                        binding.etOtp2.clearFocus()
                        binding.etOtp1.text?.clear()
                        binding.etOtp1.requestFocus()
                        applyColorState(true, 2)
                        applyColorState(false, 1)
                    }
                }
                if (keyCode == KeyEvent.KEYCODE_SPACE) {
                    //do nothing
                }
            }
            return@setOnKeyListener false
        }

        binding.etOtp3.setOnKeyListener { v, keyCode, event ->
            event?.let {
                clearErrorMessage()
                if (keyCode == KeyEvent.KEYCODE_DEL && it.action == KeyEvent.ACTION_DOWN) {
                    binding.btnVerifyOtp.isClickable = false
                    if (!binding.etOtp3.text.isNullOrEmpty()) {
                        binding.etOtp3.text?.clear()
                        applyColorState(false, 3)
                    } else {
                        Timber.tag("3 delete").d("Delete from 3")
                        binding.etOtp3.clearFocus()
                        binding.etOtp2.text?.clear()
                        binding.etOtp2.requestFocus()
                        applyColorState(false, 2)
                        applyColorState(true, 3)
                    }
                }
                if (keyCode == KeyEvent.KEYCODE_SPACE) {
                    //do nothing
                }
            }
            return@setOnKeyListener false
        }

        binding.etOtp4.setOnKeyListener { v, keyCode, event ->
            event?.let {
                clearErrorMessage()
                if (keyCode == KeyEvent.KEYCODE_DEL && it.action == KeyEvent.ACTION_DOWN) {
                    binding.btnVerifyOtp.isClickable = false
                    if (!binding.etOtp4.text.isNullOrEmpty()) {
                        binding.etOtp4.text?.clear()
                        applyColorState(false, 4)
                    } else {
                        Timber.tag("3 delete").d("Delete from 3")
                        binding.etOtp4.clearFocus()
                        binding.etOtp3.text?.clear()
                        binding.etOtp3.requestFocus()
                        applyColorState(false, 3)
                        applyColorState(true, 4)
                    }
                }
                if (keyCode == KeyEvent.KEYCODE_SPACE) {
                    //do nothing
                }
            }
            return@setOnKeyListener false
        }

        binding.etOtp5.setOnKeyListener { v, keyCode, event ->
            event?.let {
                clearErrorMessage()
                if (keyCode == KeyEvent.KEYCODE_DEL && it.action == KeyEvent.ACTION_DOWN) {
                    binding.btnVerifyOtp.isClickable = false
                    if (!binding.etOtp5.text.isNullOrEmpty()) {
                        binding.etOtp5.text?.clear()
                        applyColorState(false, 5)
                    } else {
                        binding.etOtp5.clearFocus()
                        binding.etOtp4.text?.clear()
                        binding.etOtp4.requestFocus()
                        applyColorState(false, 4)
                        applyColorState(true, 5)
                    }
                }
                if (keyCode == KeyEvent.KEYCODE_SPACE) {
                    //do nothing
                }
            }
            return@setOnKeyListener false
        }

        binding.etOtp6.setOnKeyListener { v, keyCode, event ->
            event?.let {
                clearErrorMessage()
                if (keyCode == KeyEvent.KEYCODE_DEL && it.action == KeyEvent.ACTION_DOWN) {
                    binding.btnVerifyOtp.isClickable = false
                    if (!binding.etOtp6.text.isNullOrEmpty()) {
                        binding.etOtp6.text?.clear()
                        applyColorState(false, 6)
                    } else {
                        binding.etOtp6.clearFocus()
                        binding.etOtp5.text?.clear()
                        binding.etOtp5.requestFocus()
                        applyColorState(false, 5)
                        applyColorState(true, 6)
                    }
                }
                if (keyCode == KeyEvent.KEYCODE_SPACE) {
                    //do nothing
                }
            }
            return@setOnKeyListener false
        }

        binding.etOtp1.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (binding.etOtp1.text!!.isNotEmpty() && binding.etOtp2.text!!.isNotEmpty()
                    && binding.etOtp3.text!!.isNotEmpty() && binding.etOtp4.text!!.isNotEmpty()
                    && binding.etOtp5.text!!.isNotEmpty() && binding.etOtp6.text!!.isNotEmpty()
                ) {
                    applyColorState(true, 1)
                    binding.etOtp6.requestFocus()
                    binding.etOtp6.isCursorVisible = false
                    binding.btnVerifyOtp.isClickable = true
                    binding.btnVerifyOtp.setBackgroundColor(resources.getColor(R.color.chat_green_500))
                    binding.btnVerifyOtp.setTextColor(resources.getColor(R.color.colorOnPrimary))
                } else if (binding.etOtp1.text!!.contentEquals(" ")) {
                    binding.etOtp1.setText("")
                } else if
                               (binding.etOtp1.text.toString().length == 1) {
                    binding.etOtp1.clearFocus()
                    binding.etOtp2.requestFocus()
                    binding.etOtp2.isCursorVisible = true
                    applyColorState(false, 2)
                    applyColorState(true, 1)

                } else {
                    binding.btnVerifyOtp.setBackgroundColor(resources.getColor(R.color.colorOnFaintButton))
                    binding.btnVerifyOtp.setStrokeColorResource(R.color.colorOnFaintButton)
                    binding.btnVerifyOtp.setTextColor(resources.getColor(R.color.colorOnPrimary))
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        binding.etOtp2.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (binding.etOtp1.text!!.isNotEmpty() && binding.etOtp2.text!!.isNotEmpty()
                    && binding.etOtp3.text!!.isNotEmpty() && binding.etOtp4.text!!.isNotEmpty()
                    && binding.etOtp5.text!!.isNotEmpty() && binding.etOtp6.text!!.isNotEmpty()
                ) {
                    applyColorState(true, 2)
                    binding.etOtp6.requestFocus()
                    binding.etOtp6.isCursorVisible = false
                    binding.btnVerifyOtp.isClickable = true
                    binding.btnVerifyOtp.setBackgroundColor(resources.getColor(R.color.chat_green_500))
                    binding.btnVerifyOtp.setTextColor(resources.getColor(R.color.colorOnPrimary))
                } else if (binding.etOtp2.text!!.contentEquals(" ")) {
                    binding.etOtp2.setText("")
                } else if (binding.etOtp2.text.toString().length == 1) {
                    binding.etOtp2.clearFocus()
                    binding.etOtp3.requestFocus()
                    binding.etOtp3.isCursorVisible = true
                    applyColorState(false, 3)
                    applyColorState(true, 2)
                } else {
                    binding.btnVerifyOtp.setBackgroundColor(resources.getColor(R.color.colorOnFaintButton))
                    binding.btnVerifyOtp.setStrokeColorResource(R.color.colorOnFaintButton)
                    binding.btnVerifyOtp.setTextColor(resources.getColor(R.color.colorOnPrimary))
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
            }
        })


        binding.etOtp3.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (binding.etOtp1.text!!.isNotEmpty() && binding.etOtp2.text!!.isNotEmpty()
                    && binding.etOtp3.text!!.isNotEmpty() && binding.etOtp4.text!!.isNotEmpty()
                    && binding.etOtp5.text!!.isNotEmpty() && binding.etOtp6.text!!.isNotEmpty()
                ) {
                    applyColorState(true, 3)
                    binding.etOtp6.requestFocus()
                    binding.etOtp6.isCursorVisible = false
                    binding.btnVerifyOtp.isClickable = true
                    binding.btnVerifyOtp.setBackgroundColor(resources.getColor(R.color.chat_green_500))
                    binding.btnVerifyOtp.setTextColor(resources.getColor(R.color.colorOnPrimary))
                } else if (binding.etOtp3.text!!.contentEquals(" ")) {
                    binding.etOtp3.setText("")
                } else if (binding.etOtp3.text.toString().length == 1) {
                    binding.etOtp3.clearFocus()
                    binding.etOtp4.requestFocus()
                    binding.etOtp4.isCursorVisible = true
                    applyColorState(false, 4)
                    applyColorState(true, 3)
                } else {
                    binding.btnVerifyOtp.setBackgroundColor(resources.getColor(R.color.colorOnFaintButton))
                    binding.btnVerifyOtp.setStrokeColorResource(R.color.colorOnFaintButton)
                    binding.btnVerifyOtp.setTextColor(resources.getColor(R.color.colorOnPrimary))
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        binding.etOtp4.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (binding.etOtp1.text!!.isNotEmpty() && binding.etOtp2.text!!.isNotEmpty()
                    && binding.etOtp3.text!!.isNotEmpty() && binding.etOtp4.text!!.isNotEmpty()
                    && binding.etOtp5.text!!.isNotEmpty() && binding.etOtp6.text!!.isNotEmpty()
                ) {
                    applyColorState(true, 4)
                    binding.etOtp6.requestFocus()
                    binding.etOtp6.isCursorVisible = false
                    binding.btnVerifyOtp.isClickable = true
                    binding.btnVerifyOtp.setBackgroundColor(resources.getColor(R.color.chat_green_500))
                    binding.btnVerifyOtp.setTextColor(resources.getColor(R.color.colorOnPrimary))
                } else if (binding.etOtp4.text!!.contentEquals(" ")) {
                    binding.etOtp4.setText("")
                } else if (binding.etOtp4.text.toString().length == 1) {
                    binding.etOtp4.clearFocus()
                    binding.etOtp5.requestFocus()
                    binding.etOtp5.isCursorVisible = true
                    applyColorState(false, 5)
                    applyColorState(true, 4)
                } else {
                    binding.btnVerifyOtp.setBackgroundColor(resources.getColor(R.color.colorOnFaintButton))
                    binding.btnVerifyOtp.setStrokeColorResource(R.color.colorOnFaintButton)
                    binding.btnVerifyOtp.setTextColor(resources.getColor(R.color.colorOnPrimary))
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        binding.etOtp5.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (binding.etOtp1.text!!.isNotEmpty() && binding.etOtp2.text!!.isNotEmpty()
                    && binding.etOtp3.text!!.isNotEmpty() && binding.etOtp4.text!!.isNotEmpty()
                    && binding.etOtp5.text!!.isNotEmpty() && binding.etOtp6.text!!.isNotEmpty()
                ) {
                    applyColorState(true, 5)
                    binding.etOtp6.requestFocus()
                    binding.etOtp6.isCursorVisible = false
                    binding.btnVerifyOtp.isClickable = true
                    binding.btnVerifyOtp.setBackgroundColor(resources.getColor(R.color.chat_green_500))
                    binding.btnVerifyOtp.setTextColor(resources.getColor(R.color.colorOnPrimary))
                } else if (binding.etOtp5.text!!.contentEquals(" ")) {
                    binding.etOtp4.setText("")
                } else if (binding.etOtp5.text.toString().length == 1) {
                    binding.etOtp5.clearFocus()
                    binding.etOtp6.requestFocus()
                    binding.etOtp6.isCursorVisible = true
                    applyColorState(false, 6)
                    applyColorState(true, 5)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        binding.etOtp6.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (binding.etOtp6.text!!.contentEquals(" ")) {
                    binding.etOtp6.setText("")
                } else if (binding.etOtp6.text.toString().length == 1) {
                    binding.etOtp6.isCursorVisible = false
                    applyColorState(true, 4)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (binding.etOtp1.text!!.isNotEmpty() && binding.etOtp2.text!!.isNotEmpty()
                    && binding.etOtp3.text!!.isNotEmpty() && binding.etOtp4.text!!.isNotEmpty()
                    && binding.etOtp5.text!!.isNotEmpty() && binding.etOtp6.text!!.isNotEmpty()
                ) {
                    applyColorState(true, 6)
                    binding.btnVerifyOtp.isClickable = true

                    binding.btnVerifyOtp.setBackgroundColor(resources.getColor(R.color.chat_green_500))
                    binding.btnVerifyOtp.setTextColor(resources.getColor(R.color.colorOnPrimary))

                    binding.btnVerifyOtp.setOnClickListener {
                        viewModelAuth.setLoadingState(true)
                        otpVerifyCode = binding.etOtp1.text.toString().trim() +
                                binding.etOtp2.text.toString().trim() +
                                binding.etOtp3.text.toString().trim() +
                                binding.etOtp4.text.toString().trim() +
                                binding.etOtp5.text.toString().trim() +
                                binding.etOtp6.text.toString().trim()

                        viewModelAuth.validateOtp(
                            otpCode = otpVerifyCode!!
                        )

                    }
                } else {
                    binding.btnVerifyOtp.setBackgroundColor(resources.getColor(R.color.colorOnFaintButton))
                    binding.btnVerifyOtp.setStrokeColorResource(R.color.colorOnFaintButton)
                    binding.btnVerifyOtp.setTextColor(resources.getColor(R.color.colorOnPrimary))

                }
            }
        })

        val resendTextView = requireView().findViewById(R.id.tv_send) as TextView
        resendTextView.setOnClickListener {
            binding.tvInvalidOtp.text = "" //clear error text after resend
            applyColorState(true, 1)
            applyColorState(true, 2)
            applyColorState(true, 3)
            applyColorState(true, 4)
            applyColorState(true, 5)
            applyColorState(true, 6)
            viewModelAuth.setLoadingState(true)
            viewModelAuth.getOtpCode()
        }


    }

    private fun applyColorState(isColorWhite: Boolean, bgNumber: Int) {
        background = if (isColorWhite) {
            R.drawable.bg_color_white_conor_small
        } else {
            R.drawable.bg_border_black
        }
        when (bgNumber) {
            1 -> {
                binding.etOtp1.background = ContextCompat.getDrawable(
                    requireContext(),
                    background!!
                )
            }
            2 -> {
                binding.etOtp2.background = ContextCompat.getDrawable(
                    requireContext(),
                    background!!
                )
            }
            3 -> {
                binding.etOtp3.background = ContextCompat.getDrawable(
                    requireContext(),
                    background!!
                )
            }
            4 -> {
                binding.etOtp4.background = ContextCompat.getDrawable(
                    requireContext(),
                    background!!
                )
            }
            5 -> {
                binding.etOtp5.background = ContextCompat.getDrawable(
                    requireContext(),
                    background!!
                )
            }
            6 -> {
                binding.etOtp6.background = ContextCompat.getDrawable(
                    requireContext(),
                    background!!
                )
            }

        }
    }

    private fun String.applyErrorUi() {
        background = R.drawable.bg_border_red
        binding.etOtp1.background = ContextCompat.getDrawable(
            requireContext(),
            background!!)
        binding.etOtp2.background = ContextCompat.getDrawable(
            requireContext(),
            background!!)
        binding.etOtp3.background = ContextCompat.getDrawable(
            requireContext(),
            background!!)
        binding.etOtp4.background = ContextCompat.getDrawable(
            requireContext(),
            background!!)
        binding.etOtp5.background = ContextCompat.getDrawable(
            requireContext(),
            background!!)
        binding.etOtp6.background = ContextCompat.getDrawable(
            requireContext(),
            background!!)

        binding.tvInvalidOtp.text = this
        binding.tvInvalidOtp.visibility = View.VISIBLE
    }

    private fun clearErrorMessage() {
        binding.tvInvalidOtp.visibility = View.INVISIBLE
    }

    override fun observe() {
        super.observe()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            //use StateFlow with collectLatest EVER
            viewModelAuth.isLoading.collectLatest {
                if (it) {
                    (activity as MainActivity).showLoadingDialog(getString(R.string.verifying))
                } else {
                    (activity as MainActivity).hideLoadingDialog()
                }
            }
        }
            viewModelAuth.getOtpResponse.observe(viewLifecycleOwner) {
                val arr = it.data!!.otpCode
                    arr.toCharArray()
                binding.etOtp1.setText(arr.elementAt(0).toString())
                binding.etOtp2.setText(arr.elementAt(1).toString())
                binding.etOtp3.setText(arr.elementAt(2).toString())
                binding.etOtp4.setText(arr.elementAt(3).toString())
                binding.etOtp5.setText(arr.elementAt(4).toString())
                binding.etOtp6.setText(arr.elementAt(5).toString())
                Timber.tag("OTP").d("OTP${it.data?.otpCode}")
            }


        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModelAuth.validateOtpObj.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        binding.root.displaySnackTop(it.message ?: "Fail Otp Code")
                    }
                    is RemoteEvent.LoadingEvent -> TODO()
                    is RemoteEvent.SuccessEvent -> {
                        val responseData = it.data
                        if (responseData!!.status == Constant.STATUS_SUCCESS) {
                            if (viewModelAuth.isTwoFactor.value!!) {
                                viewModelAuth.getConfirmedQuestions()
                                findNavController().navigate(R.id.action_dest_pwd_forget_verify_otp_to_dest_forget_pwd_question)
                            } else {
                                findNavController().navigate(
                                    R.id.dest_otp_verify_to_forget_pwd_reset
                                )
                            }
                        } else {
                            responseData.error?.applyErrorUi()
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModelAuth.forgotPwdOtpTimber.collectLatest {
                if(viewModelAuth.forgotPwdOtpTimber.value != 0) {
                    binding.tvReceiveOtpQuestion.text = getString(R.string.otp_expire)
                    binding.tvSend.text = " $it s"
                } else {

                    binding.tvReceiveOtpQuestion.text = getString(R.string.receive_otp_question)
                    binding.tvSend.text = getString(R.string.resend)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModelAuth.otpResponseEvent.collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> TODO()
                    is RemoteEvent.LoadingEvent -> TODO()
                    is RemoteEvent.SuccessEvent -> {
                        viewModelAuth.setForgotPwdOtpTimber(1) //todo add this later it.data!!.data.expireTimeMin current value is for testing purpose
                        viewModelAuth.setMobileNumber(viewModelAuth.mobile.value!!.toString())
                    }
                }
            }
        }

    }
}