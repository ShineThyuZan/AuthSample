package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.my_profile.profile

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.galaxytechno.chat.databinding.ProfileImagePickBottomSheetBinding
import com.galaxytechno.chat.presentation.base.BaseBottomSheet
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.my_profile.MyProfileViewModel

class PickProfileImageBottomSheetFragment :
    BaseBottomSheet<ProfileImagePickBottomSheetBinding>(ProfileImagePickBottomSheetBinding::inflate) {

    private val vm: MyProfileViewModel by activityViewModels()

    override fun setupView() {
        super.setupView()
        dialog?.setCanceledOnTouchOutside(true)
    }

    override fun setupListener() {
        super.setupListener()
        binding.tvCamera.setOnClickListener {
            vm.setProfileStateCameraOrGallery("Camera")
            findNavController().popBackStack()
        }
        binding.tvGallery.setOnClickListener {
            vm.setProfileStateCameraOrGallery("Gallery")
            findNavController().popBackStack()
        }
        binding.tvRemove.setOnClickListener {
            vm.setProfileStateCameraOrGallery("Remove")
            findNavController().popBackStack()
        }
    }


}