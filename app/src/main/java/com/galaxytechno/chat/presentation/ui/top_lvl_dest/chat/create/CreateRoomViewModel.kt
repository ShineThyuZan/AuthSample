package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.galaxytechno.chat.main.presentation.screens.chat.presentation.room.create.CreateRoomState
import com.galaxytechno.chat.model.vos.FriListVos
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.creategroup.udf.CreateGroupAction
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.creategroup.udf.CreateGroupEvent
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.initmember.udf.CreateRoomUseCase
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.initmember.udf.InitMemberAction
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.initmember.udf.InitMemberEvent
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.newmessage.udf.NewMessageAction
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.newmessage.udf.NewMessageEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateRoomViewModel @Inject constructor(
    private val useCase: CreateRoomUseCase
) : ViewModel() {

    private val _state: MutableState<CreateRoomState> = mutableStateOf(CreateRoomState())
    val state: State<CreateRoomState> get() = _state

    private val _newMessageEvent = MutableSharedFlow<NewMessageEvent>()
    val newMessageEvent: SharedFlow<NewMessageEvent> get() = _newMessageEvent

    private val _initMemberEvent = MutableSharedFlow<InitMemberEvent>()
    val initMemberEvent: SharedFlow<InitMemberEvent> get() = _initMemberEvent

    private val _createGroupEvent = MutableSharedFlow<CreateGroupEvent>()
    val createGroupEvent: SharedFlow<CreateGroupEvent> get() = _createGroupEvent

    init {
        getAllFriends()
    }

    private fun getAllFriends() {
        viewModelScope.launch {
            useCase.getAllFriendsForCreateRoom().collect {
                _state.value = state.value.copy(
                    friends = flow {
                        emit(it)
                    }.cachedIn(viewModelScope)
                )
            }
        }
    }

    fun saveSelectedItem(item: FriListVos) {
        viewModelScope.launch {
            val list = state.value.selectedFriends.value
            val mutableList = list.toMutableList()
            mutableList.add(index = 0, element = item)
//            mutableList.add(item)
            state.value.selectedFriends.value = mutableList
        }
    }

    fun deleteSelectedItem(item: FriListVos) {
        viewModelScope.launch {
            val list = state.value.selectedFriends.value
            val mutableList = list.toMutableList()
            mutableList.remove(item)
            state.value.selectedFriends.value = mutableList
        }
    }

    private var searchNewMessageJob: Job? = null
    private fun searchNewMessage(query: String) {
        searchNewMessageJob?.cancel()

        _state.value = state.value.copy(
            newMessageState = state.value.newMessageState.copy(
                searchFriends = emptyFlow()
            )
        )
        searchNewMessageJob = viewModelScope.launch {
            delay(1000L)
            useCase.searchAppUsersForCreateRoom(
                query = query
            ).collect {

                _state.value = state.value.copy(
                    newMessageState = state.value.newMessageState.copy(
                        searchFriends = flow {
                            emit(it)
                        }.cachedIn(viewModelScope)
                    )
                )
            }
        }
    }

    private var searchInitMemberJob: Job? = null
    private fun searchInitMember(query: String) {
        searchInitMemberJob?.cancel()

        _state.value = state.value.copy(
            initMemberState = state.value.initMemberState.copy(
                searchFriends = emptyFlow()
            )
        )
        searchInitMemberJob = viewModelScope.launch {
            delay(1000L)
            useCase.searchAppUsersForCreateRoom(
                query = query
            ).collect {
                _state.value = state.value.copy(
                    initMemberState = state.value.initMemberState.copy(
                        searchFriends = flow {
                            emit(it)
                        }.cachedIn(viewModelScope)
                    )
                )
            }
        }
    }

    fun onActionNewMessage(action: NewMessageAction) {
        when (action) {
            NewMessageAction.ClickBack -> {
                viewModelScope.launch {
                    _newMessageEvent.emit(
                        NewMessageEvent.Popup
                    )
                }
            }
            NewMessageAction.ClickCreateButton -> {
                viewModelScope.launch {
                    _newMessageEvent.emit(
                        NewMessageEvent.NavigateToAddMember
                    )
                }

            }
            is NewMessageAction.ClickItem -> {
                viewModelScope.launch {
                    _newMessageEvent.emit(
                        NewMessageEvent.NavigateToConversation(
                            friendVo = action.friend
                        )
                    )
                }
            }
            NewMessageAction.ClickSearchTextCancel -> {

                _state.value = state.value.copy(
                    newMessageState = state.value.newMessageState.copy(
                        searchQuery = ""
                    )
                )
            }
            is NewMessageAction.ClickSummit -> {
                viewModelScope.launch {
                    _newMessageEvent.emit(
                        NewMessageEvent.SummitSearch(
                            query = action.summitText
                        )
                    )
                }
            }
            is NewMessageAction.SearchTextChanged -> {
                _state.value = state.value.copy(
                    newMessageState = state.value.newMessageState.copy(
                        searchQuery = action.searchQuery
                    )
                )

                if (action.searchQuery.isNotEmpty()) {
                    searchNewMessage(
                        query = action.searchQuery
                    )
                }
            }
        }
    }

    fun onActionInitMember(action: InitMemberAction) {
        when (action) {
            is InitMemberAction.CheckItem -> {
                InitMemberEvent.ShowSnackBar(message = "clicked item...")

            }
            InitMemberAction.ClickBack -> {
                viewModelScope.launch {
                    _initMemberEvent.emit(
                        InitMemberEvent.Popup
                    )
                }
            }
            InitMemberAction.ClickNext -> {
                viewModelScope.launch {
                    _initMemberEvent.emit(
                        InitMemberEvent.NavigateToCreateGroup
                    )
                }
            }
            InitMemberAction.ClickSearchTextCancel -> {
                _state.value = state.value.copy(
                    initMemberState = state.value.initMemberState.copy(
                        searchQuery = ""
                    )
                )
            }
            is InitMemberAction.ClickSummit -> {
                viewModelScope.launch {
                    _initMemberEvent.emit(
                        InitMemberEvent.SummitSearch(
                            query = action.summitText
                        )
                    )
                }
            }
            is InitMemberAction.SearchTextChanged -> {
                _state.value = state.value.copy(
                    initMemberState = state.value.initMemberState.copy(
                        searchQuery = action.searchQuery
                    )
                )

                if (action.searchQuery.isNotEmpty()) {
                    searchInitMember(
                        query = action.searchQuery
                    )
                }
            }
            InitMemberAction.ShowSnack -> {
                viewModelScope.launch {
                    _initMemberEvent.emit(
                        InitMemberEvent.ShowSnack
                    )
                }

            }
        }
    }

    fun onActionCreateGroup(action: CreateGroupAction) {
        when (action) {
            is CreateGroupAction.ChangeName -> {
                _state.value = state.value.copy(
                    createGroupState = state.value.createGroupState.copy(
                        groupName = action.name
                    )
                )
            }
            is CreateGroupAction.ChangePhoto -> {
                _state.value = state.value.copy(
                    createGroupState = state.value.createGroupState.copy(
                        groupPhoto = action.image
                    )
                )
            }
            CreateGroupAction.ClickBack -> {
                viewModelScope.launch {
                    _createGroupEvent.emit(
                        CreateGroupEvent.Popup
                    )
                }
            }
            CreateGroupAction.ClickCreate -> {
                viewModelScope.launch {
                    _createGroupEvent.emit(
                        CreateGroupEvent.ShowSnack(message = "Coming soon...")
                    )
                }
            }
            CreateGroupAction.ClickUpload -> {
                viewModelScope.launch {
                    _createGroupEvent.emit(
                        CreateGroupEvent.ShowImagePickerSheet
                    )
                }
            }
            is CreateGroupAction.RemoveItem -> {

            }
            CreateGroupAction.UploadFromCamera -> {
                viewModelScope.launch {
                    _createGroupEvent.emit(
                        CreateGroupEvent.CameraPicker
                    )
                }
            }
            CreateGroupAction.UploadFromGallery -> {
                viewModelScope.launch {
                    _createGroupEvent.emit(
                        CreateGroupEvent.GalleryPicker
                    )
                }
            }
        }
    }

}