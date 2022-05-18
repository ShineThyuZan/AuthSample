package com.galaxytechno.chat.presentation.ui.other_lvl_dest.language_select

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galaxytechno.chat.domain.AppRepository
import com.galaxytechno.chat.domain.AuthRepository
import com.galaxytechno.chat.model.dto.LanguageVos
import com.galaxytechno.chat.model.dto.LanguagesResponse
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val authRepository: AuthRepository
) : ViewModel() {


    /** LOADING STATE for progress dialog */
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading
    fun setLoadingState(state: Boolean) {
        viewModelScope.launch {
            _isLoading.emit(state)
        }
    }


    private val _supportLanguageList = Channel<RemoteEvent<LanguagesResponse>>()
    val languagesList get() = _supportLanguageList.receiveAsFlow()
     fun getSupportLanguageList() {
        viewModelScope.launch {
            appRepository.getLanguages().collect {

                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        _supportLanguageList.send(RemoteEvent.ErrorEvent(it.message!!))
                    }
                    is RemoteEvent.LoadingEvent -> {
                        setLoadingState(true)
                    }
                    is RemoteEvent.SuccessEvent -> {
                        _supportLanguageList.send(RemoteEvent.SuccessEvent(it.data!!))
                    }
                }

            }
        }
    }

    private val _done = Channel<Boolean>()
    val done get() = _done.receiveAsFlow()
    fun setLanguageObj(languageObj: LanguageVos) {
        viewModelScope.launch {
            val isSavedState = async {
                saveInitLangeSelectState()
            }
            val isSavedStatus = async {
                saveLocaleStatus(languageObj.locale)
            }
            if (isSavedState.await() && isSavedStatus.await()) {
                setLoadingState(true)
                delay(1000)
                _done.send(true)
                setLoadingState(false)
            }
        }

    }

    private suspend fun saveInitLangeSelectState(): Boolean {
        viewModelScope.launch {
            appRepository.storeLanguageChosenState(true)
        }
        return true
    }

    private suspend fun saveLocaleStatus(status: Int): Boolean {
        viewModelScope.launch {
            appRepository.storeLocaleStatus(status)
            authRepository.storeLocaleStatus(status)
        }
        return true
    }
}