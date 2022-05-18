package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat

import android.view.View
import com.galaxytechno.chat.databinding.BottomSheetAudioSendBinding
import com.galaxytechno.chat.presentation.base.BaseBottomSheet
import com.galaxytechno.chat.presentation.extension.displayToast

class SendAudioBottomSheetFragment: BaseBottomSheet<BottomSheetAudioSendBinding> (
    BottomSheetAudioSendBinding::inflate
        ) {
    override fun setupListener() {
        super.setupListener()
        binding.ivStopAudio.setOnClickListener {
            displayToast("Stop Audio")
        }

        binding.ivSendAudio.setOnClickListener {
            displayToast("Send Audio")
        }

        binding.ivAudioRecorder.setOnLongClickListener {
            binding.ivAudioRecorder.visibility = View.INVISIBLE
            binding.tvAudioRecordInstruction.visibility = View.INVISIBLE
            binding.ivAudioRecording.visibility = View.VISIBLE
            binding.ivStopAudio.visibility = View.VISIBLE
            binding.ivSendAudio.visibility = View.VISIBLE
            displayToast("Start Record")
            true
        }

        binding.ivStopAudio.setOnClickListener {
            binding.ivAudioRecording.visibility = View.INVISIBLE
            binding.ivStopAudio.visibility = View.INVISIBLE
            binding.ivSendAudio.visibility = View.INVISIBLE
            binding.ivAudioRecorder.visibility = View.VISIBLE
            binding.tvAudioRecordInstruction.visibility = View.VISIBLE
            displayToast("End Record")
        }
        binding.ivSendAudio.setOnClickListener {
            displayToast("Send Record")
        }
    }

    override fun setupView() {
        super.setupView()
    }

    override fun observe() {
        super.observe()
    }
}