package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.my_profile.profile

import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.galaxytechno.chat.R
import com.galaxytechno.chat.databinding.DialogFullScreenImageBinding
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.galaxytechno.chat.presentation.extension.displaySnackAction
import com.galaxytechno.chat.presentation.extension.displayToast
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FullScreenImageFragment :
    OtherLvlFragment<DialogFullScreenImageBinding>(DialogFullScreenImageBinding::inflate) {
    private val args: FullScreenImageFragmentArgs by navArgs()

    override fun setupView() {
        super.setupView()
        Glide.with(requireActivity())
            .load(args.imageUrl)
            .into(binding.ivProfileImage)
    }

    override fun setupListener() {
        super.setupListener()
        binding.ivBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        val moreAction = binding.ivMore.findViewById<ImageView>(R.id.iv_more)
        moreAction.setOnClickListener {
            val popupMenu = PopupMenu(context, moreAction)
            popupMenu.menuInflater.inflate(R.menu.user_action_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.save -> {
                        val downloadManager =
                            requireActivity().getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                        val uri = Uri.parse(args.imageUrl)
                        val request = DownloadManager.Request(uri)
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        val reference = downloadManager.enqueue(request)

                        binding.root.displaySnackAction(
                            msg = getString(R.string.save_image),
                            actionText = getString(android.R.string.yes),
                            onActionClick = {
                                setupView()
                            },
                            duration = Snackbar.LENGTH_SHORT
                        )
                    }
                    R.id.share -> displayToast("We will set up share process")
                }
                true
            }
            popupMenu.show()
        }
    }

}