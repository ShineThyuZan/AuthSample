package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.contact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galaxytechno.chat.domain.ContactsRepository
import com.galaxytechno.chat.domain.UserRepository
import com.galaxytechno.chat.model.dto.*
import com.galaxytechno.chat.model.entity.UserEntity
import com.galaxytechno.chat.model.response.VerifiedResponse
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.util.DataResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FriContactViewModel @Inject constructor(
    private val contactsRepo: ContactsRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    /** just sign up loading state */
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun setLoadingState(state: Boolean) {
        viewModelScope.launch {
            _isLoading.emit(state)
        }
    }

    private val _pickContactsStatus: MutableStateFlow<RemoteEvent<List<Contacts>>> =
        MutableStateFlow(
            RemoteEvent.LoadingEvent()
        )
    val pickContactsStatus: StateFlow<RemoteEvent<List<Contacts>>> get() = _pickContactsStatus.asStateFlow()

    private val _listToServer: MutableList<String> = mutableListOf()

    fun getContactsFromDevice() {
        viewModelScope.launch {
            contactsRepo.getContacts().collect {
                when (it) {
                    is DataResource.ErrorEvent -> {
                        _pickContactsStatus.value = RemoteEvent.ErrorEvent(
                            errorMessage = it.message ?: "Error"
                        )
                    }
                    is DataResource.LoadingEvent -> {
                        _pickContactsStatus.value = RemoteEvent.LoadingEvent()
                    }
                    is DataResource.SuccessEvent -> {

                        val listFromDevice = it.data!!
                        val listToServer: MutableList<String> = mutableListOf()
                        Timber.tag("klt.contact").d(listToServer.toString())
                        listToServer.clear()
                        listFromDevice.forEach { contact ->
                            if (contact.numbers.size > 1) {

                                contact.numbers.forEach { phone ->
                                    val nonSpace = phone.replace("\\s".toRegex(), "")
                                    val onlyNumber = nonSpace.replace("[^0-9]".toRegex(), "")
                                    val ph = onlyNumber.replace("[()-]".toRegex(), "")
                                    listToServer.add(ph)
                                }
                            } else {
                                if (contact.numbers.isNotEmpty()) {
                                    val numberOnlyPhone = contact.numbers.first()
                                    val nonSpace = numberOnlyPhone.replace("\\s".toRegex(), "")
                                    val onlyNumber = nonSpace.replace("[^0-9]".toRegex(), "")
                                    val ph = onlyNumber.replace("[()-]".toRegex(), "")
                                    listToServer.add(ph)
                                }
                            }
                        }
                        friContactCheck(listToServer)

//                        Timber.tag("klt.contact.list").d(listToServer.toString())
//                        if (listToServer.isEmpty()) {
//                            Timber.tag("klt.contact.list.empty").d(listToServer.toString())
//                            _contactsState.value = contactsState.value.copy(
//                                stateList = contactsState.value.stateList.copy(
//                                    uiState = ContactsListUi.EmptyContact
//                                )
//                            )
//                        } else {
//                            Timber.tag("klt.contact.list.server").d(listToServer.toString())
//                            getContactsFromServer(listToServer)
//                        }


//                        _pickContactsStatus.value = RemoteEvent.SuccessEvent(
//                            it.data!!
//                        )
//                        _listToServer.clear()
//                        if (it.data.isNotEmpty()) {
//                            it.data.forEach { contacts ->
//                                if (contacts.numbers.size > 1) {
//                                    contacts.numbers.forEach { phone ->
//                                        _listToServer.add(phone)
//                                    }
//                                } else {
//                                    if (contacts.numbers.isNotEmpty())
//                                        _listToServer.add(contacts.numbers.first())
//                                }
//                            }
//                            friContactCheck(_listToServer)
//                        }
                    }
                }
            }
        }
    }

    /** confirmation(delete and accept ) friend request in notification */
    private val _acceptFriendRequest = Channel<RemoteEvent<VerifiedResponse>>()
    val acceptFriRequest get() = _acceptFriendRequest.receiveAsFlow()
    fun acceptFriendRequest() {
        viewModelScope.launch {
            userRepository.confirmFriendRequest(
                friendId = contactFriObj.value!!.userId,
                isAccept = true
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
                        _acceptFriendRequest.send(RemoteEvent.SuccessEvent(it.data!!))
                        setLoadingState(false)
                    }
                }
            }
        }
    }

    /** confirmation(delete and accept ) friend request in notification */
    private val _cancelFriendRequest = Channel<RemoteEvent<VerifiedResponse>>()
    val cancelFriRequest get() = _cancelFriendRequest.receiveAsFlow()
    fun cancelFriendRequest() {
        viewModelScope.launch {
            userRepository.confirmFriendRequest(
                friendId = contactFriObj.value!!.userId,
                isAccept = false
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
                        _cancelFriendRequest.send(RemoteEvent.SuccessEvent(it.data!!))
                        setLoadingState(false)
                    }
                }
            }
        }
    }


    /** (post)requesting user profile info for info update and showing */
    private val _friContactList = Channel<RemoteEvent<FriContactCheckResponse>>()
    val friContactEvent get() = _friContactList.receiveAsFlow()

    private fun friContactCheck(mobiles: List<String>) {
        viewModelScope.launch {
            userRepository.friContactCheck(
                mobiles = mobiles
            ).collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        setLoadingState(false)
                        _friContactList.send(RemoteEvent.ErrorEvent(errorMessage = it.data!!.error))
                    }
                    is RemoteEvent.LoadingEvent -> {
                        setLoadingState(true)

                    }
                    is RemoteEvent.SuccessEvent -> {
                        setLoadingState(false)
                        _friContactList.send(RemoteEvent.SuccessEvent(it.data!!))
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
                friendId = contactFriObj.value!!.userId
            ).collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        setLoadingState(false)
                        _unfriendResponse.send(RemoteEvent.ErrorEvent(errorMessage = it.data!!.error ?: "Error"))
                    }
                    is RemoteEvent.LoadingEvent -> {
                        setLoadingState(true)
                    }
                    is RemoteEvent.SuccessEvent -> {
                        setLoadingState(true)
                        delay(500)
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

    fun blockRequest( isBlock : Boolean) {
        viewModelScope.launch {
            userRepository.block(
                friendId = contactFriObj.value!!.userId,
                isBlock = isBlock
            ).collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        setLoadingState(false)
                        _blockResponse.send(RemoteEvent.ErrorEvent(errorMessage = it.data!!.error ?: "Error"))
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

    /** saving contact fri user data to viewModel  */
    private val _contactFriObj = MutableLiveData<ContactFriObj>()
    val contactFriObj: LiveData<ContactFriObj> get() = _contactFriObj
    fun setContactFriData(contactFriData: ContactFriObj) {
        viewModelScope.launch {
            _contactFriObj.value = contactFriData
        }
    }

    /** saving contact fri user data to viewModel  */
    private val _contactProfileObj = MutableLiveData<ContactProfileInfoResponse>()
    val contactProfileObj: LiveData<ContactProfileInfoResponse> get() = _contactProfileObj
    fun setContactProfileData(contactProfileData: ContactProfileInfoResponse) {
        viewModelScope.launch {
            _contactProfileObj.value = contactProfileData
        }
    }

    /** (post)requesting user profile info for info update and showing */
    private val _contactProfileResponse = Channel<RemoteEvent<ContactProfileInfoResponse>>()
    val contactProfileResponse get() = _contactProfileResponse.receiveAsFlow()

    fun contactProfileDetail() {
        viewModelScope.launch {
            setLoadingState(true)
            userRepository.contactProfileInfo(
                friendId = contactFriObj.value!!.userId
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
                        setContactProfileData(it.data!!)
                        _contactProfileResponse.send(RemoteEvent.SuccessEvent(it.data))
                    }
                }
            }
        }
    }


    /** (post)request friend add*/
    private val _addFriendResponse = Channel<RemoteEvent<FriendAddResponse>>()
    val addFriendResponse get() = _addFriendResponse.receiveAsFlow()

    fun requestAddFriend() {
        viewModelScope.launch {
            setLoadingState(true)
            userRepository.addFriend(
                friendId = contactFriObj.value!!.userId
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

    /** (put)cancel friend request*/
    private val _friRequestedCancel = Channel<RemoteEvent<VerifiedResponse>>()
    val friRequestedCancel get() = _friRequestedCancel.receiveAsFlow()

    fun friRequestedCancel() {
        viewModelScope.launch {
            userRepository.cancelFriendRequest(
                friendId = contactFriObj.value!!.userId
            ).collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        setLoadingState(false)
                    }
                    is RemoteEvent.LoadingEvent -> {
                        setLoadingState(true)
                    }
                    is RemoteEvent.SuccessEvent -> {
                        setLoadingState(false)
                        _friRequestedCancel.send(RemoteEvent.SuccessEvent(it.data!!))
                    }
                }
            }
        }
    }
}