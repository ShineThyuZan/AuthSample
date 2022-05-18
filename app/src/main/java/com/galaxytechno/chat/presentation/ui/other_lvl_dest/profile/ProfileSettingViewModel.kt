package com.galaxytechno.chat.presentation.ui.other_lvl_dest.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galaxytechno.chat.domain.UserRepository
import com.galaxytechno.chat.model.entity.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileSettingViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _user = MutableLiveData<UserEntity>()
    val userEntity: LiveData<UserEntity> get() = _user
    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            userRepository.getUserFromDb().collect {
                _user.value = it
            }
        }
    }
}