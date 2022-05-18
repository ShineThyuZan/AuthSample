package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.tabs

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.tabs.all.ChatAllFragment

class OrderPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return ChatAllFragment()
            1 -> return ChatPersonalFragment()
            2 -> return ChatGroupsFragment()
        }
        return ChatAllFragment()
    }
}