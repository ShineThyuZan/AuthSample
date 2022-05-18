package com.galaxytechno.chat.presentation.ui.auth.pwd_forget

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.galaxytechno.chat.databinding.FragmentPhoneNoNotFoundBinding
import com.galaxytechno.chat.presentation.base.OtherLvlFragment

class FragmentNoAccountFound :
    OtherLvlFragment<FragmentPhoneNoNotFoundBinding>(FragmentPhoneNoNotFoundBinding::inflate) {

    private val args: FragmentNoAccountFoundArgs by navArgs()
    override fun setupView() {
        super.setupView()
        binding.tvPhoneNo.text = args.phone
    }

    override fun setupListener() {
        super.setupListener()
        binding.tvChangeNo.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}