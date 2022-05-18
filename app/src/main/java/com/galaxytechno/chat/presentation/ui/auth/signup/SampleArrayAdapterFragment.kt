package com.galaxytechno.chat.presentation.ui.auth.signup

class SampleArrayAdapterFragment {

//    private val viewModel: SignupViewModel by activityViewModels()
//    private var questionAList: List<SecurityQuestResponse> = listOf()
//    private lateinit var questListAdapter: SignupCustomArraryAdapter
//    private var questionOBj: MutableList<SecurityQuestObj> = mutableListOf()
//    private var selectedQuestionId = ""
//
//
//    override fun initialize() {
//        super.initialize()
//        viewModel.getSecurityQuestList()
//    }
//
//    override fun setupListener() {
//        super.setupListener()
//
//        questListAdapter =
//            SignupCustomArraryAdapter(requireContext(), questionOBj)
//        binding.btnStart.setOnClickListener {
//            if (binding.cbTermsAndService.isChecked) {
//                findNavController().navigate(R.id.action_dest_signup_security_question_to_dest_login)
//            } else {
//                Toast.makeText(context, "Please accept Terms of Service", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        binding.tvTermsAndService.setOnClickListener {
//            findNavController().navigate(R.id.action_dest_signup_security_question_to_dest_signup_terms)
//        }
//    }
//
//
//    private fun setupCustomSpinnerA() {
//
//        questListAdapter = SignupCustomArraryAdapter(requireContext(), questionOBj)
//        binding.spQuestion1.adapter = questListAdapter
//
//        binding.spQuestion1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>,
//                view: View,
//                position: Int,
//                id: Long
//            ) {
//
//                selectedQuestionId = questionOBj[position].id.toString()
//                Toast.makeText(context, questionOBj[position].question, Toast.LENGTH_SHORT).show()
//                Toast.makeText(context, questionOBj[position].id.toString(), Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//                // Code to perform some action when nothing is selected
//            }
//        }
//    }
//
//    private fun setupCustomSpinnerB() {
//
//        questListAdapter = SignupCustomArraryAdapter(requireContext(), questionOBj)
//        binding.spQuestion2.adapter = questListAdapter
//
//        binding.spQuestion2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>,
//                view: View,
//                position: Int,
//                id: Long
//            ) {
//
//                selectedQuestionId = questionOBj[position].id.toString()
//                Toast.makeText(context, questionOBj[position].question, Toast.LENGTH_SHORT).show()
//                Toast.makeText(context, questionOBj[position].id.toString(), Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//                // Code to perform some action when nothing is selected
//            }
//        }
//    }
//
//    private fun setupCustomSpinnerC() {
//
//        questListAdapter = SignupCustomArraryAdapter(requireContext(), questionOBj)
//        binding.spQuestion3.adapter = questListAdapter
//
//        binding.spQuestion3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>,
//                view: View,
//                position: Int,
//                id: Long
//            ) {
//
//                selectedQuestionId = questionOBj[position].id.toString()
//                Toast.makeText(context, questionOBj[position].question, Toast.LENGTH_SHORT).show()
//                Toast.makeText(context, questionOBj[position].id.toString(), Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//                // Code to perform some action when nothing is selected
//            }
//        }
//    }
//
//
//    override fun observe() {
//        super.observe()
//        viewModel.questionAList.observe(viewLifecycleOwner) {
//            questionOBj = it
//
//            setupCustomSpinnerA()
//
//        }
//        viewModel.questionBList.observe(viewLifecycleOwner) {
//            questionOBj = it
//
//            setupCustomSpinnerB()
//
//        }
//        viewModel.questionBList.observe(viewLifecycleOwner) {
//            questionOBj = it
//
//            setupCustomSpinnerC()
//
//        }
//
//
//
//        viewModel.securityQuestAObj.observe(viewLifecycleOwner) {
//        }
//
//    }

}