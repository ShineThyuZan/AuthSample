package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.notification

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.galaxytechno.chat.R
import com.galaxytechno.chat.databinding.FragmentRequestConfirmationBinding
import com.galaxytechno.chat.model.dto.DataObj
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.presentation.extension.displayToast
import com.galaxytechno.chat.presentation.single_activity.MainActivity
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.feeds.FeedAdapter
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.feeds.FeedUserActivityAdapter
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.feeds.FeedUserActivityDelegate
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.feeds.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FriendRequestConfirmationFragment :
    OtherLvlFragment<FragmentRequestConfirmationBinding>(FragmentRequestConfirmationBinding::inflate),
    FeedUserActivityDelegate {
    private val vm: NotificationViewModel by activityViewModels()
    private val args: FriendRequestConfirmationFragmentArgs by navArgs()
    private lateinit var feedUserActivityAdapter: FeedUserActivityAdapter
    private lateinit var feedAdapter:FeedAdapter
    private var feedUserActivityList: MutableList<DataObj> = mutableListOf()
    private var feedList: MutableList<DataObj> = mutableListOf()
    private val viewModel: FeedViewModel by activityViewModels()
    private var isFriend: Boolean = false
    private  var status : FriendStatus = FriendStatus.ERROR
    private var friName: String = ""

    enum class FriendStatus {
        ERROR, CANCEL, ACCEPT
    }
    override fun setupView() {

        super.setupView()
        vm.requestedFriProfileData(args.requesterId)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val authNavOptions = NavOptions.Builder()
                .setPopUpTo(R.id.notificationFragment, true)
                .build()
            findNavController().navigate(
                R.id.action_friendRequestConfirmationFragment_to_notificationFragment,
                Bundle(),
                authNavOptions
            )
        }
    }
    override fun setupListener() {
        super.setupListener()
        binding.ivBack.setOnClickListener {
            val authNavOptions = NavOptions.Builder()
                .setPopUpTo(R.id.notificationFragment, true)
                .build()
            findNavController().navigate(
                R.id.action_friendRequestConfirmationFragment_to_notificationFragment,
                Bundle(),
                authNavOptions
            )
        }
        binding.includeBtnConfirmation.btnFriRequestCancel.setOnClickListener {
            status = FriendStatus.CANCEL
            vm.confirmFriendRequest(args.requesterId, false)
            isFriend = false
        }
        binding.includeBtnConfirmation.btnFriRequestAccept.setOnClickListener {
            status = FriendStatus.ACCEPT
            vm.confirmFriendRequest(args.requesterId, true)
            isFriend = true
        }
        binding.includeAlreadyFriend.btnAlreadyFriends.setOnClickListener {
            showConfirmDialog()
        }
        binding.includeAddFriend.btnAddFriends.setOnClickListener {
            vm.requestAddFriend(args.requesterId)
        }
        binding.includeFriendRequested.btnContactProfileRequest.setOnClickListener {
            vm.cancelFriendRequest(args.requesterId)
        }
        binding.ivPhone.setOnClickListener {
            (activity as MainActivity).comingSoonDialog()
        }
        binding.ivChat.setOnClickListener {
            (activity as MainActivity).comingSoonDialog()
        }
        binding.ivProfileMore.setOnClickListener {
            (activity as MainActivity).comingSoonDialog()
        }
    }

    /** user's profile activity recycler view */
    private fun setupFeedsUserActivityRecycler() {
        feedUserActivityAdapter =
            FeedUserActivityAdapter(requireContext(), this@FriendRequestConfirmationFragment)
        binding.rvActivityFeed.apply {
            this.adapter = feedUserActivityAdapter
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.setHasFixedSize(true)
        }
    }

    /** user's profile activity recycler view */
    private fun setupFeedsUserRecycler() {
        feedAdapter =
            FeedAdapter(requireContext(), this@FriendRequestConfirmationFragment)
        binding.rvFeed.apply {
            this.adapter = feedAdapter
            this.layoutManager = LinearLayoutManager(context)
            this.setHasFixedSize(true)
        }
    }

    override fun observe() {
        super.observe()
        /** just show loading */
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            //use StateFlow with collectLatest EVER
            vm.isLoading.collectLatest {
                if (it) {
                    (activity as MainActivity).showLoadingDialog(getString(R.string.response))
                } else {
                    (activity as MainActivity).hideLoadingDialog()
                }
            }
        }
        /** contact profile response */
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            vm.contactProfileResponse.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {}
                    is RemoteEvent.LoadingEvent -> {}
                    is RemoteEvent.SuccessEvent -> {
                        binding.tvFriProfileName.text = it.data!!.data?.name
                        friName = it.data.data!!.name
                        binding.tvFriBio.text = it.data.data.bio ?: getString(R.string.bio)
                        binding.tvFriProfileFriends.text =
                            it.data.data.totalFriends.toString() + " " + getString(R.string.friends)
                        Glide.with(requireActivity())
                            .load(it.data.data.headUrl)
                            .placeholder(R.drawable.profile_placeholder)
                            .into(binding.ivFriProfile)
                        val headImage = it.data.data.headUrl
                        binding.ivFriProfile.setOnClickListener {
                            val directions =
                                FriendRequestConfirmationFragmentDirections.actionFriendRequestConfirmationFragmentToFullScreenImageFragment(
                                    headImage.toString(),
                                    "fromFriendRequestedConfirmation"
                                )
                            findNavController().navigate(directions)
                        }
                        Glide.with(requireActivity())
                            .load(it.data.data.coverImgUrl)
                            .placeholder(R.drawable.place_holder)
                            .into(binding.ivFriBackground)
                        val coverImage = it.data.data.coverImgUrl
                        binding.ivFriBackground.setOnClickListener {
                            val directions =
                                FriendRequestConfirmationFragmentDirections.actionFriendRequestConfirmationFragmentToFullScreenImageFragment(
                                    coverImage.toString(),
                                    "fromFriendRequestedConfirmation="
                                )
                            findNavController().navigate(directions)
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            vm.unfriendResponse.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        displayToast("error unfri")
                        btnAlreadyFriendView()
                    }
                    is RemoteEvent.LoadingEvent -> {}
                    is RemoteEvent.SuccessEvent -> {
                        displayToast("success unfriend ")
                        btnAddFriendView()
//                        btnFriendRequestAcceptView()

                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            vm.confirmFriendRequest.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        status = FriendStatus.ERROR
                    }
                    is RemoteEvent.LoadingEvent -> {}
                    is RemoteEvent.SuccessEvent -> {
                        isFriend = status != FriendStatus.CANCEL

                        if (status != FriendStatus.ERROR){
                            if (isFriend) {
                                displayToast("success friend")
                                btnAlreadyFriendView()
                            } else {
                                displayToast("success cancel friend")
                                btnAddFriendView()

                            }
                        }else{
                            if(isFriend){
                                displayToast("error btn friend")
                            }else{
                                displayToast("error btn cancel")
                            }

                            btnFriendRequestAcceptView()
                        }



                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            vm.addFriendResponse.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {}
                    is RemoteEvent.LoadingEvent -> {}
                    is RemoteEvent.SuccessEvent -> {
                        btnFriendRequestedView()
                    }
                }
            }
        }


        viewModel.bankObj.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {

                when (it) {
                    is RemoteEvent.ErrorEvent -> {

                    }
                    is RemoteEvent.LoadingEvent -> {

                    }
                    is RemoteEvent.SuccessEvent -> {
                        setupFeedsUserActivityRecycler()
                        setupFeedsUserRecycler()
                        it.data.let { bankData ->
                            feedUserActivityList = bankData!!.data as MutableList<DataObj>
                            feedUserActivityAdapter.setNewData(feedUserActivityList)
                            feedAdapter.setNewData(feedUserActivityList)
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            vm.cancelFriRequest.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {}
                    is RemoteEvent.LoadingEvent -> {}
                    is RemoteEvent.SuccessEvent -> {
                        btnAddFriendView()
                    }
                }
            }
        }


    }

    override fun onClickedFeedUserActivity(data: DataObj) {
//        displayToast(data.name)
    }

    /** show/hide friend status method */

    private fun btnAddFriendView() {
        binding.includeAddFriend.root.visibility = View.VISIBLE
        binding.includeFriendRequested.root.visibility = View.GONE
        binding.includeBtnConfirmation.root.visibility = View.GONE
        binding.includeAlreadyFriend.root.visibility = View.GONE

    }

    private fun btnAlreadyFriendView() {
        binding.includeAddFriend.root.visibility = View.GONE
        binding.includeFriendRequested.root.visibility = View.GONE
        binding.includeBtnConfirmation.root.visibility = View.GONE
        binding.includeAlreadyFriend.root.visibility = View.VISIBLE
    }

    private fun btnFriendRequestedView() {
        binding.includeAddFriend.root.visibility = View.GONE
        binding.includeFriendRequested.root.visibility = View.VISIBLE
        binding.includeBtnConfirmation.root.visibility = View.GONE
        binding.includeAlreadyFriend.root.visibility = View.GONE
    }

    private fun btnFriendRequestAcceptView() {
        binding.includeAddFriend.root.visibility = View.GONE
        binding.includeFriendRequested.root.visibility = View.GONE
        binding.includeBtnConfirmation.root.visibility = View.VISIBLE
        binding.includeAlreadyFriend.root.visibility = View.GONE
    }

    /** unfriend confirm dialog */
    private fun showConfirmDialog() {
        val confirmBtn: Button
        val cancelBtn: Button
        val titleTextView: TextView
        val warningTextView: TextView
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_confirmation)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        /** animate dialog with window animation */
        dialog.window!!.setWindowAnimations(R.style.AnimationForDialog)
        dialog.setCanceledOnTouchOutside(false)
        titleTextView = dialog.findViewById(R.id.tv_title)
        warningTextView = dialog.findViewById(R.id.tv_warning)
        titleTextView.setText(R.string.unfriend)
        warningTextView.text = getString(
            R.string.unfriend_warning,
            friName
        )
        confirmBtn = dialog.findViewById(R.id.btn_confirm) as Button
        cancelBtn = dialog.findViewById(R.id.btn_cancel) as Button

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        confirmBtn.setOnClickListener {
            vm.unFriendRequest(friendId = args.requesterId.toLong())
            dialog.dismiss()
        }
        dialog.show()
    }


}