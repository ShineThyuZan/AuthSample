package com.galaxytechno.chat.presentation.ui.auth.signup.question

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.galaxytechno.chat.R
import com.galaxytechno.chat.common.InternetChecker
import com.galaxytechno.chat.common.NetworkStatus
import com.galaxytechno.chat.databinding.FragmentSignupSecurityQuestionBinding
import com.galaxytechno.chat.model.dto.SecurityQuestObj
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.presentation.extension.displaySnack
import com.galaxytechno.chat.presentation.single_activity.MainActivity
import com.galaxytechno.chat.presentation.ui.auth.AuthViewModel
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SignupSecurityQuestionFragment : OtherLvlFragment<FragmentSignupSecurityQuestionBinding>(
    FragmentSignupSecurityQuestionBinding::inflate
) {
    private val viewModel: AuthViewModel by activityViewModels()
    private var securityQuesA: SecurityQuestObj = SecurityQuestObj()
    private var securityQuesB: SecurityQuestObj = SecurityQuestObj()
    private var securityQuesC: SecurityQuestObj = SecurityQuestObj()
    private var ans3: String = ""

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

    override fun setupView() {
        super.setupView()
        clearErrorOnFocus()
    }

    override fun setupListener() {
        super.setupListener()
        val questionNumberOne = "1"
        val questionNumberTwo = "2"
        val questionNumberThree = "3"

        binding.etAnswer1.addTextChangedListener { text ->
            text?.let {
                binding.tilAnswer1.apply {
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
                binding.tilAnswer1.endIconMode = TextInputLayout.END_ICON_NONE
            }
        }

        binding.etAnswer2.addTextChangedListener { text ->
            text?.let {

                binding.tilAnswer2.apply {
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
                binding.tilAnswer2.endIconMode = TextInputLayout.END_ICON_NONE
            }
        }

        binding.etAnswer3.addTextChangedListener { text ->
            text?.let {

                binding.tilAnswer3.apply {
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
                binding.tilAnswer3.endIconMode = TextInputLayout.END_ICON_NONE
            }
        }

        binding.btnQuestion1.setOnClickListener {
            binding.btnQuestion1.setStrokeColorResource(R.color.colorPrimaryContainer)
            val directions =
                SignupSecurityQuestionFragmentDirections.actionDestSignupSecurityQuestionToSignupQuestAButtonSheet(
                    questionNumberOne
                )
            findNavController().navigate(directions)
        }

        binding.btnQuestion2.setOnClickListener {
            binding.btnQuestion2.setStrokeColorResource(R.color.colorPrimaryContainer)
            val directions =
                SignupSecurityQuestionFragmentDirections.actionDestSignupSecurityQuestionToSignupQuestAButtonSheet(
                    questionNumberTwo
                )
            findNavController().navigate(directions)

        }

        binding.btnQuestion3.setOnClickListener {
            binding.btnQuestion3.setStrokeColorResource(R.color.colorPrimaryContainer)
            val directions =
                SignupSecurityQuestionFragmentDirections.actionDestSignupSecurityQuestionToSignupQuestAButtonSheet(
                    questionNumberThree
                )
            findNavController().navigate(directions)

        }

        binding.tvTermsAndService.setOnClickListener {
            findNavController().navigate(R.id.action_dest_signup_security_question_to_dest_signup_terms)
        }

        binding.etAnswer1.doAfterTextChanged {
            viewModel.setAnswerA(binding.etAnswer1.text!!.trim().toString())
        }
        binding.etAnswer2.doAfterTextChanged {
            viewModel.setAnswerB(binding.etAnswer2.text!!.trim().toString())
        }
        binding.etAnswer3.doAfterTextChanged {
            viewModel.setAnswerC(binding.etAnswer3.text!!.trim().toString())
        }


        binding.btnStart.setOnClickListener {
            if (validate()) {
                if (!internetStatus) {
                    binding.root.displaySnack(getString(R.string.snack_no_internet))
                } else {
                    clearErrorOnFocus()
                    viewModel.setLoadingState(true)
                    viewModel.requestSignUp()
                }
            }
        }
    }


    override fun observe() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            //use StateFlow with collectLatest EVER
            viewModel.isLoading.collectLatest {
                if (it) {
                    (activity as MainActivity).showLoadingDialog(getString(R.string.sing_up))
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
            viewModel.signupRequest.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        binding.root.displaySnack(it.message ?: "Error happened!")
                    }
                    is RemoteEvent.LoadingEvent -> {
                        //in this case do nothing, if we have to fetch list from server, we will display shimmers in LoadingEvent
                    }
                    is RemoteEvent.SuccessEvent -> {
                        viewModel.setLoadingState(false)
                        binding.root.displaySnack(getString(R.string.snack_request_login_success))
                        hideKeyBoard()
                        navigateToHome()
//                        displayToast(it.data!!.status)
                    }
                }
            }
        }
        

        viewModel.questAObj.observe(viewLifecycleOwner) {
            binding.btnQuestion1.text = it.question
            securityQuesA = it
        }

        viewModel.questBObj.observe(viewLifecycleOwner) {
            binding.btnQuestion2.text = it.question
            securityQuesB = it
        }

        // question obj get
        viewModel.questCObj.observe(viewLifecycleOwner) {
            ans3 = binding.etAnswer3.text?.trim().toString()
            binding.btnQuestion3.text = it.question
            securityQuesC = it
        }


    }

    private fun navigateToHome() {
        val authNavOptions = NavOptions.Builder()
            .setPopUpTo(R.id.auth_navigation, true)
            .build()
        findNavController().navigate(
            R.id.home_navigation,
            Bundle(),
            authNavOptions
        )

    }

    private fun validate(): Boolean {
     var errorCount = 0

        if(binding.btnQuestion1.text.trim().toString() == resources.getString(R.string.select_question)) {
            binding.btnQuestion1.setStrokeColorResource(R.color.colorError)
            errorCount++
        }
        if(binding.btnQuestion2.text.trim().toString() == resources.getString(R.string.select_question)) {
            binding.btnQuestion2.setStrokeColorResource(R.color.colorError)
            errorCount++
        }
        if(binding.btnQuestion3.text.trim().toString() == resources.getString(R.string.select_question)) {
            binding.btnQuestion3.setStrokeColorResource(R.color.colorError)
            errorCount++
        }
        if(binding.etAnswer1.text?.trim().toString().isNullOrEmpty()) {
            errorAns1View()
            errorCount++
        }
        if(binding.etAnswer2.text?.trim().toString().isNullOrEmpty()) {
            errorAns2View()
            errorCount++
        }
        if(binding.etAnswer3.text?.trim().toString().isNullOrEmpty()) {
            errorAns3View()
            errorCount++
        }
        if (!binding.cbTermsAndService.isChecked) {
            Toast.makeText(context, "Please accept Terms of Service", Toast.LENGTH_SHORT)
                .show()
            errorCount++
        }
        return errorCount == 0  //return true if errorCount = 0 else return false
    }

    private fun clearErrorOnFocus() {
        binding.etAnswer1.setOnFocusChangeListener { v, hasFocus ->
            if (v == binding.etAnswer1 && hasFocus) {
                normalAns1View()
            }
        }

        binding.etAnswer2.setOnFocusChangeListener { v, hasFocus ->
            if (v == binding.etAnswer2 && hasFocus) {
                normalAns2View()
            }
        }

        binding.etAnswer3.setOnFocusChangeListener { v, hasFocus ->
            if (v == binding.etAnswer3 && hasFocus) {
                normalAns3View()
            }
        }

    }

    private fun errorAns1View() {
        binding.tilAnswer1.setBoxStrokeColorStateList(ContextCompat.getColorStateList(requireContext(), R.color.textinputlayout_boxstroke_error_state)!!)
    }

    private fun normalAns1View() {
        binding.tilAnswer1.setBoxStrokeColorStateList(ContextCompat.getColorStateList(requireContext(), R.color.textinputlayout_boxstroke_normal_state)!!)
    }

    private fun errorAns2View() {
        binding.tilAnswer2.setBoxStrokeColorStateList(ContextCompat.getColorStateList(requireContext(), R.color.textinputlayout_boxstroke_error_state)!!)
    }

    private fun normalAns2View() {
        binding.tilAnswer2.setBoxStrokeColorStateList(ContextCompat.getColorStateList(requireContext(), R.color.textinputlayout_boxstroke_normal_state)!!)
    }

    private fun errorAns3View() {
        binding.tilAnswer3.setBoxStrokeColorStateList(ContextCompat.getColorStateList(requireContext(), R.color.textinputlayout_boxstroke_error_state)!!)
    }

    private fun normalAns3View() {
        binding.tilAnswer3.setBoxStrokeColorStateList(ContextCompat.getColorStateList(requireContext(), R.color.textinputlayout_boxstroke_normal_state)!!)
    }

    fun hideKeyBoard() {
        // Hide the keyboard
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
        Timber.tag("Hide Keyboard").d("Hide True")
    }




}