package com.galaxytechno.chat.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.galaxytechno.chat.common.Constant
import com.galaxytechno.chat.common.RemoteDataSource
import com.galaxytechno.chat.data.db.DbDataSource
import com.galaxytechno.chat.data.ds.DsDataSource
import com.galaxytechno.chat.data.remote.ApiService
import com.galaxytechno.chat.di.Qualifier
import com.galaxytechno.chat.model.dto.*
import com.galaxytechno.chat.model.entity.UserEntity
import com.galaxytechno.chat.model.request.DoTwoFactorRequest
import com.galaxytechno.chat.model.response.VerifiedResponse
import com.galaxytechno.chat.model.vos.AppUserVo
import com.galaxytechno.chat.model.vos.FriListVos
import com.galaxytechno.chat.movie.Movie
import com.galaxytechno.chat.movie.MoviePagingDataSource
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.paging.AppUsersDataSource
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.paging.FriendsDataSource
import com.galaxytechno.chat.util.TypeConverter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

open class UserRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val db: DbDataSource,
    private val ds: DsDataSource,
    @Qualifier.Io private val io: CoroutineDispatcher,
    @Qualifier.RepoScope private val repoScope: CoroutineScope,
    private val apiService: ApiService
) : UserRepository, RemoteDataSource() {

    private var repoLocale: Int? = null
    private var userId: Long? = null

    private val config = PagingConfig(
        pageSize = Constant.PAGE_SIZE,
        enablePlaceholders = false
    )

    init {
        repoScope.launch {
            repoLocale = getLocaleFromRepo()
        }
    }


    private suspend fun getLocaleFromRepo(): Int {
        return ds.pullLocaleStatus().first()
    }

    //DS
    override suspend fun saveAccessToken(token: String) {
        withContext(io) {
            ds.putAccessToken(token)
        }
    }

    override suspend fun getAccessToken(): Flow<String> {
        return ds.pullAccessToken()
    }

    override suspend fun saveRefreshToken(token: String) {
        withContext(io) {
            ds.putRefreshToken(token)
        }
    }

    override suspend fun getRefreshToken(): Flow<String> {
        return ds.pullRefreshToken()
    }

    override suspend fun removeAccessToken() {
        withContext(io) {
            ds.removeAccessToken()
        }
    }

    override suspend fun removeRefreshToken() {
        withContext(io) {
            ds.removeRefreshToken()
        }
    }

    //DB
    override suspend fun saveUserToDb(userEntity: UserEntity) {
        withContext(io) {
            db.saveUser(userEntity)
        }
    }

    override suspend fun getUserFromDb(): Flow<UserEntity> {
        return flow {
            emit(
                db.getUser()
            )
            repoScope.launch {
                userId = db.getUser().userId
            }
        }.flowOn(io)
    }

    override suspend fun deleteUserFromDb() {
        withContext(io) {
            db.deleteUser()
        }

    }

    //API
    override suspend fun fetchRefreshToken(token: String): Flow<RemoteEvent<RefreshTokenDTO>> {
        return flow {
            emit(
                safeApiCall {
                    api.refreshToken(token)
                }
            )
        }.flowOn(io)
    }


    override suspend fun userProfileInfo(): Flow<RemoteEvent<ProfileInfoResponse>> {
        return flow {
            emit(
                safeApiCall {
                    api.getProfileInfo(
                        userId = userId ?: 0,
                        locale = repoLocale ?: -1
                    )
                }
            )
        }.flowOn(io)
    }

    override suspend fun profileInfoUpload(
        name: String,
        bio: String?,
        email: String?,
        birthDate: String?,
        gender: Int?,
        profileImg: File?,
        coverImg: File?,
        isProfileImgRemove: Boolean,
        isCoverImgRemove: Boolean,
    ): Flow<RemoteEvent<ProfileInfoResponse>> {
        val names = TypeConverter.createPartFromString(name)

        val bios = bio?.let { TypeConverter.createPartFromString(bio) }
            ?: TypeConverter.createPartFromString(null)
        val emails = email?.let { TypeConverter.createPartFromString(email) }
            ?: TypeConverter.createPartFromString(null)
        val birthDates =
            birthDate?.let { TypeConverter.createPartFromString(birthDate) }
                ?: TypeConverter.createPartFromString(null)
        val genders =
            gender?.let { TypeConverter.createPartFromString(gender.toString()) }
                ?: TypeConverter.createPartFromString(null)
        val isProfileImgRemoves = TypeConverter.createPartFromString(isProfileImgRemove.toString())
        val isCoverImgRemoves = TypeConverter.createPartFromString(isCoverImgRemove.toString())
        val locales = TypeConverter.createPartFromString(repoLocale.toString())

        val profileType = profileImg?.let {
            TypeConverter.createPartFile("profileImg", profileImg)
        } ?: TypeConverter.createPartFileNullable("")

        val coverType = coverImg?.let {
            TypeConverter.createPartFile("coverImg", coverImg)
        } ?: TypeConverter.createPartFileNullable("")
        return flow {
            emit(
                safeApiCall {
                    api.updateProfileInfo(
                        userId = userId ?: 0,
                        name = names,
                        bio = bios,
                        email = emails,
                        birthDate = birthDates,
                        gender = genders,
                        profileImage = profileType,
                        coverImage = coverType,
                        isProfileImgRemove = isProfileImgRemoves,
                        isCoverImgRemove = isCoverImgRemoves,
                        locale = locales
                    )
                }
            )
        }.flowOn(io)
    }

    override suspend fun createChatGroup(
        groupName: String,
        description: String?,
        image: File?,
        memberIdLists: String,
    ): Flow<RemoteEvent<CreateGroupResponse>> {

        val coverPhoto = image?.let { file ->
            TypeConverter.createPartFile("headImage", file)
        } ?: TypeConverter.createPartFileNullable("nothingChange")
        return flow<RemoteEvent<CreateGroupResponse>> {
            safeApiCall {
                api.createChatGroupMember(
                    userId = TypeConverter.createPartFromString(userId.toString()),
                    groupName = TypeConverter.createPartFromString(groupName),
                    description = TypeConverter.createPartFromString(groupName),
                    image = coverPhoto,
                    memberIdLists = TypeConverter.createPartFromString(groupName),
                    locale = TypeConverter.createPartFromString(repoLocale.toString())
                )
            }
        }.flowOn(io)
    }

    override suspend fun logout(
    ): Flow<RemoteEvent<VerifiedResponse>> {
        return flow {
            emit(
                safeApiCall {
                    api.logout(
                        userId = userId ?: 0,
                        isMobile = true,
                        locale = repoLocale ?: -1
                    )
                }
            )
        }.flowOn(io)
    }

    override suspend fun doTwoFactor(
        isTwoFactor: Boolean
    ): Flow<RemoteEvent<VerifiedResponse>> {
        val request = DoTwoFactorRequest(
            userId = userId ?: 0,
            isTwoFactor = isTwoFactor,
            locale = repoLocale ?: -1
        )
        return flow {
            emit(
                safeApiCall {
                    api.doTwoFactor(
                        request = request
                    )
                }
            )
        }.flowOn(io)
    }

    override suspend fun friContactCheck(
        mobiles: List<String>
    ): Flow<RemoteEvent<FriContactCheckResponse>> {
        val request = FriContactCheckRequest(
            userId = userId ?: 0,
            locale = repoLocale ?: -1,
            mobiles = mobiles
        )
        return flow {
            emit(
                safeApiCall {
                    api.friContactCheck(
                        request = request
                    )
                }
            )
        }.flowOn(io)
    }

    override suspend fun getFriList(
        offset: Int,
        limit: Int,
        searchedUserName: String,
    ): Flow<RemoteEvent<GetFriendListResponse>> {
        return flow {
            emit(
                safeApiCall {
                    api.getFriList(
                        offset = offset,
                        limit = limit,
                        userId = userId ?: 0,
                        searchedUsername = searchedUserName,
                        locale = repoLocale ?: 1,
                    )
                }
            )
        }.flowOn(io)
    }

    override suspend fun getFriSearch(
        offset: Int,
        limit: Int,
        searchedUserName: String
    ): Flow<RemoteEvent<GetSearchFriListResponse>> {
        return flow {
            emit(
                safeApiCall {
                    api.getFriSearch(
                        offset = offset,
                        limit = limit,
                        userId = userId ?: 0,
                        searchedUsername = searchedUserName,
                        locale = repoLocale ?: 1
                    )
                }
            )
        }.flowOn(io)
    }

    override suspend fun getNotiFriRequestList(
        offset: Int,
        limit: Int,
    ): Flow<RemoteEvent<FriRequestResponse>> {
        return flow {
            emit(
                safeApiCall {
                    api.getNotiFriRequestList(
                        offset = offset,
                        limit = limit,
                        userId = userId ?: 0,
                        locale = repoLocale ?: -1
                    )
                }
            )
        }.flowOn(io)
    }

    override suspend fun getBlockList(
        offset: Int,
        limit: Int
    ): Flow<RemoteEvent<BlockListResponse>> {
        return flow {
            emit(
                safeApiCall {
                    api.postRequestBlockList(
                        offset = offset,
                        limit = limit,
                        userId = userId ?: 0,
                        locale = repoLocale ?: 1
                    )
                }
            )
        }.flowOn(io)
    }


    override suspend fun getChatRecentMsg(): Flow<RemoteEvent<GetFriendListResponse>> {
        return flow {
            emit(
                safeApiCall {
                    api.getChatRecentMsg(locale = repoLocale ?: 0)
                }
            )
        }.flowOn(io)
    }

    override suspend fun getBankList(): Flow<RemoteEvent<BankListResponse>> {
        return flow {
            emit(
                safeApiCall {
                    api.getBankList()
                }
            )
        }.flowOn(io)
    }


    override suspend fun getPagingMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = MoviePagingDataSource.PAGE_SIZE, // mandatory (others are optional)
//                maxSize = MoviePagingDataSource.MAX_SIZE, //cache size
//                initialLoadSize = MoviePagingDataSource.INITIAL_LOAD_SIZE, //initial load
//                prefetchDistance = 2,
//                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingDataSource(apiService = apiService) }
        ).flow
    }

    override suspend fun contactProfileInfo(
        friendId: Long
    ): Flow<RemoteEvent<ContactProfileInfoResponse>> {
        return flow {
            emit(
                safeApiCall {
                    api.getContactProfileInfo(
                        userId = userId ?: 0,
                        friendId = friendId,
                        locale = repoLocale ?: -1
                    )
                }
            )
        }.flowOn(io)
    }

    override suspend fun addFriend(
        friendId: Long
    ): Flow<RemoteEvent<FriendAddResponse>> {
        return flow {
            emit(
                safeApiCall {
                    api.addFriend(
                        userId = userId ?: 0,
                        friendId = friendId,
                        locale = repoLocale ?: 0
                    )
                }
            )
        }.flowOn(io)
    }

    override suspend fun confirmFriendRequest(
        friendId: Long,
        isAccept: Boolean
    ): Flow<RemoteEvent<VerifiedResponse>> {
        val request = FriendRequestConfirmRequest(
            userId = userId ?: 0,
            friendId = friendId,
            isAccept = isAccept,
            locale = repoLocale ?: 0
        )
        return flow {
            emit(
                safeApiCall {
                    api.confirmFriendRequest(
                        request
                    )
                }
            )
        }.flowOn(io)
    }

    override suspend fun cancelFriendRequest(friendId: Long
    ): Flow<RemoteEvent<VerifiedResponse>> {
      return flow {
          emit(
              safeApiCall {
                  var request = FriRequestedCancelRequest(
                      userId = userId ?: 0,
                      friendId = friendId,
                      locale = repoLocale ?: 0
                  )
                  api.cancelRequestByRequester(request = request)
              }
          )
      }
    }

    override suspend fun friendProfileInfo(
        friendId: Long
    ): Flow<RemoteEvent<FriendProfileInfoResponse>> {
        return flow {
            emit(
                safeApiCall {
                    api.getFriendProfileInfo(
                        userId = userId ?: 0,
                        friendId = friendId,
                        locale = repoLocale ?: -1
                    )
                }
            )
        }.flowOn(io)
    }

    override suspend fun unFriend(
        friendId: Long
    ): Flow<RemoteEvent<UnfriendResponse>> {
        return flow {
            emit(
                safeApiCall {
                    api.unFriend(
                        userId = userId ?: 0,
                        friendId = friendId,
                        locale = repoLocale ?: -1
                    )
                }
            )
        }.flowOn(io)
    }

    override suspend fun block(
        friendId: Long,
        isBlock: Boolean
    ): Flow<RemoteEvent<BlockResponse>> {
        return flow {
            emit(
                safeApiCall {
                    api.block(
                        userId = userId ?: 0,
                        friendId = friendId,
                        isBlock = isBlock,
                        locale = repoLocale ?: -1
                    )
                }
            )
        }.flowOn(io)
    }

    //create room
    override suspend fun searchUsers(
        userId: Long,
        locale: Int,
        query: String
    ): Flow<PagingData<AppUserVo>> {
        return Pager(
            config = config,
            pagingSourceFactory = {
                AppUsersDataSource(
                    api = apiService,
                    userId = userId,
                    locale = locale,
                    queryName = query
                )
            }
        ).flow
    }

    override suspend fun searchUsersForCreateGroup(
        userId: Long,
        locale: Int,
        query: String
    ): Flow<PagingData<AppUserVo>> {
        return Pager(
            config = config,
            pagingSourceFactory = {
                AppUsersDataSource(
                    api = apiService,
                    userId = userId,
                    locale = locale,
                    queryName = query
                )
            }
        ).flow
    }

    override suspend fun getAlreadyFriendsWithHeader(
        userId: Long,
        locale: Int,
        query: String
    ): Flow<PagingData<FriListVos>> {
        return Pager(
            config = config,
            pagingSourceFactory = {
                FriendsDataSource(
                    api = apiService,
                    userId = userId,
                    locale = locale,
                    queryName = query

                )
            }
        ).flow
    }
}