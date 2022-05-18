package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.notification

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.galaxytechno.chat.R
import com.galaxytechno.chat.databinding.FragmentNotiBinding
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.google.android.material.tabs.TabLayoutMediator

class NotificationFragment : OtherLvlFragment<FragmentNotiBinding>(FragmentNotiBinding::inflate) {

    override fun setupView() {
        binding.toolbarNoti.tvToolbarTitle.text = getString(R.string.msg_noti)
        super.setupView()

        setupPager()
    }

    override fun setupListener() {
        super.setupListener()
        binding.toolbarNoti.ivBackArrow.setOnClickListener {
            val authNavOptions = NavOptions.Builder()
                .setPopUpTo(R.id.dest_top_home, true)
                .build()
            findNavController().navigate(R.id.action_notificationFragment_to_dest_top_home, Bundle(), authNavOptions)
        }
    }

    private fun setupPager() {
        val notiPagerAdapter = NotiPagerAdapter(
            requireActivity().supportFragmentManager,
            lifecycle
        )
        binding.notiPager.apply {
            this.isUserInputEnabled = true
            this.adapter = notiPagerAdapter
            this.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            this.registerOnPageChangeCallback(notiPagerCallback)
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

        TabLayoutMediator(binding.notiTab, binding.notiPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.activity)
                }
                1 -> {
                    tab.text = getString(R.string.friend_request)
                }
            }
            //todo something
        }.attach()
    }

    private var notiPagerCallback = object : ViewPager2.OnPageChangeCallback() {

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

    override fun onDestroyView() {
        binding.notiPager.unregisterOnPageChangeCallback(notiPagerCallback)
        super.onDestroyView()
    }


}