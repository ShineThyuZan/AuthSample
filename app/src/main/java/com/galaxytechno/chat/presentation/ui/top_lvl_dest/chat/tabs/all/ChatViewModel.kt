package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.tabs.all

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.galaxytechno.chat.di.Qualifier
import com.galaxytechno.chat.domain.AppRepository
import com.galaxytechno.chat.domain.UserRepository
import com.galaxytechno.chat.model.dto.GetFriendListResponse
import com.galaxytechno.chat.movie.Movie
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val appRepository: AppRepository,
    @Qualifier.Io private val io: CoroutineDispatcher
) : ViewModel() {

    init {
        getPagerMovies()
    }

    /** Sample data view Model
     */
    private val _movies = MutableLiveData<PagingData<Movie>>()
    val movies: LiveData<PagingData<Movie>> get() = _movies
    private fun getPagerMovies() {
        viewModelScope.launch(io) {
            val data = userRepository.getPagingMovies().cachedIn(viewModelScope)
            data.collect {
                _movies.postValue(it)
            }
        }
    }


    /** country code api call sample recent msg list */
    private val _countryObj = MutableLiveData<RemoteEvent<GetFriendListResponse>>()
    val countryObj: LiveData<RemoteEvent<GetFriendListResponse>> get() = _countryObj
    fun getChatRecentMsg() {
        viewModelScope.launch {
            userRepository.getChatRecentMsg().collect {
                _countryObj.value = it
            }
        }

    }
}