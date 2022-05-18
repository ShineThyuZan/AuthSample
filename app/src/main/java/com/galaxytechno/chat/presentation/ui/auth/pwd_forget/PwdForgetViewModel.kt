package com.galaxytechno.chat.presentation.ui.auth.pwd_forget

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galaxytechno.chat.di.Qualifier
import com.galaxytechno.chat.domain.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PwdForgetViewModel @Inject constructor(
    private val appRepository: AppRepository,
    @Qualifier.Io private val io: CoroutineDispatcher
) : ViewModel() {
    private val _countryId = MutableLiveData<Int>()
    val countryId: LiveData<Int> get() = _countryId
    fun setCountryId(country: Int) {
        viewModelScope.launch {
            _countryId.postValue(country)
            Timber.tag("country").d("country$country")
        }
    }

}