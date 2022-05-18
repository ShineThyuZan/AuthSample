package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.my_profile.profile

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.galaxytechno.chat.databinding.CoverImagePickBottomSheetBinding
import com.galaxytechno.chat.presentation.base.BaseBottomSheet
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.my_profile.MyProfileViewModel

class PickCoverImageBottomSheetFragment : BaseBottomSheet<CoverImagePickBottomSheetBinding>(
    CoverImagePickBottomSheetBinding::inflate
) {

    private val vm: MyProfileViewModel by activityViewModels()

    override fun setupView() {
        super.setupView()
        dialog?.setCanceledOnTouchOutside(true)
    }

    override fun setupListener() {
        super.setupListener()
        binding.tvCamera.setOnClickListener {
            vm.setCoverStateCameraOrGallery("Camera")
            findNavController().popBackStack()
        }
        binding.tvGallery.setOnClickListener {
            vm.setCoverStateCameraOrGallery("Gallery")
            findNavController().popBackStack()
        }
        binding.tvRemove.setOnClickListener {
            vm.setCoverStateCameraOrGallery("Remove")
            findNavController().popBackStack()
        }
    }


}