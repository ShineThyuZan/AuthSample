package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.galaxytechno.chat.R
import com.galaxytechno.chat.databinding.TopLvlChatBinding
import com.galaxytechno.chat.presentation.base.TopFragment
import com.galaxytechno.chat.presentation.single_activity.MainActivity
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.tabs.OrderPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopChatFragment : TopFragment<TopLvlChatBinding>(TopLvlChatBinding::inflate) {
    override fun setupView() {
        super.setupView()
        setupPager()
    }

    override fun setupListener() {
        super.setupListener()
        binding.ivSearchView.setOnClickListener {
        //findNavController().navigate(R.id.action_dest_top_chat_to_chatSearchFragment)
            (activity as MainActivity).comingSoonDialog()
        }
        binding.ivChatGroup.setOnClickListener {
            findNavController().navigate(R.id.action_addButton_to_newMessage)
        }
        binding.ivProfileBack.setOnClickListener {
            (activity as MainActivity).comingSoonDialog()
        }
    }

    private fun setupPager() {
        val orderAdapter = OrderPagerAdapter(
            this.childFragmentManager,
            lifecycle
        )
        binding.orderPager.apply {
            this.isUserInputEnabled = true
            this.adapter = orderAdapter
            this.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            this.registerOnPageChangeCallback(orderPagerCallback)
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

        TabLayoutMediator(binding.chatTab, binding.orderPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.order_all)
                }
                1 -> {
                    tab.text = getString(R.string.presonal)
                }
                2 -> {
                    tab.text = getString(R.string.group)
                }
            }
            //todo something
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

    override fun onDestroyView() {
        binding.orderPager.unregisterOnPageChangeCallback(orderPagerCallback)
        super.onDestroyView()
    }

}