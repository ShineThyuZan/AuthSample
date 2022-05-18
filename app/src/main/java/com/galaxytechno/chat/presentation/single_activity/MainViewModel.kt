package com.galaxytechno.chat.presentation.single_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galaxytechno.chat.di.Qualifier
import com.galaxytechno.chat.domain.AppRepository
import com.galaxytechno.chat.model.dto.LanguageVos
import com.galaxytechno.chat.model.dto.LanguagesResponse
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {


    private val _authState = MutableLiveData<Boolean>()
    val authState: LiveData<Boolean> get() = _authState

    private val _languageChosenState = MutableLiveData<Boolean>()
    val languageChosenState: LiveData<Boolean> get() = _languageChosenState


    init {
        getAuthState()
        getLanguageChosenState()
    }

    private fun getAuthState() {
        viewModelScope.launch {
            appRepository.getAuthState().collect {
                _authState.postValue(it)
            }
        }
    }

    private fun getLanguageChosenState() {
        viewModelScope.launch {
            appRepository.getLanguageChosenState().collect {
                _languageChosenState.postValue(it)
            }
        }
    }

}
