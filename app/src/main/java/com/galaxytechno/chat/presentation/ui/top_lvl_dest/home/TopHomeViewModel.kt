package com.galaxytechno.chat.presentation.ui.top_lvl_dest.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galaxytechno.chat.domain.UserRepository
import com.galaxytechno.chat.model.dto.FriContactCheckResponse
import com.galaxytechno.chat.model.entity.UserEntity
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TopHomeViewModel @Inject constructor(
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




}


