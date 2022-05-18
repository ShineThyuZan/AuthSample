package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.tabs.creategp.chat_gp_member

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.galaxytechno.chat.R
import com.galaxytechno.chat.databinding.FragmentChatGpMemberBinding
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.galaxytechno.chat.presentation.extension.displayToast
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.my_profile.MyProfileViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatGroupMemberFragment :
    OtherLvlFragment<FragmentChatGpMemberBinding>(FragmentChatGpMemberBinding::inflate) {

    private val vm: MyProfileViewModel by activityViewModels()
    private val args: ChatGroupMemberFragmentArgs by navArgs()

    override fun setupView() {
        super.setupView()
        setupPager()
        binding.tvToolbarTitle.text = getString(R.string.group_name)
        vm.getUserFromDb()
        Glide.with(requireContext())
            .load(args.headUrl)
            .placeholder(R.drawable.ic_yk_logo)
            .into(binding.ivChatUserSecond)
        binding.tvFriName.text = args.name + ","
    }

    override fun setupListener() {
        super.setupListener()

        binding.ivBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.constCall.setOnClickListener {
            displayToast("on clicked call")
        }
        binding.constMute.setOnClickListener {
            displayToast("on clicked mute")
        }
        binding.constLeave.setOnClickListener {
            displayToast("on clicked leave group")
        }
    }

    /** setup Pager create Friend , Message , Feeds, Service */
    private fun setupPager() {
        val gpMemberAdapter = GroupMemberAdapter(
            requireActivity().supportFragmentManager,
            lifecycle
        )
        binding.gpChatPager.apply {
            this.isUserInputEnabled = true
            this.adapter = gpMemberAdapter
            this.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            this.registerOnPageChangeCallback(gpMemberCallBack)
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }


        TabLayoutMediator(binding.gpChatTabs, binding.gpChatPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.member)
                }
                1 -> {
                    tab.text = getString(R.string.photo)
                }
                2 -> {
                    tab.text = getString(R.string.video)
                }
                3 -> {
                    tab.text = getString(R.string.audio)
                }
            }
            //todo something

        }.attach()
    }

    private var gpMemberCallBack = object : ViewPager2.OnPageChangeCallback() {

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

    override fun observe() {
        super.observe()
        vm.userEntity.observe(viewLifecycleOwner) {
            Glide.with(requireContext())
                .load(it.headUrl)
                .placeholder(R.drawable.ic_yk_logo)
                .into(binding.ivChatUserFirst)
            binding.tvYourName.text = ", " + it.name
        }
    }

}