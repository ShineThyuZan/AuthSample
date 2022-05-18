package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galaxytechno.chat.domain.UserRepository
import com.galaxytechno.chat.model.dto.ContactProfileInfoResponse
import com.galaxytechno.chat.model.dto.FriendAddResponse
import com.galaxytechno.chat.model.response.VerifiedResponse
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileInfoViewmodel @Inject constructor(
    private val repo: UserRepository
)  : ViewModel(){

    /** just sign up loading state */
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading
    private fun setLoadingState(state: Boolean) {
        viewModelScope.launch {
            _isLoading.emit(state)
        }
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
                        setLoadingState(false)
                        _contactProfileResponse.send(RemoteEvent.SuccessEvent(it.data!!))
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
        isAccept : Boolean
    ) {
        viewModelScope.launch {
            repo.confirmFriendRequest(
            friendId = friendId,
                isAccept =  isAccept
            ).collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        Timber.tag("error").d("Error Event")
                    }
                    is RemoteEvent.LoadingEvent -> {
                        setLoadingState(true)
                    }
                    is RemoteEvent.SuccessEvent -> {
                        setLoadingState(true)
                        _confirmFriendRequest.send(RemoteEvent.SuccessEvent(it.data!!))
                        delay(1000)
                        setLoadingState(false)
                    }
                }
            }
        }
    }
    /** (post)request friend add*/
    private val _addFriendResponse = Channel<RemoteEvent<FriendAddResponse>>()
    val addFriendResponse get() = _addFriendResponse.receiveAsFlow()

    fun requestAddFriend(requesterId : Long) {
        viewModelScope.launch {
            setLoadingState(true)
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
                        setLoadingState(true)
                    }
                    is RemoteEvent.SuccessEvent -> {
                        Timber.tag("Succcc").d("add success")
                        setLoadingState(false)
                        _addFriendResponse.send(RemoteEvent.SuccessEvent(it.data!!))
                    }
                }
            }
        }
    }

}