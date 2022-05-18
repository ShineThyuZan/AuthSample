package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat

import com.galaxytechno.chat.databinding.BottomSheetMessageSendBinding
import com.galaxytechno.chat.presentation.base.BaseBottomSheet
import com.galaxytechno.chat.presentation.extension.displayToast

class SendMessageBottomSheetFragment: BaseBottomSheet<BottomSheetMessageSendBinding> (
    BottomSheetMessageSendBinding::inflate
        ) {
    override fun setupListener() {
        super.setupListener()
        binding.tvTakePhotoOrVideo.setOnClickListener {
            displayToast("Take a photo or video")
        }

        binding.tvGallery.setOnClickListener {
            displayToast("Gallery")
        }

        binding.tvSticker.setOnClickListener {
            displayToast("Stickers")
        }

        binding.tvDocument.setOnClickListener {
            displayToast("Documents")
        }

        binding.tvContact.setOnClickListener {
            displayToast("Contact")
        }
    }

    override fun setupView() {
        super.setupView()
    }

    override fun observe() {
        super.observe()
    }
}