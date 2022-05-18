package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home.pagerFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galaxytechno.chat.domain.UserRepository
import com.galaxytechno.chat.model.dto.CountriesResponse
import com.galaxytechno.chat.model.dto.GetFriendListResponse
import com.galaxytechno.chat.model.dto.GetSearchFriListResponse
import com.galaxytechno.chat.model.entity.UserEntity
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFeatureViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    /** getting user data from Db */
    private val _userEntity: MutableLiveData<UserEntity> = MutableLiveData()
    val userEntity: LiveData<UserEntity> get() = _userEntity

    fun getUserFromDb() {
        viewModelScope.launch {
            userRepository.getUserFromDb().collect {
                _userEntity.value = it
            }
        }
    }

    /** just save mobile number to view model*/
    private val _searchText = MutableSharedFlow<String>()
    val searchText : SharedFlow<String> get() = _searchText
    fun setSearchText(searchText : String) {
        viewModelScope.launch {
            _searchText.emit(searchText)
        }
    }


    /** (post)requesting user profile info for info update and showing */
    private val _friList = MutableSharedFlow<RemoteEvent<GetSearchFriListResponse>>()
    val friListEvent get() = _friList.asSharedFlow()

    fun getFriendSearch(
        offset: Int,
        limit: Int,
        searchedUserName: String
    ) {
        viewModelScope.launch {
            userRepository.getFriSearch(
                offset = offset,
                limit = limit,
                searchedUserName = searchedUserName
            ).collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        _friList.emit(RemoteEvent.ErrorEvent(errorMessage = it.data!!.messageCode.toString()))
                    }
                    is RemoteEvent.LoadingEvent -> {
                    }
                    is RemoteEvent.SuccessEvent -> {

                        _friList.emit(RemoteEvent.SuccessEvent(it.data!!))
                    }
                }
            }
        }
    }




}