package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.galaxytechno.chat.R
import com.galaxytechno.chat.common.Constant
import com.galaxytechno.chat.databinding.FragmentPersonalChatRoomBinding
import com.galaxytechno.chat.model.dto.MessageObj
import com.galaxytechno.chat.presentation.base.OtherLvlFragment
import com.galaxytechno.chat.presentation.extension.displayToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class PersonalChatRoomFragment : OtherLvlFragment<FragmentPersonalChatRoomBinding>(
    FragmentPersonalChatRoomBinding::inflate
) {
    private val args: PersonalChatRoomFragmentArgs by navArgs()
    private var isMessageIcon = MutableStateFlow(false)
    private val messageIcon: Boolean = true
    private val audioIcon: Boolean = false
    private val chatList: ArrayList<MessageObj> = arrayListOf()
    lateinit var chatRoomAdapter: PersonalChatRoomAdapter
    val vm: PersonalChatRoomViewModel by viewModels()

    override fun initialize() {
        super.initialize()
        vm.joinRoom()
        chatList.clear()
        chatRoomAdapter = PersonalChatRoomAdapter(this.requireContext(), chatList)
        binding.rvMessage.adapter = chatRoomAdapter

        val layoutManager = LinearLayoutManager(this.requireContext())
        binding.rvMessage.layoutManager = layoutManager
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vm.leaveRoom()
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.leaveRoom()
    }

    override fun setupListener() {
        super.setupListener()
        binding.tbChat.ivBackArrow.setOnClickListener {
            when (args.navFlag) {
                "fromFriContactProfile" -> {
                    findNavController().navigate(R.id.action_personalChatRoomFragment_to_friContactProfileFragment)
                }
                "fromChatNewMsg" -> {
                    findNavController().navigate(R.id.action_personalChatRoomFragment_to_chatNewMessageFragment)
                }

                "fromFriendProfile" -> {
                    findNavController().popBackStack()
                }
            }
        }

        binding.tilMessage.setStartIconOnClickListener {
            findNavController().navigate(R.id.action_personalChatRoomFragment_to_sendMessageBottomSheetFragment)
        }

        binding.tbChat.clToolbarChat.setOnClickListener {
            val directions =
                PersonalChatRoomFragmentDirections.actionPersonalChatRoomFragmentToChatGroupMemberFragment(
                    args.receiverId,
                    args.receiverName,
                    args.imageUrl.ifEmpty { Constant.SERVER_IMAGE_URL },
                )
            findNavController().navigate(directions)

        }

        binding.etMessage.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    binding.tilMessage.setEndIconDrawable(R.drawable.ic_send_message)
                    isMessageIcon.value = messageIcon

                } else {
                    binding.tilMessage.setEndIconDrawable(R.drawable.ic_mice_gray)
                    isMessageIcon.value = audioIcon
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable) {}
        })

        binding.etMessage.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                hideKeyBoard()
                var message = MessageObj(
                    headUrl = binding.etMessage.text!!.trim().toString(),
                    messageContent = binding.etMessage.text.toString(),
                    messageType = 1,
                    contentType = 1,
                    receiverId = 1002,
                    viewType = 0
                )
                vm.sendMessage(binding.etMessage.text.toString())
                addItemToRecyclerView(message)
                binding.tvPlaceHolder.visibility = View.GONE
                displayToast("This is Message")
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    override fun setupView() {
        super.setupView()
        /** binding name and image url in screen */
        binding.tbChat.tvName.text = args.receiverName
        Glide.with(requireActivity())
            .load(args.imageUrl)
            .into(binding.tbChat.ivProfile)
    }

    override fun observe() {
        super.observe()
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            vm.conservationData.collectLatest {
                Log.d("soccccket", "$it")
                if (it != "") {
                    val message = MessageObj(
                        headUrl = binding.etMessage.text!!.trim().toString(),
                        messageContent = it,
                        messageType = 1,
                        contentType = 1,
                        receiverId = 1001,
                        1
                    )
                    vm.sendMessage(binding.etMessage.text.toString())
                    binding.tvPlaceHolder.visibility = View.GONE
                    addItemToRecyclerView(message)
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            isMessageIcon.collectLatest {
                if (isMessageIcon.value) {
                    binding.tilMessage.setEndIconOnClickListener {
                        var message = MessageObj(
                            headUrl = binding.etMessage.text!!.trim().toString(),
                            messageContent = binding.etMessage.text.toString(),
                            messageType = 1,
                            contentType = 1,
                            receiverId = 1002,
                            viewType = 0
                        )
                        vm.sendMessage(binding.etMessage.text.toString())
                        addItemToRecyclerView(message)
                        binding.tvPlaceHolder.visibility = View.GONE
                        displayToast("This is Message")
                    }
                } else {
                    binding.tilMessage.setEndIconOnClickListener {
                        findNavController().navigate(R.id.action_personalChatRoomFragment_to_sendAudioBottomSheetFragment)
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addItemToRecyclerView(message: MessageObj) {
        chatList.add(message)
        chatRoomAdapter.notifyItemInserted(chatList.size)
        chatRoomAdapter.notifyDataSetChanged()
        binding.etMessage.setText("")
        binding.rvMessage.scrollToPosition(chatList.size - 1)
    }

    fun hideKeyBoard() {
        // Hide the keyboard
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
        Timber.tag("Hide Keyboard").d("Hide True")
    }

}