package com.galaxytechno.chat.presentation.ui.auth.signup.question

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.galaxytechno.chat.R
import com.galaxytechno.chat.databinding.FragmentSignupQuestionBinding
import com.galaxytechno.chat.model.dto.SecurityQuestObj
import com.galaxytechno.chat.presentation.base.BaseBottomSheet
import com.galaxytechno.chat.presentation.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupQuestButtonSheet : BaseBottomSheet<FragmentSignupQuestionBinding>(
    FragmentSignupQuestionBinding::inflate
), QuestAListDelegate, QuestBListDelegate, QuestCListDelegate {

    private val args: SignupQuestButtonSheetArgs by navArgs()

    private val viewModelAuth: AuthViewModel by activityViewModels()
    private lateinit var questionAdapter: QuestionABottomSheetAdapter
    private lateinit var questionBAdapter: QuestionBBottomSheetAdapter
    private lateinit var questionCAdapter: QuestionCBottomSheetAdapter
    private var mQuestList: List<SecurityQuestObj> = listOf()

    override fun initialize() {
        super.initialize()
        binding.tvQuestType.text = getString(R.string.select_question)
    }

    override fun setupView() {
        super.setupView()
    }


    override fun observe() {
        super.observe()

        when (args.questionType) {
            "1" -> {
                viewModelAuth.questionAList.observe(viewLifecycleOwner) {
                    setUpQuestionAListRecyclerView()
                    questionAdapter.setNewData(it as MutableList<SecurityQuestObj>)
                }
            }

            "2" -> {
                viewModelAuth.questionBList.observe(viewLifecycleOwner) {
                    setUpQuestionBListRecyclerView()
                    questionBAdapter.setNewData(it as MutableList<SecurityQuestObj>)
                }
            }

            "3" -> {
                viewModelAuth.questionCList.observe(viewLifecycleOwner) {
                    setUpQuestionCListRecyclerView()
                    questionCAdapter.setNewData(it as MutableList<SecurityQuestObj>)
                }
            }


        }
    }


    private fun setUpQuestionAListRecyclerView() {
        questionAdapter =
            QuestionABottomSheetAdapter(requireContext(), this@SignupQuestButtonSheet)
        binding.rvQuestion.apply {
            this.adapter = questionAdapter
            this.layoutManager = LinearLayoutManager(context)
            this.setHasFixedSize(true)
        }
    }

    private fun setUpQuestionBListRecyclerView() {
        questionBAdapter =
            QuestionBBottomSheetAdapter(requireContext(), this@SignupQuestButtonSheet)
        binding.rvQuestion.apply {
            this.adapter = questionBAdapter
            this.layoutManager = LinearLayoutManager(context)
            this.setHasFixedSize(true)
        }
    }

    private fun setUpQuestionCListRecyclerView() {
        questionCAdapter =
            QuestionCBottomSheetAdapter(requireContext(), this@SignupQuestButtonSheet)
        binding.rvQuestion.apply {
            this.adapter = questionCAdapter
            this.layoutManager = LinearLayoutManager(context)
            this.setHasFixedSize(true)
        }
    }

    override fun onQuestAClicked(questData: SecurityQuestObj) {
        viewModelAuth.setQuestAObj(questData)
        findNavController().popBackStack()
    }

    override fun onQuestBClicked(questData: SecurityQuestObj) {
        viewModelAuth.setQuestBObj(questData)
        findNavController().popBackStack()

    }

    override fun onQuestCClicked(questData: SecurityQuestObj) {
        viewModelAuth.setQuestCObj(questData)
        findNavController().popBackStack()
    }

}