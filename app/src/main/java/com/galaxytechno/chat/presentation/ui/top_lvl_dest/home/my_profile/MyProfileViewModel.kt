package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.my_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galaxytechno.chat.common.Constant
import com.galaxytechno.chat.domain.UserRepository
import com.galaxytechno.chat.model.dto.*
import com.galaxytechno.chat.model.entity.UserEntity
import com.galaxytechno.chat.model.response.VerifiedResponse
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val userRepository: UserRepository

) : ViewModel() {

    private val _profileFile = MutableStateFlow<File?>(null)
    private val profileFile: StateFlow<File?> get() = _profileFile

    fun setProfileFile(file: File) {
        viewModelScope.launch {
            _profileFile.value = file
        }
    }

    private val _coverFile = MutableStateFlow<File?>(null)
    private val coverFile: StateFlow<File?> get() = _coverFile
    fun setCoverFile(file: File) {
        viewModelScope.launch {
            _coverFile.value = file
        }
    }

    private val _emptyFile = MutableStateFlow<File?>(null)
    private val emptyFile: StateFlow<File?> get() = _emptyFile
    fun setEmptyFile(file: File) {
        viewModelScope.launch {
            _emptyFile.value = file
        }
    }


    /** get user data to set in user profile edit state */
    private val _userEntity: MutableLiveData<UserEntity> = MutableLiveData()
    val userEntity: LiveData<UserEntity> get() = _userEntity
    fun getUserFromDb() {
        viewModelScope.launch {
            userRepository.getUserFromDb().collect {
                _userEntity.postValue(it)
            }
        }
    }

    /** confirmation(delete and accept ) friend request in notification */
    private val _confirmFriendRequest = Channel<RemoteEvent<VerifiedResponse>>()
    val confirmFriRequest get() = _confirmFriendRequest.receiveAsFlow()
    fun confirmFriendRequest(
        friendId: Long,
        isAccept: Boolean
    ) {
        viewModelScope.launch {
            userRepository.confirmFriendRequest(
                friendId = friendId,
                isAccept = isAccept
            ).collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        setLoadingState(false)
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

    /** user edit profile requesting */
    private val _editResponse = MutableSharedFlow<RemoteEvent<ProfileInfoResponse>>()
    val editResponse get() = _editResponse.asSharedFlow()
    fun editProfile(
        name: String,
        bio: String,
        email: String,
        birthDate: String,
        gender: Int,
        isProfileImgRemove: Boolean,
        isCoverImageRemove: Boolean
    ) {
        viewModelScope.launch {
            setLoadingState(true)
            userRepository.profileInfoUpload(
                name = name,
                bio = bio,
                email = email,
                birthDate = birthDate,
                gender = gender,
                profileImg = profileFile.value ?: emptyFile.value,
                coverImg = coverFile.value ?: emptyFile.value,
                isProfileImgRemove = isProfileImgRemove,
                isCoverImgRemove = isCoverImageRemove,
            ).collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        setLoadingState(false)
                    }
                    is RemoteEvent.LoadingEvent -> {}
                    is RemoteEvent.SuccessEvent -> {
                        if (it.data?.status == Constant.STATUS_SUCCESS) {
                            saveUserEntity(
                                userEntity = it.data.data!!.toEntity()
                            )
                        }
                        delay(1000L)
                        _editResponse.emit(it)
                        setLoadingState(false)
                    }
                }
            }
        }
    }

    /** (post)requesting friend profile info for info update and showing */
    private val _friendProfileResponse =
        MutableStateFlow<RemoteEvent<FriendProfileInfoResponse>>(RemoteEvent.LoadingEvent())
    val friendProfileResponse get() = _friendProfileResponse.asStateFlow()
    private fun friendProfileDetail(friendId: Long) {
        viewModelScope.launch {
            setLoadingState(true)
            userRepository.friendProfileInfo(
//                userId = userEntity.value!!.userId,
                friendId = friendId

            ).collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        setLoadingState(false)
                        _friendProfileResponse.emit(
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
                        _friendProfileResponse.emit(RemoteEvent.SuccessEvent(it.data!!))
                    }
                }
            }
        }
    }

    /** (post)request friend add*/
    private val _addFriendResponse = Channel<RemoteEvent<FriendAddResponse>>()
    val addFriendResponse get() = _addFriendResponse.receiveAsFlow()

    fun requestAddFriend(userId: Long) {
        viewModelScope.launch {
            setLoadingState(true)
            userRepository.addFriend(
                friendId = userId
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
                        Timber.tag("Success").d("add success")
                        setLoadingState(false)
                        _addFriendResponse.send(RemoteEvent.SuccessEvent(it.data!!))
                    }
                }
            }
        }
    }


    private var offset: Int = 0
    fun addOffset() {
        viewModelScope.launch {
            offset++
        }
    }

    /** save user data to db when edit user profile info */
    private fun saveUserEntity(userEntity: UserEntity) {
        viewModelScope.launch {
            userRepository.saveUserToDb(userEntity)
        }
    }

    /** save friend data to db when edit user profile info */
    private val _contactFriObj = MutableLiveData<ContactFriObj>()
    val contactFriObj: LiveData<ContactFriObj> get() = _contactFriObj
    fun setContactFriData(contactFriData: ContactFriObj) {
        viewModelScope.launch {
            _contactFriObj.value = contactFriData
        }
    }

    private val _friendId = MutableLiveData<Long>()
    val friendId: LiveData<Long> get() = _friendId
    fun saveFriendId(friendId: Long) {
        viewModelScope.launch {
            _friendId.value = friendId
            friendProfileDetail(friendId)
        }

    }

    /**  profile image action state from bottom sheet ( pick form camera , gallery , delete state ) */
    private val _profileActionState = MutableSharedFlow<String>()
    val profileActionState: SharedFlow<String> get() = _profileActionState
    fun setProfileStateCameraOrGallery(state: String) {
        viewModelScope.launch {
            _profileActionState.emit(state)
        }
    }

    /**  cover image action state from bottom sheet ( pick form camera , gallery , delete state ) */
    private val _coverActionState = MutableSharedFlow<String>()
    val coverActionState: SharedFlow<String> get() = _coverActionState
    fun setCoverStateCameraOrGallery(state: String) {
        viewModelScope.launch {
            _coverActionState.emit(state)
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

    /** (post)requesting user profile info for info update and showing */
    private val _userProfileInfo = Channel<RemoteEvent<ProfileInfoResponse>>()
    val userProfileInfoEvent get() = _userProfileInfo.receiveAsFlow()

    fun requestUserProfileInfo() {
        viewModelScope.launch {
            userRepository.userProfileInfo(
            ).collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        setLoadingState(false)
                        _userProfileInfo.send(
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
                        _userProfileInfo.send(RemoteEvent.SuccessEvent(it.data!!))
                    }
                }
            }
        }
    }


    /** (post)requesting user profile info for info update and showing */
    private val _friList = MutableSharedFlow<RemoteEvent<GetFriendListResponse>>()
    val friListEvent get() = _friList.asSharedFlow()

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
                        _friList.emit(RemoteEvent.ErrorEvent(errorMessage = it.data!!.messageCode.toString()))
                    }
                    is RemoteEvent.LoadingEvent -> {
                        setLoadingState(true)
                    }
                    is RemoteEvent.SuccessEvent -> {
                        setLoadingState(true)
                        delay(500)
                        setLoadingState(false)
                        _friList.emit(RemoteEvent.SuccessEvent(it.data!!))
                    }
                }
            }
        }
    }

    /** (post)unfriend */
    private val _unfriendResponse = Channel<RemoteEvent<UnfriendResponse>>()
    val unfriendResponse get() = _unfriendResponse.receiveAsFlow()

    fun unFriendRequest() {
        viewModelScope.launch {
            userRepository.unFriend(
                friendId = friendId.value!!
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

    /** (post)block */
    private val _blockResponse = Channel<RemoteEvent<BlockResponse>>()
    val blockResponse get() = _blockResponse.receiveAsFlow()

    fun blockRequest() {
        viewModelScope.launch {
            userRepository.block(
                friendId = friendId.value!!,
                isBlock = true
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

