package com.galaxytechno.chat.data.remote

import com.galaxytechno.chat.common.Endpoint
import com.galaxytechno.chat.model.dto.*
import com.galaxytechno.chat.model.request.DoTwoFactorRequest
import com.galaxytechno.chat.model.request.PasswordResetRequest
import com.galaxytechno.chat.model.request.SignupRequest
import com.galaxytechno.chat.model.request.VerifyConfirmedQuestionRequest
import com.galaxytechno.chat.model.response.VerifiedResponse
import com.galaxytechno.chat.movie.ListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    //MASTER
    @GET(Endpoint.MASTER_LANGUAGES)
    suspend fun getLanguages(): Response<LanguagesResponse>

    @GET(Endpoint.MASTER_QUESTIONS)
    suspend fun getQuestions(
        @Query("locale") locale: Int
    ): Response<QuestionsResponse>

    @GET(Endpoint.MASTER_COUNTRIES)
    suspend fun getCountries(
        @Query("locale") locale: Int
    ): Response<CountriesResponse>

    //AUTH
    @POST(Endpoint.SIGNUP)
    suspend fun signup(
        @Body request: SignupRequest
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST(Endpoint.LOGIN)
    suspend fun login(
        @Field("countryId") countryId: Int,
        @Field("mobile") mobile: String,
        @Field("password") password: String,
        @Field("uuid") uuid: String,
        @Field("isMobile") isMobile: Boolean,
        @Field("locale") locale: Int
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST(Endpoint.REQUEST_OTP)
    suspend fun requestOtp(
        @Field("mobile") mobile: String,
        @Field("countryId") countryId: Int,
        @Field("locale") locale: Int
    ): Response<GetOtpResponse>

    @FormUrlEncoded
    @POST(Endpoint.VALIDATE_OTP)
    suspend fun validateOtp(
        @Field("countryId") countryId: Int,
        @Field("mobile") mobileNumber: String,
        @Field("otpCode") otpCode: String,
        @Field("locale") locale: Int
    ): Response<ValidateOtpResponse>

    @FormUrlEncoded
    @POST(Endpoint.REQUEST_CONFIRM_QUESTIONS)
    suspend fun requestConfirmedQuestions(
        @Field("countryId") countryId: Int,
        @Field("mobile") mobile: String,
        @Field("locale") locale: Int
    ): Response<GetConfirmedQuestionsResponse>

    @POST(Endpoint.VERIFY_CONFIRM_QUESTIONS)
    suspend fun validateConfirmedQuestions(
        @Body request: VerifyConfirmedQuestionRequest
    ): Response<VerifiedResponse>

    @POST(Endpoint.FRI_CONTACT_CHECK)
    suspend fun friContactCheck(
        @Body request: FriContactCheckRequest
    ): Response<FriContactCheckResponse>


    @PUT(Endpoint.RESET_PASSWORD)
    suspend fun resetPassword(
        @Body request: PasswordResetRequest
    ): Response<VerifiedResponse>

    @FormUrlEncoded
    @POST(Endpoint.PROFILE_CHECK_BY_MOBILE)
    suspend fun checkAccountStatus(
        @Field("countryId") countryId: Int,
        @Field("mobile") mobile: String,
        @Field("locale") locale: Int
    ): Response<CheckAccountByMobileResponse>

    @FormUrlEncoded
    @POST(Endpoint.LOGOUT)
    suspend fun logout(
        @Field("userId") userId: Long,
        @Field("isMobile") isMobile: Boolean,
        @Field("locale") locale: Int
    ): Response<VerifiedResponse>

    //USER
    @FormUrlEncoded
    @POST(Endpoint.PROFILE_INFO_BY_USER_ID)
    suspend fun getProfileInfo(
        @Field("userId") userId: Long,
        @Field("locale") locale: Int
    ): Response<ProfileInfoResponse>

    @FormUrlEncoded
    @POST(Endpoint.PROFILE_INFO_BY_FRIEND_ID)
    suspend fun getContactProfileInfo(
        @Field("userId") userId: Long,
        @Field("friendId") friendId: Long,
        @Field("locale") locale: Int
    ): Response<ContactProfileInfoResponse>

    @FormUrlEncoded
    @POST(Endpoint.PROFILE_INFO_BY_FRIEND_ID)
    suspend fun getFriendProfileInfo(
        @Field("userId") userId: Long,
        @Field("friendId") friendId: Long,
        @Field("locale") locale: Int
    ): Response<FriendProfileInfoResponse>

    @FormUrlEncoded
    @POST(Endpoint.GET_FRI_LIST)
    suspend fun getFriList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Field("userId") userId: Long,
        @Field("username") searchedUsername: String,
        @Field("locale") locale: Int
    ): Response<GetFriendListResponse>


    @FormUrlEncoded
    @POST(Endpoint.GET_FRI_SEARCH)
    suspend fun getFriSearch(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Field("userId") userId: Long,
        @Field("username") searchedUsername: String,
        @Field("locale") locale: Int
    ): Response<GetSearchFriListResponse>

    @FormUrlEncoded
    @POST(Endpoint.GET_FRI_REQ_LIST)
    suspend fun getNotiFriRequestList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Field("userId") userId: Long,
        @Field("locale") locale: Int
    ): Response<FriRequestResponse>

    @FormUrlEncoded
    @POST(Endpoint.BLOCK_LIST)
    suspend fun postRequestBlockList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Field("userId") userId: Long,
        @Field("locale") locale: Int
    ): Response<BlockListResponse>


    @Multipart
    @POST(Endpoint.PROFILE_INFO)
    suspend fun updateProfileInfo(
        @Path("userId") userId: Long,
        @Part("name") name: RequestBody,
        @Part("bio") bio: RequestBody,
        @Part("email") email: RequestBody,
        @Part("birthDate") birthDate: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part profileImage: MultipartBody.Part? = null,
        @Part coverImage: MultipartBody.Part? = null,
        @Part("isProfileImgRemove") isProfileImgRemove: RequestBody,
        @Part("isCoverImgRemove") isCoverImgRemove: RequestBody,
        @Part("locale") locale: RequestBody,
    ): Response<ProfileInfoResponse>

    @Multipart
    @POST(Endpoint.CREATE_CHAT_GROUP_MEMBER)
    suspend fun createChatGroupMember(
        @Part("userId") userId: RequestBody,
        @Part("groupName") groupName: RequestBody,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("memberIds") memberIdLists: RequestBody,
        @Part("locale") locale: RequestBody
    ): Response<CreateGroupResponse>


    @PUT(Endpoint.DO_TWO_FACTOR)
    suspend fun doTwoFactor(
        @Body request: DoTwoFactorRequest
    ): Response<VerifiedResponse>


    //todo : for later
    @GET(Endpoint.REFRESH_TOKEN)
    suspend fun refreshToken(
        @Header(Endpoint.AUTHORIZATION) token: String
    ): Response<RefreshTokenDTO>

    //todo : popo's testing api
    @GET(Endpoint.MASTER_COUNTRY_LIST)
    suspend fun getChatRecentMsg(
        @Query("locale") locale: Int
    ): Response<GetFriendListResponse>

    //test
    @GET("https://api.themoviedb.org/3/movie/upcoming?api_key=cdbea55de27a909b4aaa2cfc02eabb75")
    suspend fun fetchMovies(
        @Query("page") pageNumber: Int,
    ): Response<ListResponse>

    @GET("https://iix1ylnorg.execute-api.ap-southeast-1.amazonaws.com/test/language/list")
    suspend fun getBankList(): Response<BankListResponse>

    @FormUrlEncoded
    @POST(Endpoint.ADD_FRIEND_REQUEST)
    suspend fun addFriend(
        @Field("userId") userId: Long,
        @Field("friendId") friendId: Long,
        @Field("locale") locale: Int
    ): Response<FriendAddResponse>


    @PUT(Endpoint.CONFIRM_FRIEND_REQUEST)
    suspend fun confirmFriendRequest(
        @Body request: FriendRequestConfirmRequest
    ): Response<VerifiedResponse>

    @PUT(Endpoint.CANCEL_REQUEST_BY_REQUESTER)
    suspend fun cancelRequestByRequester(
        @Body request: FriRequestedCancelRequest
    ): Response<VerifiedResponse>



    @FormUrlEncoded
    @POST(Endpoint.UNFRIEND_REQUEST)
    suspend fun unFriend(
        @Field("userId") userId: Long,
        @Field("friendId") friendId: Long,
        @Field("locale") locale: Int
    ): Response<UnfriendResponse>

    @FormUrlEncoded
    @POST(Endpoint.BLOCK_REQUEST)
    suspend fun block(
        @Field("userId") userId: Long,
        @Field("friendId") friendId: Long,
        @Field("isBlock") isBlock: Boolean,
        @Field("locale") locale: Int
    ): Response<BlockResponse>

    @FormUrlEncoded
    @POST(Endpoint.SEARCH_USER)
    suspend fun searchUser(
        @Field("userId") userId: Long,
        @Field("username") query: String,
        @Field("locale") locale: Int,
        @Query("offset") page: Int,
        @Query("limit") loadSize: Int,
    ): Response<UsersDTO>


}