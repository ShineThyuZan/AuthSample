package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.contact

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.galaxytechno.chat.R
import com.galaxytechno.chat.common.Constant
import com.galaxytechno.chat.databinding.FragmentContactProfileBinding
import com.galaxytechno.chat.model.dto.DataObj
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.presentation.extension.displaySnack
import com.galaxytechno.chat.presentation.extension.displayToast
import com.galaxytechno.chat.presentation.single_activity.MainActivity
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.feeds.FeedAdapter
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.feeds.FeedUserActivityAdapter
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.feeds.FeedUserActivityDelegate
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.feeds.FeedViewModel
import kotlinx.coroutines.flow.collectLatest

class FriContactProfileFragment : OtherLvlFragment<FragmentContactProfileBinding>(
    FragmentContactProfileBinding::inflate
), FeedUserActivityDelegate {
    private val vm: FriContactViewModel by activityViewModels()
    private val viewModel: FeedViewModel by activityViewModels()
    private lateinit var feedUserActivityAdapter: FeedUserActivityAdapter
    private lateinit var feedAdapter: FeedAdapter
    private var feedUserActivityList: MutableList<DataObj> = mutableListOf()
    override fun initialize() {
        super.initialize()
        vm.contactProfileDetail()
    }

    override fun setupListener() {
        super.setupListener()

        binding.btnAddFriendContact.btnAddFriends.setOnClickListener {
            binding.btnAddFriendContact.root.visibility = View.GONE
            binding.btnLoading.root.visibility = View.VISIBLE
            vm.requestAddFriend()
        }

        binding.ivProfileBack.setOnClickListener {
            findNavController().navigate(R.id.action_friContactProfileFragment_to_friContactFragment)
        }

        binding.btnAlreadyFriendContact.btnAlreadyFriends.setOnClickListener {
            showConfirmDialog()
        }

        binding.ivMore.setOnClickListener {
            findNavController().navigate(R.id.action_friContactProfileFragment_to_moreActionBottomSheetFragmentContact)
        }

        binding.btnRequestAccept.btnFriRequestAccept.setOnClickListener {
            binding.btnLoading.root.visibility = View.VISIBLE
            vm.acceptFriendRequest()

        }

        binding.btnRequestAccept.btnFriRequestCancel.setOnClickListener {
            binding.btnLoading.root.visibility = View.VISIBLE
            vm.cancelFriendRequest()

        }
        binding.btnFriendRequested.btnContactProfileRequest.setOnClickListener {
            binding.btnFriendRequested.root.visibility = View.GONE
            binding.btnLoading.root.visibility = View.VISIBLE
            vm.friRequestedCancel()
        }
    }

    /** user's profile activity recycler view */
    private fun setupFeedsUserActivityRecycler() {
        feedUserActivityAdapter =
            FeedUserActivityAdapter(requireContext(), this@FriContactProfileFragment)
        binding.rvMyProfileActivity.apply {
            this.adapter = feedUserActivityAdapter
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.setHasFixedSize(true)
        }

    }

    /** user's profile activity recycler view */
    private fun setupFeedRecycler() {
        feedAdapter = FeedAdapter(requireContext(), this@FriContactProfileFragment)
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
                    is RemoteEvent.ErrorEvent -> {

                    }
                    is RemoteEvent.LoadingEvent -> {

                    }
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
            vm.addFriendResponse.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {}
                    is RemoteEvent.LoadingEvent -> {}
                    is RemoteEvent.SuccessEvent -> {
                        if (it.data!!.status == Constant.STATUS_SUCCESS) {
                            btnFriendRequestedView()
                            binding.btnLoading.root.visibility = View.GONE
                        } else {
                            displayToast(it.data.error)
                        }
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            vm.contactProfileResponse.collectLatest { it ->
                when (it) {
                    is RemoteEvent.ErrorEvent -> {}
                    is RemoteEvent.LoadingEvent -> {}
                    is RemoteEvent.SuccessEvent -> {
                        val profileData = it.data?.data!!


                        /** coverImage Data binding and Click action  */
                        profileData.coverImgUrl?.let {
                            Glide.with(requireContext())
                                .load(it)
                                .placeholder(R.drawable.ic_splash_screen)
                                .into(binding.ivBackground)
                        }
                        binding.ivBackground.setOnClickListener {
                            val direction =
                                FriContactProfileFragmentDirections.actionFriContactProfileFragmentToFullScreenImageFragment(
                                    profileData.coverImgUrl.toString(),
                                    "fromFriContactProfile"
                                )
                            findNavController().navigate(direction)
                        }

                        /** profileImage Data binding and Click action  */
                        profileData.headUrl?.let {
                            Glide.with(requireContext())
                                .load(it)
                                .placeholder(R.drawable.ic_splash_screen)
                                .into(binding.ivMyProfile)
                        }
                        binding.ivMyProfile.setOnClickListener {
                            val direction =
                                FriContactProfileFragmentDirections.actionFriContactProfileFragmentToFullScreenImageFragment(
                                    profileData.headUrl.toString(),
                                    "fromFriContactProfile"
                                )
                            findNavController().navigate(direction)
                        }

                        binding.ivPhone.setOnClickListener {
                            (activity as MainActivity).comingSoonDialog()
                        }
                        /** navigate personal chat screen with arguments */
                        binding.ivMessage.setOnClickListener {
                            (activity as MainActivity).comingSoonDialog()

//                            val directions =
//                                FriContactProfileFragmentDirections.actionFriContactProfileFragmentToPersonalChatRoomFragment(
//                                    vm.contactFriObj.value!!.name,
//                                    vm.contactFriObj.value!!.userId,
//                                    10002,
//                                    profileData.headUrl.toString(),
//                                    "fromFriContactProfile"
//                                )
//                            findNavController().navigate(directions)
                        }

                        /** bio and Profile name binding from api data */
                        binding.tvMyProfileName.text = profileData.name

                        if (profileData.bio.isNullOrEmpty()) {
                            binding.tvBio.text = resources.getString(R.string.bio)
                        } else {
                            binding.tvBio.text = profileData.bio
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
                            profileLockView()
                        } else {
                            profileUnlockView()
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
                            5 -> {
                                accountBlockedView()
                            }
                        }
                    }
                }
            }
        }

        /** unfriend api call response */
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
            vm.acceptFriRequest.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {}
                    is RemoteEvent.LoadingEvent -> {}
                    is RemoteEvent.SuccessEvent -> {
                        vm.setLoadingState(false)
                        btnAlreadyFriendView()
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
                        vm.setLoadingState(false)
                        btnAddFriendView()
                    }
                }
            }
        }

        /** observing block response */
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vm.blockResponse.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        displayToast(it.data?.error ?: "Error")
                    }
                    is RemoteEvent.LoadingEvent -> {
                        vm.setLoadingState(true)
                    }
                    is RemoteEvent.SuccessEvent -> {
                        vm.setLoadingState(false)
                        accountBlockedView()
                        findNavController().popBackStack()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            vm.friRequestedCancel.collectLatest {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {}
                    is RemoteEvent.LoadingEvent -> {}
                    is RemoteEvent.SuccessEvent -> {
                        vm.setLoadingState(false)
                        btnAddFriendView()
                    }
                }
            }
        }
    }

    /** show/hide friend status method */
    private fun btnAddFriendView() {
        binding.btnAddFriendContact.root.visibility = View.VISIBLE
        binding.btnLoading.root.visibility = View.GONE
        binding.btnFriendRequested.root.visibility = View.GONE
        binding.btnRequestAccept.root.visibility = View.GONE
        binding.btnAlreadyFriendContact.root.visibility = View.GONE
        binding.btnAlreadyBlockContact.root.visibility = View.GONE
    }

    private fun btnAlreadyFriendView() {
        binding.btnAlreadyFriendContact.root.visibility = View.VISIBLE
        binding.btnFriendRequested.root.visibility = View.GONE
        binding.btnAddFriendContact.root.visibility = View.GONE
        binding.btnRequestAccept.root.visibility = View.GONE
        binding.btnAlreadyBlockContact.root.visibility = View.GONE
    }

    private fun btnFriendRequestedView() {
        binding.btnFriendRequested.root.visibility = View.VISIBLE
        binding.btnLoading.root.visibility = View.GONE
        binding.btnAddFriendContact.root.visibility = View.GONE
        binding.btnRequestAccept.root.visibility = View.GONE
        binding.btnAlreadyFriendContact.root.visibility = View.GONE
        binding.btnAlreadyBlockContact.root.visibility = View.GONE
    }

    private fun btnFriendRequestAcceptView() {
        binding.btnRequestAccept.root.visibility = View.VISIBLE
        binding.btnLoading.root.visibility = View.GONE
        binding.btnFriendRequested.root.visibility = View.GONE
        binding.btnAddFriendContact.root.visibility = View.GONE
        binding.btnAlreadyFriendContact.root.visibility = View.GONE
        binding.btnAlreadyBlockContact.root.visibility = View.GONE
    }

    private fun accountBlockedView() {
        binding.btnAlreadyBlockContact.root.visibility = View.VISIBLE
        binding.btnLoading.root.visibility = View.GONE
        binding.btnRequestAccept.root.visibility = View.GONE
        binding.btnFriendRequested.root.visibility = View.GONE
        binding.btnAddFriendContact.root.visibility = View.GONE
        binding.btnAlreadyFriendContact.root.visibility = View.GONE

        binding.lyAccountBlocked.root.visibility = View.VISIBLE
        binding.lyAccountLocked.root.visibility = View.GONE
        binding.rvMyProfileActivity.visibility = View.GONE
        binding.tvMyProfileFeed.visibility = View.GONE
        binding.tvMyProfileFriends.visibility = View.GONE
    }

    private fun profileLockView() {
        binding.lyAccountLocked.root.visibility = View.VISIBLE
        binding.rvMyProfileActivity.visibility = View.GONE
        binding.tvMyProfileFeed.visibility = View.GONE
        binding.tvMyProfileFriends.visibility = View.GONE
    }

    private fun profileUnlockView() {
        binding.lyAccountLocked.root.visibility = View.GONE
        binding.rvMyProfileActivity.visibility = View.VISIBLE
        binding.tvMyProfileFeed.visibility = View.VISIBLE
        binding.tvMyProfileFriends.visibility = View.VISIBLE
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
            vm.contactFriObj.value!!.name
        )
        confirmBtn = dialog.findViewById(R.id.btn_confirm) as Button
        cancelBtn = dialog.findViewById(R.id.btn_cancel) as Button

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        confirmBtn.setOnClickListener {
            binding.btnAlreadyFriendContact.root.visibility = View.GONE
            binding.btnLoading.root.visibility = View.VISIBLE
            vm.unFriendRequest()
            dialog.dismiss()
        }
        dialog.show()
    }

    /** user activity feeds click action */
    override fun onClickedFeedUserActivity(data: DataObj) {
        displayToast(data.name)
    }


}