package com.galaxytechno.chat.presentation.ui.top_lvl_dest.feeds

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.galaxytechno.chat.databinding.FragmentFeedSearchBinding
import com.galaxytechno.chat.model.dto.Country
import com.galaxytechno.chat.model.dto.UserVos
import com.galaxytechno.chat.model.vos.FriListVos
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.presentation.extension.displaySnack
import com.galaxytechno.chat.presentation.extension.displayToast
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.tabs.all.ChatRecentMsgAdapter
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.tabs.all.ChatRecentMsgDelegate
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.tabs.all.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedSearchFragment : OtherLvlFragment<FragmentFeedSearchBinding>(FragmentFeedSearchBinding::inflate),
    ChatRecentMsgDelegate {

    private val viewModel: ChatViewModel by viewModels()
    private lateinit var chatRecentMsgAdapter: ChatRecentMsgAdapter
    private var msgRecentList: MutableList<FriListVos> = mutableListOf()

    override fun setupView() {
        super.setupView()
        viewModel.getChatRecentMsg()
    }

    override fun setupListener() {
        super.setupListener()
        binding.ivBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setUpCountryListRecyclerView() {
        chatRecentMsgAdapter = ChatRecentMsgAdapter(requireContext(), this@FeedSearchFragment)
        binding.rvRecentMsg.apply {
            this.adapter = chatRecentMsgAdapter
            this.layoutManager = LinearLayoutManager(context)
            this.setHasFixedSize(true)
        }
    }

    override fun observe() {
        super.observe()
        viewModel.countryObj.observe(viewLifecycleOwner) {
            when (it) {
                is RemoteEvent.SuccessEvent -> {
                    setUpCountryListRecyclerView()
//                    if (it.data!!.data!!.countryList.isNullOrEmpty()) {
//                        binding.constRecent.isVisible = false
//                        binding.offlineState.constOfflineState.isVisible = true
//                    } else {
//                        msgRecentList = it.data.data!!.countryList as MutableList<FriListVos>
//                        chatRecentMsgAdapter.setNewData(msgRecentList)
//                    }

                }
                is RemoteEvent.ErrorEvent -> {
                    displayToast(it.message ?: "Error ")
                }
                is RemoteEvent.LoadingEvent -> {
                    // todo shimmer loading for country
                }
            }
        }
    }


    override fun onClickedRecentMsg(data: UserVos) {
        binding.root.displaySnack(data.name)
    }

}