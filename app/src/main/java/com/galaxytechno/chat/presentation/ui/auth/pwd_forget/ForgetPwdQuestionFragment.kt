package com.galaxytechno.chat.presentation.ui.auth.pwd_forget

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.galaxytechno.chat.R
import com.galaxytechno.chat.common.Constant
import com.galaxytechno.chat.databinding.FragmentForgetPwdQuestionBinding
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.presentation.extension.displayToast
import com.galaxytechno.chat.presentation.extension.isVerifiedAnswer
import com.galaxytechno.chat.presentation.single_activity.MainActivity
import com.galaxytechno.chat.presentation.ui.auth.AuthViewModel
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ForgetPwdQuestionFragment :
    OtherLvlFragment<FragmentForgetPwdQuestionBinding>(FragmentForgetPwdQuestionBinding::inflate) {

    private val viewModel: AuthViewModel by activityViewModels()

    override fun initialize() {
        super.initialize()
        viewModel.getConfirmedQuestions()
    }

    override fun setupView() {
        clearErrorOnFocus()
        super.setupView()
    }

    override fun setupListener() {
        super.setupListener()
        binding.toolbarForgetPwd.tvToolbarTitle.text = getString(R.string.toolbar_title_forget_pwd)
        binding.toolbarForgetPwd.ivBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.etAnsOne.doAfterTextChanged {
            viewModel.setForgetPwdAnswerA(binding.etAnsOne.text!!.trim().toString())
        }
        binding.etAnsTwo.doAfterTextChanged {
            viewModel.setForgetPwdAnswerB(binding.etAnsTwo.text!!.trim().toString())
        }
        binding.etAnsThree.doAfterTextChanged {
            viewModel.setForgetPwdAnswerC(binding.etAnsThree.text!!.trim().toString())
        }

        binding.btnQuestionNext.setOnClickListener {
            clearErrorOnFocus()
           if(validate()) {
               viewModel.setLoadingState(true)
               viewModel.validateConfirmedQuestions()
           }
        }

        binding.etAnsOne.addTextChangedListener { text ->
            text?.let {
                if(binding.tilQuestOne.isErrorEnabled) {
                    binding.tilQuestOne.isErrorEnabled = false
                    normalAnsOneView()
                }
                binding.tilQuestOne.apply {
                    endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                    setEndIconDrawable(R.drawable.ic_close)
                    isEndIconCheckable = true
                    setEndIconOnClickListener {
                        text.clear()
                        this.endIconMode = TextInputLayout.END_ICON_NONE
                    }
                }
            } ?: run {
                binding.tilQuestOne.endIconMode = TextInputLayout.END_ICON_NONE
            }
        }

        binding.etAnsTwo.addTextChangedListener { text ->
            text?.let {
                if(binding.tilQuesTwo.isErrorEnabled) {
                    binding.tilQuesTwo.isErrorEnabled = false
                    normalAnsOneView()
                }
                binding.tilQuesTwo.apply {
                    endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                    setEndIconDrawable(R.drawable.ic_close)
                    isEndIconCheckable = true
                    setEndIconOnClickListener {
                        text.clear()
                        this.endIconMode = TextInputLayout.END_ICON_NONE
                    }
                }
            } ?: run {
                binding.tilQuesTwo.endIconMode = TextInputLayout.END_ICON_NONE
            }
        }

        binding.etAnsThree.addTextChangedListener { text ->
            text?.let {
                if(binding.tilQuesThree.isErrorEnabled) {
                    binding.tilQuesThree.isErrorEnabled = false
                    normalAnsOneView()
                }
                binding.tilQuesThree.apply {
                    endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                    setEndIconDrawable(R.drawable.ic_close)
                    isEndIconCheckable = true
                    setEndIconOnClickListener {
                        text.clear()
                        this.endIconMode = TextInputLayout.END_ICON_NONE
                    }
                }
            } ?: run {
                binding.tilQuesThree.endIconMode = TextInputLayout.END_ICON_NONE
            }
        }
    }

    private fun showIncorrectAnswerDialog() {
        val okButton: Button
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.incorrect_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        /** animate dialog with window animation */
        dialog.window!!.setWindowAnimations(R.style.AnimationForDialog)
        dialog.setCanceledOnTouchOutside(false)
        okButton = dialog.findViewById(R.id.btnOk) as Button

        okButton.setOnClickListener {
            //todo delete dataStore userAccessToken key and LoginState
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun observe() {
        super.observe()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            //use StateFlow with collectLatest EVER
            viewModel.isLoading.collectLatest {
                if (it) {
                    (activity as MainActivity).showLoadingDialog(getString(R.string.checking))
                } else {
                    (activity as MainActivity).hideLoadingDialog()
                }
            }
        }

        /** observing forget Pwd quest data inserting in question text */
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.forgetPwdQuestEvent.collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        displayToast(it.message ?: "error in requesting ")
                    }
                    is RemoteEvent.LoadingEvent -> {
                        displayToast("loading state ")
                    }
                    is RemoteEvent.SuccessEvent -> {
                        viewModel.setSelectedConfirmedQuestions(it.data!!.data?.questionAndAnsList?: listOf())
                        binding.tvQuestionOne.text = it.data.data!!.questionAndAnsList[0].question
                        binding.tvQuestionTwo.text = it.data.data.questionAndAnsList[1].question
                        binding.tvQuestionThree.text = it.data.data.questionAndAnsList[2].question
                    }
                }
            }
        }
        /** observing forget pwd quest and answer confirmation */
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.forgetPwdQuestAndAnsRequestConfirmation.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        displayToast(it.data!!.error?:"Error")
                    }
                    is RemoteEvent.LoadingEvent -> TODO()
                    is RemoteEvent.SuccessEvent -> {
                        if (it.data!!.status == Constant.STATUS_SUCCESS) {
                            findNavController().navigate(R.id.action_forget_quest_to_pwd_reset)
                        } else {
                            showIncorrectAnswerDialog()
                        }
                    }
                }
            }
        }

    }

    private fun validate(): Boolean {
        var errorCount = 0
        if (!binding.etAnsOne.isVerifiedAnswer()) {
            binding.tilQuestOne.error = "Fill the Answer"
            errorAnsOneView()
            errorCount++
        }
        if (!binding.etAnsTwo.isVerifiedAnswer()) {
            binding.tilQuesTwo.error = "Fill the Answer"
            errorAnsTwoView()
            errorCount++
        }
        if (!binding.etAnsThree.isVerifiedAnswer()) {
            binding.tilQuesThree.error = "Fill the Answer"
            errorAnsThreeView()
            errorCount++
        }
        return errorCount == 0
    }

    private fun clearErrorOnFocus() {
        binding.etAnsOne.setOnFocusChangeListener { v, hasFocus ->
            if (v == binding.etAnsOne && hasFocus) {
                binding.tilQuestOne.isErrorEnabled = !hasFocus
                normalAnsOneView()
            }
        }

        binding.etAnsTwo.setOnFocusChangeListener { v, hasFocus ->
            if (v == binding.etAnsTwo && hasFocus) {
                binding.tilQuesTwo.isErrorEnabled = !hasFocus
                normalAnsTwoView()
            }
        }

        binding.etAnsThree.setOnFocusChangeListener { v, hasFocus ->
            if (v == binding.etAnsThree && hasFocus) {
                binding.tilQuesThree.isErrorEnabled = !hasFocus
                normalAnsThreeView()
            }
        }

    }

    private fun normalAnsOneView() {
        binding.tilQuestOne.setBoxStrokeColorStateList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_boxstroke_normal_state)!!)
        binding.tilQuestOne.setEndIconTintList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_endicon_normal_state))
    }

    private fun errorAnsOneView() {
        binding.tilQuestOne.setBoxStrokeColorStateList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_boxstroke_error_state)!!)
        binding.tilQuestOne.setEndIconTintList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_endicon_error_state))
    }

    private fun normalAnsTwoView() {
        binding.tilQuesTwo.setBoxStrokeColorStateList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_boxstroke_normal_state)!!)
        binding.tilQuesTwo.setEndIconTintList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_endicon_normal_state))
    }

    private fun errorAnsTwoView() {
        binding.tilQuesTwo.setBoxStrokeColorStateList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_boxstroke_error_state)!!)
        binding.tilQuesTwo.setEndIconTintList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_endicon_error_state))
    }

    private fun normalAnsThreeView() {
        binding.tilQuesThree.setBoxStrokeColorStateList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_boxstroke_normal_state)!!)
        binding.tilQuesThree.setEndIconTintList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_endicon_normal_state))
    }

    private fun errorAnsThreeView() {
        binding.tilQuesThree.setBoxStrokeColorStateList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_boxstroke_error_state)!!)
        binding.tilQuesThree.setEndIconTintList(ContextCompat.getColorStateList(this.requireContext(), R.color.textinputlayout_endicon_error_state))
    }

}