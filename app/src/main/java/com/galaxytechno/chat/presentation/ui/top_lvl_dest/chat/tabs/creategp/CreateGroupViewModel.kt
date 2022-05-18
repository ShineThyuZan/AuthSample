package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.tabs.creategp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galaxytechno.chat.domain.UserRepository
import com.galaxytechno.chat.model.dto.CreateGroupResponse
import com.galaxytechno.chat.model.vos.FriListVos
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CreateGroupViewModel @Inject constructor(
    private val userRepository: UserRepository

) : ViewModel() {

    /** just sign up loading state */
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun setLoadingState(state: Boolean) {
        viewModelScope.launch {
            _isLoading.emit(state)
        }
    }

    private var offset: Int = 0
    fun addOffset() {
        viewModelScope.launch {
            offset++
        }
    }

    /**   image pick action state from bottom sheet ( pick form camera , gallery , delete state ) */
    private val _imgPickState = MutableSharedFlow<String>()
    val imgPickState: SharedFlow<String> get() = _imgPickState
    fun setImagePickState(state: String) {
        viewModelScope.launch {
            _imgPickState.emit(state)
        }
    }

    /** user edit profile requesting */
    private val _createGroup = MutableSharedFlow<RemoteEvent<CreateGroupResponse>>()
    val createGroup get() = _createGroup.asSharedFlow()
    fun createChatGroup(
        groupName: String,
        description: String,
        image: File?

    ) {
        viewModelScope.launch {
            userRepository.createChatGroup(
                groupName = groupName,
                description = description,
                image = image,
                memberIdLists = "1002,1005"
            ).collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {}
                    is RemoteEvent.LoadingEvent -> {}
                    is RemoteEvent.SuccessEvent -> {
                        _createGroup.emit(it)
                    }
                }
            }
        }
    }

    private val _selectedList = MutableStateFlow<List<FriListVos>>(listOf())
    val selectedList: StateFlow<List<FriListVos>> get() = _selectedList
    fun addSelectedItem(item: FriListVos) {
        val listToModify = _selectedList.value.toMutableList()
        listToModify.add(index = 0, element = item)
        _selectedList.value = listToModify.toList()
    }

    fun deleteSelectedItem(item: FriListVos) {
        val listToModify = _selectedList.value.toMutableList()
        listToModify.remove(item)
        _selectedList.value = listToModify.toList()
    }

    private val _memberList = MutableStateFlow<List<FriListVos>>(listOf())
    val memberList: StateFlow<List<FriListVos>> get() = _memberList
    fun getFriendList(
        offset: Int,
        limit: Int,
        searchedUserName: String
    ) {
        viewModelScope.launch {
            userRepository.getFriList(
                offset = offset,
                limit = limit,
                searchedUserName = searchedUserName
            ).collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        setLoadingState(false)
                    }
                    is RemoteEvent.LoadingEvent -> {
                        setLoadingState(true)
                    }
                    is RemoteEvent.SuccessEvent -> {
                        setLoadingState(true)
                        delay(500)
                        setLoadingState(false)
                        _memberList.value = it.data!!.data.friendList!!.toList()
                    }
                }
            }
        }
    }
}