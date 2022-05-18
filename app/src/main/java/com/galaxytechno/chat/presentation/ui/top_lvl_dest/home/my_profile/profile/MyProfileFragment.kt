package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.my_profile.profile

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.galaxytechno.chat.R
import com.galaxytechno.chat.common.Constant
import com.galaxytechno.chat.databinding.FragmentMyProfileBinding
import com.galaxytechno.chat.model.dto.DataObj
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.presentation.extension.displayToast
import com.galaxytechno.chat.presentation.single_activity.MainActivity
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.feeds.FeedUserActivityAdapter
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.feeds.FeedUserActivityDelegate
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.feeds.FeedViewModel
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.my_profile.MyProfileViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collectLatest


class MyProfileFragment :
    OtherLvlFragment<FragmentMyProfileBinding>(FragmentMyProfileBinding::inflate),
    FeedUserActivityDelegate {

    private val viewModel: FeedViewModel by activityViewModels()
    private val viewModelMyProfile: MyProfileViewModel by activityViewModels()
    private lateinit var feedUserActivityAdapter: FeedUserActivityAdapter
    private var feedUserActivityList: MutableList<DataObj> = mutableListOf()


    override fun setupView() {
        super.setupView()
        viewModelMyProfile.getUserFromDb()
        viewModelMyProfile.requestUserProfileInfo()
        setupPager()
    }

    override fun setupListener() {
        super.setupListener()
        binding.btnMyProfileEdit.setOnClickListener {
            viewModelMyProfile.requestUserProfileInfo()
            findNavController().navigate(R.id.action_myProfileFragment_to_myProfileEditFragment)
        }
        binding.ivProfileBack.setOnClickListener {
            findNavController().navigate(R.id.action_myProfileFragment_to_dest_top_home)
        }
        binding.ivProfileMore.setOnClickListener {
            (activity as MainActivity).comingSoonDialog()
        }
        binding.ivProfileQr.setOnClickListener {
            (activity as MainActivity).comingSoonDialog()
        }
        binding.ivMyProfile.setOnClickListener {
            val directions =
                MyProfileFragmentDirections.actionMyProfileFragmentToFullScreenImageFragment(
                    viewModelMyProfile.userEntity.value!!.headUrl,
                    "fromMyProfile"
                )
            findNavController().navigate(directions)
        }
        binding.ivBackground.setOnClickListener {
            val directions =
                MyProfileFragmentDirections.actionMyProfileFragmentToFullScreenImageFragment(
                    viewModelMyProfile.userEntity.value!!.coverImgUrl,
                    "fromMyProfile"
                )
            findNavController().navigate(directions)
        }

        /** key action on back press */
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_myProfileFragment_to_dest_top_home)
        }
    }

    private fun setupPager() {
        val myProfilePagerAdapter = MyProfilePagerAdapter(
            this.childFragmentManager,
            lifecycle
        )
        binding.myProfilePager.apply {
            this.isUserInputEnabled = true
            this.adapter = myProfilePagerAdapter
            this.orientation = ViewPager2.ORIENTATION_HORIZONTAL

            this.registerOnPageChangeCallback(orderPagerCallback)
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

        TabLayoutMediator(binding.myProfileTab, binding.myProfilePager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.feeds)
                }
//                1 -> {
//                    tab.text = getString(R.string.favorites)
//                }
//                2 -> {
//                    tab.text = getString(R.string.stickers)
//                }
            }
        }.attach()
    }

    private var orderPagerCallback = object : ViewPager2.OnPageChangeCallback() {

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }
    }

    /** user's profile activity recycler view */
    private fun setupFeedsUserActivityRecycler() {
        feedUserActivityAdapter = FeedUserActivityAdapter(requireContext(), this@MyProfileFragment)
        binding.rvMyProfileActivity.apply {
            this.adapter = feedUserActivityAdapter
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.setHasFixedSize(true)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun observe() {
        super.observe()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModelMyProfile.isLoading.collectLatest {
                if (it) {
                    (activity as MainActivity).showLoadingDialog(getString(R.string.loading))
                } else {
                    (activity as MainActivity).hideLoadingDialog()
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
                        it.data.let { bankData ->
                            feedUserActivityList = bankData!!.data as MutableList<DataObj>
                            feedUserActivityAdapter.setNewData(feedUserActivityList)
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModelMyProfile.userProfileInfoEvent.collectLatest {
                when(it){
                    is RemoteEvent.ErrorEvent -> {}
                    is RemoteEvent.LoadingEvent ->{}
                    is RemoteEvent.SuccessEvent -> {

                        val profileData = it.data!!.data

                        //todo change variable for feed count later, this is testing purpose
                        if (profileData!!.totalFriends > 1) {
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
                        binding.tvMyProfileFriends.setOnClickListener {
                            findNavController().navigate(R.id.action_myProfileFragment_to_myProfileFriendFragment)
                        }
                    }
                }
            }
        }


        viewModelMyProfile.userEntity.observe(viewLifecycleOwner) {

            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.profile_placeholder)
                .error(R.drawable.profile_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .priority(Priority.HIGH)

            binding.tvMyProfileName.text = it.name
            binding.tvBio.text = it.bio.ifEmpty { resources.getString(R.string.bio) }
            Glide.with(requireContext())
                .load(it.headUrl)
                .apply(options)
                .into(binding.ivMyProfile)

            val headImage = binding.ivMyProfile.findViewById<ImageView>(R.id.ivMyProfile)
            Glide.with(requireActivity())
                .load(it.coverImgUrl)
                .placeholder(Drawable.createFromPath(Constant.SERVER_IMAGE_URL))
                .into(binding.ivBackground)

        }
    }

    override fun onClickedFeedUserActivity(data: DataObj) {
        displayToast(data.name)
    }
}