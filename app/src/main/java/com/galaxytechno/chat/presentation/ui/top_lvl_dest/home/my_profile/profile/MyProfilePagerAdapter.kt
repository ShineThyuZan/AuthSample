package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.my_profile.profile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyProfilePagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 1
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return MyProfileFeedFragment()
//            1 -> return MyProfileFavoritesFragment()
//            2 -> return MyProfileStickerFragment()
        }
        return MyProfileFragment()
    }
}