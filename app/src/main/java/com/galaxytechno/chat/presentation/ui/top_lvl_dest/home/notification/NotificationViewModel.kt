package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galaxytechno.chat.domain.UserRepository
import com.galaxytechno.chat.model.dto.ContactProfileInfoResponse
import com.galaxytechno.chat.model.dto.FriRequestResponse
import com.galaxytechno.chat.model.dto.FriendAddResponse
import com.galaxytechno.chat.model.dto.UnfriendResponse
import com.galaxytechno.chat.model.entity.UserEntity
import com.galaxytechno.chat.model.response.VerifiedResponse
import com.galaxytechno.chat.model.vos.FriReqVos
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repo: UserRepository,
) : ViewModel() {
    /** just sign up loading state */
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading
    private fun setLoadingState(state: Boolean) {
        viewModelScope.launch {
            _isLoading.emit(state)
        }
    }

    /** getting user data from Db */
    private val _userEntity: MutableLiveData<UserEntity> = MutableLiveData(UserEntity())
    val userEntity: LiveData<UserEntity> get() = _userEntity

    private var offset: Int = 0
    fun addOffset() {
        viewModelScope.launch {
            offset++
        }
    }

    /** showing fri request list in notification */
    private val _requestedFriends = MutableSharedFlow<RemoteEvent<FriRequestResponse>>()
    val requestFriListChannel get() = _requestedFriends.asSharedFlow()
    fun getNotiFriRequestList(
        offset: Int,
        limit: Int = 30
    ) {
        viewModelScope.launch {
            repo.getNotiFriRequestList(
                offset = offset,
                limit = limit,
            ).collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        Timber.tag("error").d("Error Event")
                    }
                    is RemoteEvent.LoadingEvent -> {
                        Timber.tag("error").d("Loading Event")
                    }
                    is RemoteEvent.SuccessEvent -> {
                        _requestedFriends.emit(RemoteEvent.SuccessEvent(it.data!!))
                    }
                }
            }
        }
    }

    /** (post)unfriend */
    private val _unfriendResponse = Channel<RemoteEvent<UnfriendResponse>>()
    val unfriendResponse get() = _unfriendResponse.receiveAsFlow()

    fun unFriendRequest( friendId: Long) {
        viewModelScope.launch {
            repo.unFriend(
                friendId = friendId
            ).collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        setLoadingState(false)
                        _unfriendResponse.send(
                            RemoteEvent.ErrorEvent(
                                errorMessage = it.data!!.error ?: "Error"
                            )
                        )
                    }
                    is RemoteEvent.LoadingEvent -> {
                        setLoadingState(true)
                    }
                    is RemoteEvent.SuccessEvent -> {


                        setLoadingState(false)
                        _unfriendResponse.send(RemoteEvent.SuccessEvent(it.data!!))
                    }
                }
            }
        }
    }

    /** confirmation(delete and accept ) friend request in notification */
    private val _confirmFriendRequest = Channel<RemoteEvent<VerifiedResponse>>()
    val confirmFriendRequest get() = _confirmFriendRequest.receiveAsFlow()
    fun confirmFriendRequest(
        friendId: Long,
        isAccept: Boolean
    ) {
        viewModelScope.launch {
            repo.confirmFriendRequest(
                friendId = friendId,
                isAccept = isAccept
            ).collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        Timber.tag("error").d("Error Event")
                    }
                    is RemoteEvent.LoadingEvent -> {
                        setLoadingState(true)
                    }
                    is RemoteEvent.SuccessEvent -> {
                        _confirmFriendRequest.send(RemoteEvent.SuccessEvent(it.data!!))
                        setLoadingState(false)
                    }
                }
            }
        }
    }

    /** saving contact fri user data to viewModel  */
    private val _friReqVo = MutableLiveData<FriReqVos>()
    val friReqVo: LiveData<FriReqVos> get() = _friReqVo
    fun setFriRequestedVos(friReqVos: FriReqVos) {
        viewModelScope.launch {
            _friReqVo.value = friReqVos
        }
        // requestedFriProfileData()
    }


    /** (post)requesting user profile info for info update and showing */
    private val _contactProfileResponse = Channel<RemoteEvent<ContactProfileInfoResponse>>()
    val contactProfileResponse get() = _contactProfileResponse.receiveAsFlow()

    fun requestedFriProfileData(requesterId: Long) {
        viewModelScope.launch {
            setLoadingState(true)
            repo.contactProfileInfo(
                friendId = requesterId
            ).collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        setLoadingState(false)
                        _contactProfileResponse.send(
                            RemoteEvent.ErrorEvent(
                                errorMessage = it.data?.error ?: "Error"
                            )
                        )
                    }
                    is RemoteEvent.LoadingEvent -> {
                        setLoadingState(true)
                    }
                    is RemoteEvent.SuccessEvent -> {
                        delay(500L)
                        _contactProfileResponse.send(RemoteEvent.SuccessEvent(it.data!!))
                        setLoadingState(false)
                    }
                }
            }
        }
    }


    /** (post)request friend add*/
    private val _addFriendResponse = Channel<RemoteEvent<FriendAddResponse>>()
    val addFriendResponse get() = _addFriendResponse.receiveAsFlow()

    fun requestAddFriend(requesterId: Long) {
        viewModelScope.launch {
            repo.addFriend(
                friendId = requesterId
            ).collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        setLoadingState(false)
                        _addFriendResponse.send(
                            RemoteEvent.ErrorEvent(
                                errorMessage = it.data?.error ?: "Error"
                            )
                        )
                    }
                    is RemoteEvent.LoadingEvent -> {
                    }
                    is RemoteEvent.SuccessEvent -> {
                        setLoadingState(false)
                        _addFriendResponse.send(RemoteEvent.SuccessEvent(it.data!!))
                    }
                }
            }
        }
    }

    /** (put)cancel friend request*/
    private val _cancelFriRequest = Channel<RemoteEvent<VerifiedResponse>>()
    val cancelFriRequest get() = _cancelFriRequest.receiveAsFlow()

    fun cancelFriendRequest(friendId: Long) {
        viewModelScope.launch {
            repo.cancelFriendRequest(
                friendId = friendId
            ).collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        setLoadingState(false)
                        _cancelFriRequest.send(
                            RemoteEvent.ErrorEvent(
                                errorMessage = it.data?.error ?: "Error"
                            )
                        )
                    }
                    is RemoteEvent.LoadingEvent -> {
                        setLoadingState(true)
                    }
                    is RemoteEvent.SuccessEvent -> {
                        setLoadingState(false)
                        _cancelFriRequest.send(RemoteEvent.SuccessEvent(it.data!!))
                    }
                }
            }
        }
    }


}
