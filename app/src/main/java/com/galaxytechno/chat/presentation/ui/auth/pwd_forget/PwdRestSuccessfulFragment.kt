package com.galaxytechno.chat.presentation.ui.auth.pwd_forget

import android.os.Bundle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.galaxytechno.chat.R
import com.galaxytechno.chat.databinding.FragmentPwdResetSuccessfulBinding
import com.galaxytechno.chat.presentation.base.OtherLvlFragment

class PwdRestSuccessfulFragment :
    OtherLvlFragment<FragmentPwdResetSuccessfulBinding>(FragmentPwdResetSuccessfulBinding::inflate) {

    override fun setupListener() {
        super.setupListener()
        binding.btnGoLogin.setOnClickListener {

            val authNavOptions = NavOptions.Builder()
                .setPopUpTo(R.id.dest_login, true)
                .build()
            findNavController().navigate(R.id.action_pwdRestSuccessfulFragment_to_dest_login, Bundle(), authNavOptions)
        }
    }
}