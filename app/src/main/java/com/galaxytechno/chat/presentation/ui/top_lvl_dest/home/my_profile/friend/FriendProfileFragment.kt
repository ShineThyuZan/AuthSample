package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.my_profile.friend

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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.galaxytechno.chat.R
import com.galaxytechno.chat.databinding.FragmentFriendProfileBinding
import com.galaxytechno.chat.model.dto.DataObj
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.presentation.extension.displayToast
import com.galaxytechno.chat.presentation.single_activity.MainActivity
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.feeds.FeedAdapter
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.feeds.FeedUserActivityAdapter
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.feeds.FeedUserActivityDelegate
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.feeds.FeedViewModel
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.my_profile.MyProfileViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest


class FriendProfileFragment : OtherLvlFragment<FragmentFriendProfileBinding>(
    FragmentFriendProfileBinding::inflate
), FeedUserActivityDelegate {
    private val args: FriendProfileFragmentArgs by navArgs()
    val vm: MyProfileViewModel by activityViewModels()
    private var isFriend: Boolean = false
    private val viewModel: FeedViewModel by activityViewModels()
    private lateinit var feedUserActivityAdapter: FeedUserActivityAdapter
    private lateinit var feedAdapter: FeedAdapter
    private var feedUserActivityList: MutableList<DataObj> = mutableListOf()

    override fun initialize() {
        super.initialize()
        // vm.friendProfileDetail(args.userId)
        vm.saveFriendId(args.userId)

    }

    override fun setupListener() {
        super.setupListener()
        binding.ivProfileBack.setOnClickListener {
            when (args.navFlag) {


                "fromHomePeople" -> {
                    findNavController().navigate(R.id.action_friendProfileFragment_to_home_search)
                }

                "fromMyProfileFriend" -> {
//                    val authNavOptions = NavOptions.Builder()
//                        .setPopUpTo(R.id.myProfileFriendFragment, true)
//                        .build()
                    findNavController().navigate(
                        R.id.action_friendProfileFragment_to_myProfileFriendFragment
//                        Bundle(),
//                        authNavOptions
                    )
                }

            }
        }
        /** key action on back press */
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            when (args.navFlag) {
                "fromHomePeople" -> {

                    findNavController().navigate(
                        R.id.action_friendProfileFragment_to_home_search
                    )
                }

                "fromMyProfileFriend" -> {
                    findNavController().navigate(
                        R.id.action_friendProfileFragment_to_myProfileFriendFragment
                    )
                }

            }
        }

        binding.btnAddFriendContact.btnAddFriends.setOnClickListener {
            vm.setLoadingState(true)
            vm.requestAddFriend(args.userId)
        }
        binding.btnAlreadyFriendContact.btnAlreadyFriends.setOnClickListener {
            showConfirmDialog()
        }
        binding.btnFriendRequested.btnContactProfileRequest.setOnClickListener {
            (activity as MainActivity).comingSoonDialog()
        }

        binding.ivMore.setOnClickListener {
            findNavController().navigate(R.id.action_friendProfileFragment_to_moreActionBottomSheetFragmentFriend)
        }

        binding.ivMessage.setOnClickListener {
            (activity as MainActivity).comingSoonDialog()
        }
        binding.ivPhone.setOnClickListener {
            (activity as MainActivity).comingSoonDialog()
        }

        binding.btnRequestAccept.btnFriRequestAccept.setOnClickListener {
            vm.setLoadingState(true)
            vm.confirmFriendRequest(args.userId, true)
            isFriend = true
        }
        binding.btnRequestAccept.btnFriRequestCancel.setOnClickListener {
            vm.setLoadingState(true)
            vm.confirmFriendRequest(args.userId, false)
            isFriend = false
        }
    }

    /** user's profile activity recycler view */
    private fun setupFeedsUserActivityRecycler() {
        feedUserActivityAdapter = FeedUserActivityAdapter(requireContext(), this@FriendProfileFragment)
        binding.rvMyProfileActivity.apply {
            this.adapter = feedUserActivityAdapter
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.setHasFixedSize(true)
        }
    }

    /** user's profile activity recycler view */
    private fun setupFeedRecycler() {
        feedAdapter = FeedAdapter(requireContext(), this@FriendProfileFragment)
        binding.rvFeed.apply {
            this.adapter = feedAdapter
            this.layoutManager = LinearLayoutManager(context)
            this.setHasFixedSize(true)
        }
    }

    override fun observe() {
        super.observe()

        viewModel.bankObj.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {

                when (it) {
                    is RemoteEvent.ErrorEvent -> {}
                    is RemoteEvent.LoadingEvent -> {}
                    is RemoteEvent.SuccessEvent -> {
                        setupFeedsUserActivityRecycler()
                        setupFeedRecycler()
                        it.data.let { bankData ->
                            feedUserActivityList = bankData!!.data as MutableList<DataObj>
                            feedUserActivityAdapter.setNewData(feedUserActivityList)

                            feedAdapter.setNewData(feedUserActivityList)
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vm.friendProfileResponse.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {}
                    is RemoteEvent.LoadingEvent -> {}
                    is RemoteEvent.SuccessEvent -> {

                        val profileData = it.data?.data!!

                        /** binding cover image and click screen navigation */
                        val coverImgUrl = it.data.data.coverImgUrl
                        profileData.coverImgUrl?.let { coverImageUrl ->
                            Glide.with(requireContext())
                                .load(coverImageUrl)
                                .into(binding.ivBackground)
                        }

                        binding.ivBackground.setOnClickListener {
                            val directions =
                                FriendProfileFragmentDirections.actionFriendProfileFragmentToFullScreenImageFragment(
                                    coverImgUrl.toString(),
                                    "fromFriProfile"
                                )
                            findNavController().navigate(directions)
                        }
                        /** binding profile image and click screen navigation */
                        val profileImgUrl = it.data.data.headUrl
                            ?: "https://galaxyshopbucket.s3.ap-southeast-1.amazonaws.com/MASTER_DATA_FILES/0b24d3c7-896d-4450-85f9-11672ff27852.png"

                        profileImgUrl.let { headUrl ->
                            Glide.with(requireContext())
                                .load(headUrl)
                                .placeholder(R.drawable.ic_profile)
                                .into(binding.ivMyProfile)
                        }
                        binding.ivMyProfile.setOnClickListener {
                            val directions =
                                FriendProfileFragmentDirections.actionFriendProfileFragmentToFullScreenImageFragment(
                                    profileImgUrl.toString(),
                                    "fromFriProfile"
                                )
                            findNavController().navigate(directions)
                        }

                        binding.tvMyProfileName.text = profileData.name

                        /** parameter passing and screen navigation to personal chat screen (if headUrl empty send placeholder server image)*/
//                        binding.ivMessage.setOnClickListener {
//                            val directions =
//                                FriendProfileFragmentDirections.actionFriendProfileFragmentToPersonalChatRoomFragment(
//                                    profileData.name,
//                                    profileData.userId,
//                                    10001,
//                                    profileData.headUrl!!.ifEmpty { "https://galaxyshopbucket.s3.ap-southeast-1.amazonaws.com/MASTER_DATA_FILES/0b24d3c7-896d-4450-85f9-11672ff27852.png" },
//                                    "fromFriendProfile"
//                                )
//                            findNavController().navigate(directions)
//                        }

                        profileData.bio.also { bio ->
                            binding.tvBio.text = bio ?: getString(R.string.bio)
                        }

                        //todo change variable for feed count later, this is testing purpose
                        if (profileData.totalFriends > 1) {
                            getString(
                                R.string.feeds_count,
                                profileData.totalFriends
                            ).also { binding.tvMyProfileFeed.text = it }
                        } else {
                            getString(
                                R.string.feed_count,
                                profileData.totalFriends
                            ).also { binding.tvMyProfileFeed.text = it }
                        }

                        if (profileData.totalFriends > 1) {
                            getString(
                                R.string.friends_count,
                                profileData.totalFriends
                            ).also { binding.tvMyProfileFriends.text = it }
                        } else {
                            getString(
                                R.string.friend_count,
                                profileData.totalFriends
                            ).also { binding.tvMyProfileFriends.text = it }
                        }

                        if (profileData.isProfileLock) {
                            binding.lyAccountLocked.clAccountLocked.visibility = View.VISIBLE
                            binding.rvMyProfileActivity.visibility = View.GONE
                        } else {
                            binding.lyAccountLocked.clAccountLocked.visibility = View.GONE
                            binding.rvMyProfileActivity.visibility = View.VISIBLE
                        }

                        when (profileData.friendStatus) {
                            1 -> {
                                btnAddFriendView()
                            }
                            2 -> {
                                btnFriendRequestedView()
                            }

                            3 -> {
                                btnFriendRequestAcceptView()
                            }
                            4 -> {
                                btnAlreadyFriendView()
                            }
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vm.unfriendResponse.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        displayToast(it.data?.error ?: "Error")
                    }
                    is RemoteEvent.LoadingEvent -> {
                        vm.setLoadingState(true)
                    }
                    is RemoteEvent.SuccessEvent -> {
                        vm.setLoadingState(false)
                        btnAddFriendView()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            vm.confirmFriRequest.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {}
                    is RemoteEvent.LoadingEvent -> {}
                    is RemoteEvent.SuccessEvent -> {
                        if (!isFriend) {
                            btnAddFriendView()
                        } else {
                            btnAlreadyFriendView()
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

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            //use StateFlow with collectLatest EVER
            vm.isLoading.collectLatest {
                if (it) {
                    (activity as MainActivity).showLoadingDialog(getString(R.string.checking))
                } else {
                    (activity as MainActivity).hideLoadingDialog()
                }
            }
        }

    }

    private fun btnAddFriendView() {
        binding.btnAddFriendContact.root.visibility = View.VISIBLE
        binding.btnFriendRequested.root.visibility = View.GONE
        binding.btnRequestAccept.root.visibility = View.GONE
        binding.btnAlreadyFriendContact.root.visibility = View.GONE
    }

    private fun btnAlreadyFriendView() {
        binding.btnAlreadyFriendContact.root.visibility = View.VISIBLE
        binding.btnFriendRequested.root.visibility = View.GONE
        binding.btnAddFriendContact.root.visibility = View.GONE
        binding.btnRequestAccept.root.visibility = View.GONE
    }

    private fun btnFriendRequestedView() {
        binding.btnFriendRequested.root.visibility = View.VISIBLE
        binding.btnAddFriendContact.root.visibility = View.GONE
        binding.btnRequestAccept.root.visibility = View.GONE
        binding.btnAlreadyFriendContact.root.visibility = View.GONE
    }

    private fun btnFriendRequestAcceptView() {
        binding.btnRequestAccept.root.visibility = View.VISIBLE
        binding.btnFriendRequested.root.visibility = View.GONE
        binding.btnAddFriendContact.root.visibility = View.GONE
        binding.btnAlreadyFriendContact.root.visibility = View.GONE
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
        titleTextView.setText(R.string.unfriend)
        warningTextView = dialog.findViewById(R.id.tv_warning)
        warningTextView.text = getString(
            R.string.unfriend_warning,
            vm.friendProfileResponse.value.data?.data?.name
        )
        confirmBtn = dialog.findViewById(R.id.btn_confirm) as Button
        cancelBtn = dialog.findViewById(R.id.btn_cancel) as Button

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        confirmBtn.setOnClickListener {
            vm.setLoadingState(true)
            vm.unFriendRequest()
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onClickedFeedUserActivity(data: DataObj) {
       displayToast(data.name)
    }
}