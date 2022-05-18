package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme

import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map
import com.galaxytechno.chat.domain.AppRepository
import com.galaxytechno.chat.domain.UserRepository
import com.galaxytechno.chat.model.entity.UserEntity
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.initmember.components.shimmer.FriendsUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllFriendsForCreateRoomUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val appRepository: AppRepository,
) {

    suspend operator fun invoke(): Flow<PagingData<FriendsUiModel>> {
        val locale = getLocale()
        val user = getUser()
        return userRepository.getAlreadyFriendsWithHeader(
            userId = user.userId,
            locale = locale,
            query = ""
        ).map { pagingData ->
            pagingData.map { friendVo ->
                FriendsUiModel.Item(friendVo)
            }.insertSeparators { before, after ->
                val nameOfAfterItem = after?.item?.friendName?.first().toString()
                if (after?.item?.friendName?.first() != before?.item?.friendName?.first()) {
                    return@insertSeparators FriendsUiModel.Header(nameOfAfterItem)
                } else null
            }
        }

    }

    private suspend fun getLocale(): Int {
        return appRepository.getLocaleStatus().first()
    }

    private suspend fun getUser(): UserEntity {
        return userRepository.getUserFromDb().first()
    }
}
