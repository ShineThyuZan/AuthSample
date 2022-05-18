package com.galaxytechno.chat.presentation.ui.top_lvl_dest.feeds

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galaxytechno.chat.di.Qualifier
import com.galaxytechno.chat.domain.AppRepository
import com.galaxytechno.chat.domain.UserRepository
import com.galaxytechno.chat.model.dto.BankListResponse
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val appRepository: AppRepository,
    @Qualifier.Io private val io: CoroutineDispatcher
) : ViewModel() {
    init {
        getBankDataList()
    }

    /** sample bank api call  */
    private val _bankObj = MutableLiveData<RemoteEvent<BankListResponse>>()
    val bankObj: LiveData<RemoteEvent<BankListResponse>> get() = _bankObj
    private fun getBankDataList() {
        viewModelScope.launch {
            userRepository.getBankList().collect {
                _bankObj.value = it
            }
        }
    }

}

