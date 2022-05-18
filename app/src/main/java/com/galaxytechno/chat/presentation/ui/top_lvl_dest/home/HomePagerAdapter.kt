package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.pagerFragment.HomeFeedsFragment
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.pagerFragment.HomePeopleFragment
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.pagerFragment.HomeMessageFragment
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.pagerFragment.HomeServiceFragment

class HomePagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return HomePeopleFragment()
            1 -> return HomeMessageFragment()
            2 -> return HomeFeedsFragment()
            3 -> return HomeServiceFragment()
        }
        return HomePeopleFragment()
    }
}