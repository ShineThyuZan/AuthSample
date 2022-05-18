package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.pagerFragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.galaxytechno.chat.databinding.FragmentHomeMessageBinding
import com.galaxytechno.chat.model.dto.UserVos
import com.galaxytechno.chat.model.vos.FriListVos
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.presentation.extension.displaySnack
import com.galaxytechno.chat.presentation.extension.displayToast
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.tabs.all.ChatRecentMsgAdapter
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.tabs.all.ChatRecentMsgDelegate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeMessageFragment :
    OtherLvlFragment<FragmentHomeMessageBinding>(FragmentHomeMessageBinding::inflate)
     {

}