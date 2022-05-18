package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.tabs.creategp.chat_gp_member

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.galaxytechno.chat.databinding.CreateGpBottomSheetBinding
import com.galaxytechno.chat.presentation.base.BaseBottomSheet
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.tabs.creategp.CreateGroupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateGpImageBottomSheet :
    BaseBottomSheet<CreateGpBottomSheetBinding>(CreateGpBottomSheetBinding::inflate) {
    private val viewModel: CreateGroupViewModel by activityViewModels()

    override fun setupView() {
        super.setupView()
        dialog?.setCanceledOnTouchOutside(true)
    }

    override fun setupListener() {
        super.setupListener()
        binding.tvCamera.setOnClickListener {
            viewModel.setImagePickState("Camera")
            findNavController().popBackStack()
        }
        binding.tvGallery.setOnClickListener {
            viewModel.setImagePickState("Gallery")
            findNavController().popBackStack()
        }
    }
}