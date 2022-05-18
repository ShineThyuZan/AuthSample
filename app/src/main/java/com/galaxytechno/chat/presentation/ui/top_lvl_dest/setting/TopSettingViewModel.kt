package com.galaxytechno.chat.presentation.ui.top_lvl_dest.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galaxytechno.chat.common.Constant
import com.galaxytechno.chat.domain.AppRepository
import com.galaxytechno.chat.domain.UserRepository
import com.galaxytechno.chat.model.dto.BlockListResponse
import com.galaxytechno.chat.model.dto.BlockResponse
import com.galaxytechno.chat.model.entity.UserEntity
import com.galaxytechno.chat.model.response.VerifiedResponse
import com.galaxytechno.chat.model.vos.FriListVos
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TopSettingViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val appRepository: AppRepository,
) : ViewModel() {
    private var offset: Int = 0
    fun addOffset() {
        viewModelScope.launch {
            offset++
        }
    }

    init {
        viewModelScope.launch {
            requestingUserProfileInfo()
        }
    }

    /** just sign up loading state */
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun setLoadingState(state: Boolean) {
        viewModelScope.launch {
            _isLoading.emit(state)
        }
    }

    /** showing fri request list in notification */
    private val _requestBlockList = Channel<RemoteEvent<BlockListResponse>>()
    val requestBlockList get() = _requestBlockList.receiveAsFlow()
    fun getBlockListRequest(
        offset: Int,
        limit: Int = 30
    ) {
        viewModelScope.launch {
            userRepository.getBlockList(
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
                        _requestBlockList.send(RemoteEvent.SuccessEvent(it.data!!))
                    }
                }
            }
        }
    }

    // db + api ( updated user )
    private val _user: MutableStateFlow<UserEntity> = MutableStateFlow(UserEntity())
    val user: StateFlow<UserEntity> get() = _user.asStateFlow()

    /** show loading state in logout */
    private val _isLogoutLoading = MutableStateFlow(false)
    val isLogoutLoading: StateFlow<Boolean> get() = _isLogoutLoading
    private fun setLogoutLoadingState(state: Boolean) {
        viewModelScope.launch {
            _isLogoutLoading.emit(state)
        }
    }

    /** Requesting logout from server */
    private val _logoutChannel = MutableSharedFlow<RemoteEvent<VerifiedResponse>>()
    val logoutEvent get() = _logoutChannel.asSharedFlow()
    fun requestLogout() {
        viewModelScope.launch {
            userRepository.logout(
            ).collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        setLoadingState(false)
                        _logoutChannel.emit(
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
                        logout(it.data!!)
                    }
                }
            }
        }
    }

    /** show loading state in logout */
    private val _isTwoFactorLoading = MutableStateFlow(false)
    val isTwoFactorLoading: StateFlow<Boolean> get() = _isTwoFactorLoading
    private fun setTwoFactorLoading(state: Boolean) {
        viewModelScope.launch {
            _isTwoFactorLoading.emit(state)
        }
    }

    private val _twoFactorText = MutableStateFlow("")
    val twoFactorText: StateFlow<String> get() = _twoFactorText.asStateFlow()

    private val _twoFactorStatus = MutableStateFlow(TwoFactorStatus.Loading)
    val twoFactorStatus: StateFlow<TwoFactorStatus> get() = _twoFactorStatus.asStateFlow()

    /** Requesting logout from server */
    private val _doTwoFactorChannel = Channel<RemoteEvent<VerifiedResponse>>()
    val twoFactorEvent get() = _doTwoFactorChannel.receiveAsFlow()

    var job: Job? = null
    fun requestDoTwoFactor(isTwoFactor: Boolean) {
        job?.cancel()

        job = viewModelScope.launch {
            delay(1000)
            setTwoFactorLoading(true)
            _twoFactorStatus.value = TwoFactorStatus.Loading
            _twoFactorText.value =
                if (isTwoFactor) "Enable Two Factor Authentication" else "Disable Two Factor Authentication"

            userRepository.doTwoFactor(

                isTwoFactor = isTwoFactor
            ).collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        delay(2000L)
                        _twoFactorStatus.value = TwoFactorStatus.Fail
                        _twoFactorText.value = it.data?.error ?: "Error"
                        delay(1000L)
                        setTwoFactorLoading(false)
                        _doTwoFactorChannel.send(
                            RemoteEvent.ErrorEvent(
                                errorMessage = it.data!!.error ?: "Error"
                            )
                        )
                    }
                    is RemoteEvent.LoadingEvent -> {
                        _isLogoutLoading.value = true
                    }
                    is RemoteEvent.SuccessEvent -> {
                        if (it.data!!.status == Constant.STATUS_SUCCESS) {

                            val updatedEntity = user.value.copy(
                                isTwoFactor = isTwoFactor
                            )
                            _user.value = updatedEntity
                            val isSavedTwoFactor = async {
                                setTwoFactorStateToDb(
                                    user = updatedEntity
                                )
                            }
                            if (isSavedTwoFactor.await()) {
                                delay(2000L)
                                _twoFactorStatus.value = TwoFactorStatus.Success
                                _twoFactorText.value = it.data.message ?: "Success"
                                delay(1000L)
                                setTwoFactorLoading(false)
                                _doTwoFactorChannel.send(RemoteEvent.SuccessEvent(it.data))
                            }

                        }
                    }
                }
            }
        }
    }


  private  fun requestingUserProfileInfo() {
        viewModelScope.launch {
            userRepository.userProfileInfo(
            ).collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        setLogoutLoadingState(true)

                    }
                    is RemoteEvent.LoadingEvent -> {
                        _isLogoutLoading.value = (true)
                    }
                    is RemoteEvent.SuccessEvent -> {
                        _user.value = it.data?.data!!.toEntity()
                    }
                }
            }
        }
    }

    private suspend fun setTwoFactorStateToDb(user: UserEntity): Boolean {
        viewModelScope.launch {
            userRepository.saveUserToDb(user)
        }
        return true
    }

    /** clear db and DS from android section  */
    private fun logout(response: VerifiedResponse) {
        viewModelScope.launch {
            val isClearedDb = async {
                clearDb()
            }
            val isClearedDs = async {
                clearDs()
            }
            if (
                isClearedDb.await() && isClearedDs.await()
            ) {
                delay(1500)
                setLogoutLoadingState(false)
                _logoutChannel.emit(RemoteEvent.SuccessEvent(response))
            }
        }
    }

    /** saving user question obj */
    private val _friendListVos = MutableLiveData<FriListVos>()
    val friendListVos: LiveData<FriListVos> get() = _friendListVos
    fun setFriendData(friendVos: FriListVos) {
        viewModelScope.launch {
            _friendListVos.postValue(friendVos)
        }
    }

    /** clearDb form android section */
    private suspend fun clearDb(): Boolean {
        viewModelScope.launch {
            userRepository.deleteUserFromDb()
        }
        return true
    }

    /** clearDStore form android section */
    private suspend fun clearDs(): Boolean {
        appRepository.removeAuthState()
        userRepository.removeAccessToken()
        userRepository.removeRefreshToken()
        return true
    }

    /** (post)block */
    private val _blockResponse = Channel<RemoteEvent<BlockResponse>>()
    val blockResponse get() = _blockResponse.receiveAsFlow()

    fun blockRequest(friendId: Long) {
        viewModelScope.launch {
            userRepository.block(
                friendId = friendId,
                isBlock = false
            ).collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        setLoadingState(false)
                        _blockResponse.send(
                            RemoteEvent.ErrorEvent(
                                errorMessage = it.data!!.error ?: "Error"
                            )
                        )
                    }
                    is RemoteEvent.LoadingEvent -> {
                        setLoadingState(true)
                    }
                    is RemoteEvent.SuccessEvent -> {
                        setLoadingState(true)
                        delay(500)
                        setLoadingState(false)
                        _blockResponse.send(RemoteEvent.SuccessEvent(it.data!!))
                    }
                }
            }
        }
    }
}