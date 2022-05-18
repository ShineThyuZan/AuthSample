package com.galaxytechno.chat.domain

import androidx.paging.PagingData
import com.galaxytechno.chat.model.dto.*
import com.galaxytechno.chat.model.entity.UserEntity
import com.galaxytechno.chat.model.response.VerifiedResponse
import com.galaxytechno.chat.model.vos.AppUserVo
import com.galaxytechno.chat.model.vos.FriListVos
import com.galaxytechno.chat.movie.Movie
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UserRepository {

    //DS
    suspend fun saveAccessToken(token: String)

    suspend fun getAccessToken(): Flow<String>

    suspend fun saveRefreshToken(token: String)

    suspend fun getRefreshToken(): Flow<String>

    suspend fun fetchRefreshToken(token: String): Flow<RemoteEvent<RefreshTokenDTO>>

    suspend fun removeAccessToken()

    suspend fun removeRefreshToken()

    //DB
    suspend fun saveUserToDb(userEntity: UserEntity)
    suspend fun getUserFromDb(): Flow<UserEntity>
    suspend fun deleteUserFromDb()

    //API
    suspend fun userProfileInfo(
    ): Flow<RemoteEvent<ProfileInfoResponse>>

    suspend fun contactProfileInfo(
        friendId: Long
    ): Flow<RemoteEvent<ContactProfileInfoResponse>>

    suspend fun friendProfileInfo(
        friendId: Long
    ): Flow<RemoteEvent<FriendProfileInfoResponse>>

    suspend fun profileInfoUpload(
        //todo except name , other can null
        name: String,
        bio: String? = null,
        email: String? = null,
        birthDate: String? = null,
        gender: Int? = null,
        profileImg: File? = null,
        coverImg: File? = null,
        isProfileImgRemove: Boolean,
        isCoverImgRemove: Boolean,
    ): Flow<RemoteEvent<ProfileInfoResponse>>

    suspend fun createChatGroup(
        groupName: String,
        description: String? = "",
        image: File? = null,
        memberIdLists: String
    ): Flow<RemoteEvent<CreateGroupResponse>>

    suspend fun logout(
    ): Flow<RemoteEvent<VerifiedResponse>>

    suspend fun doTwoFactor(
        isTwoFactor: Boolean,
    ): Flow<RemoteEvent<VerifiedResponse>>

    suspend fun friContactCheck(
        mobiles: List<String>
    ): Flow<RemoteEvent<FriContactCheckResponse>>

    suspend fun getFriList(
        offset: Int,
        limit: Int,
        searchedUserName: String,
    ): Flow<RemoteEvent<GetFriendListResponse>>

    suspend fun getFriSearch(
        offset: Int,
        limit: Int,
        searchedUserName: String,
    ): Flow<RemoteEvent<GetSearchFriListResponse>>

    suspend fun getNotiFriRequestList(
        offset: Int,
        limit: Int,
    ): Flow<RemoteEvent<FriRequestResponse>>

    suspend fun getBlockList(
        offset: Int,
        limit: Int
    ): Flow<RemoteEvent<BlockListResponse>>


    suspend fun getBankList(
    ): Flow<RemoteEvent<BankListResponse>>


    //todo : for testing
    suspend fun getChatRecentMsg(): Flow<RemoteEvent<GetFriendListResponse>>

    suspend fun getPagingMovies(): Flow<PagingData<Movie>>

    suspend fun addFriend(
        friendId: Long,
    ): Flow<RemoteEvent<FriendAddResponse>>

    suspend fun confirmFriendRequest(
        friendId: Long,
        isAccept: Boolean,
    ): Flow<RemoteEvent<VerifiedResponse>>

    suspend fun cancelFriendRequest(
       friendId: Long
    ): Flow<RemoteEvent<VerifiedResponse>>

    suspend fun unFriend(
        friendId: Long,
    ): Flow<RemoteEvent<UnfriendResponse>>

    suspend fun block(
        friendId: Long,
        isBlock: Boolean,
    ): Flow<RemoteEvent<BlockResponse>>

    //create room
    suspend fun getAlreadyFriendsWithHeader(
        userId: Long,
        locale: Int,
        query: String
    ): Flow<PagingData<FriListVos>>

    suspend fun searchUsers(
        userId: Long,
        locale: Int,
        query: String,
    ): Flow<PagingData<AppUserVo>>

    suspend fun searchUsersForCreateGroup(
        userId: Long,
        locale: Int,
        query: String,
    ): Flow<PagingData<AppUserVo>>

}