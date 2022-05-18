package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.tabs.creategp.chat_gp_member

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class GroupMemberAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return ChatGpMemberListFragment()
            1 -> return ChatGpPhotoFragment()
            2 -> return ChatGpVideoFragment()
            3 -> return ChatGpAudioFragment()
        }
        return ChatGpMemberListFragment()
    }
}