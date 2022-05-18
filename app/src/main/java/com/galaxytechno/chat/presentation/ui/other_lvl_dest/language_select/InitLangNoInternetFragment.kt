package com.galaxytechno.chat.presentation.ui.other_lvl_dest.language_select

import androidx.navigation.fragment.findNavController
import com.galaxytechno.chat.R
import com.galaxytechno.chat.databinding.LayoutNoInternetLanguageSelectBinding
import com.galaxytechno.chat.presentation.base.OtherLvlFragment

class InitLangNoInternetFragment: OtherLvlFragment<LayoutNoInternetLanguageSelectBinding> (
    LayoutNoInternetLanguageSelectBinding::inflate
        ) {
    override fun initialize() {
        super.initialize()
    }

    override fun setupListener() {
        super.setupListener()
        binding.btnTryAgain.setOnClickListener {
            findNavController().navigate(R.id.action_initLangNoInternetFragment_to_initLangSelectFragment)
        }
    }
}