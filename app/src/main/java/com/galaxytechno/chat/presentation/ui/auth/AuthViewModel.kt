package com.galaxytechno.chat.presentation.ui.auth

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galaxytechno.chat.common.Constant
import com.galaxytechno.chat.domain.AppRepository
import com.galaxytechno.chat.domain.AuthRepository
import com.galaxytechno.chat.domain.UserRepository
import com.galaxytechno.chat.model.dto.*
import com.galaxytechno.chat.model.request.ConfirmedQuestionsAndAnswers
import com.galaxytechno.chat.model.request.QuestAndAnsObj
import com.galaxytechno.chat.model.response.VerifiedResponse
import com.galaxytechno.chat.presentation.extension.RemoteEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val userRepo: UserRepository,
    private val appRepo: AppRepository,
) : ViewModel() {
    init {
        getCountries()
        getQuestions()
    }

    /** set country data obj in view model */
    private val _selectedCountry = MutableLiveData(Country())
    val selectedCountry: LiveData<Country> get() = _selectedCountry
    fun setSelectedCountry(countryObj: Country) {
        viewModelScope.launch {
            _selectedCountry.value = countryObj
        }
    }

    private val _selectedPhone = MutableLiveData("")
    val selectedPhone: LiveData<String> get() = _selectedPhone
    fun setSelectedPhone(phone: String) {
        _selectedPhone.value = phone
    }

    private val _selectedAccountName = MutableLiveData("")
    val selectedAccountName: LiveData<String> get() = _selectedAccountName
    private fun setSelectedAccountName(name: String) {
        _selectedAccountName.value = name
    }

    private val _selectedAccountUrl = MutableLiveData("")
    val selectedAccountUrl: LiveData<String> get() = _selectedAccountUrl
    private fun setSelectedAccountUrl(url: String) {
        _selectedAccountUrl.value = url
    }

    private var selectedName: String = ""
    fun setSelectedName(name: String) {
        selectedName = name
    }

    private var selectedPassword: String = ""
    fun setSelectedPassword(password: String) {
        selectedPassword = password
    }

    private var selectedConfirmedQuestions: List<FwdPwdQuestObj> = listOf()
    fun setSelectedConfirmedQuestions(forgetPwdQuestObj: List<FwdPwdQuestObj>) {
        viewModelScope.launch {
            selectedConfirmedQuestions = forgetPwdQuestObj
        }
    }

    /** countries needed in phone prefix (+95) etc. */
    private val _countryResponse = MutableLiveData<RemoteEvent<CountriesResponse>>()
    val countryResponse: LiveData<RemoteEvent<CountriesResponse>> get() = _countryResponse
    private fun getCountries() {
        viewModelScope.launch {
            authRepo.getCountries().collect {
                delay(500)
                _countryResponse.value = it
            }
        }
    }

    /** security questions in register step */
    private val _questionAList = MutableLiveData<MutableList<SecurityQuestObj>>()
    val questionAList: LiveData<MutableList<SecurityQuestObj>> get() = _questionAList

    private val _questionBList = MutableLiveData<MutableList<SecurityQuestObj>>()
    val questionBList: LiveData<MutableList<SecurityQuestObj>> get() = _questionBList

    private val _questionCList = MutableLiveData<MutableList<SecurityQuestObj>>()
    val questionCList: LiveData<MutableList<SecurityQuestObj>> get() = _questionCList

    private fun getQuestions() {
        viewModelScope.launch {
            authRepo.getQuestions().collect {
                _securityQuestAObj.value = it

                it.data?.let { res ->
                    Timber.tag("securityquest").d(res.message)

                    val listA = mutableListOf<SecurityQuestObj>()
                    val listB = mutableListOf<SecurityQuestObj>()
                    val listC = mutableListOf<SecurityQuestObj>()

                    //todo : 3 lists have to add asynchronously
                    res.data?.securityQuesList_A?.forEach { obj ->
                        listA.add(obj)
                    }
                    res.data?.securityQuesList_B?.forEach { obj ->
                        listB.add(obj)
                    }
                    res.data?.securityQuesList_C?.forEach { obj ->
                        listC.add(obj)
                    }
                    _questionAList.postValue(listA)
                    _questionBList.postValue(listB)
                    _questionCList.postValue(listC)
                } ?: kotlin.run {
                    //todo : if security question master data returns fail or error
                }
            }
        }
    }

    /** login */
    private val _loginChannel = Channel<RemoteEvent<LoginResponse>>()
    val loginEvent get() = _loginChannel.receiveAsFlow()
    fun requestMobileLogin(
        phone: String,
        password: String,
    ) {
        viewModelScope.launch {
            authRepo.login(
                countryId = selectedCountry.value?.id ?: -1,
                mobile = phone,
                password = password,
            ).collect {
                setLoadingState(true)
                delay(1000L)
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        setLoadingState(false)
                        _loginChannel.send(RemoteEvent.ErrorEvent(it.message ?: "Error"))
                    }
                    is RemoteEvent.LoadingEvent -> {
                        setLoadingState(true)
                    }
                    is RemoteEvent.SuccessEvent -> when (it.data?.status) {
                        Constant.STATUS_SUCCESS -> {
                            setLoadingState(false)
                            //todo : save accessToken, refreshToken
                            val isSavedAuthState = async {
                                saveAuthState()
                            }
                            val isSavedUser = async {
                                saveUserEntity(
                                    user = it.data.data!!.profileInfo
                                )
                            }
                            if (isSavedAuthState.await() && isSavedUser.await()) {
                                _loginChannel.send(RemoteEvent.SuccessEvent(data = it.data))
                                delay(2000)
                                setLoadingState(false)

                            }
                        }
                        Constant.STATUS_FAIL -> {
                            setLoadingState(false)
                            _loginChannel.send(
                                RemoteEvent.ErrorEvent(
                                    errorMessage = it.data.error ?: "Fail Login"
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    /** LOADING STATE for progress dialog */
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading
    fun setLoadingState(state: Boolean) {
        viewModelScope.launch {
            _isLoading.emit(state)
        }
    }

    /** otp requesting in sign up screen and forget pwd screen */
    //todo: Response that will do once will be one time event ( SharedFlow )
    // locale Status has to moved RepoImpl
    private val _otpResponseChannel = Channel<RemoteEvent<GetOtpResponse>>()
    val otpResponseEvent get() = _otpResponseChannel.receiveAsFlow()
    fun getOtpCode() {
        viewModelScope.launch {
            authRepo.requestOtp(
                mobile = selectedPhone.value ?: "",
                countryId = selectedCountry.value?.id ?: -1,
            ).collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        setLoadingState(false)
                        Timber.tag("error").d("Error Event")
                    }
                    is RemoteEvent.LoadingEvent -> {
                        Timber.tag("error").d("Loading Event")
                    }
                    is RemoteEvent.SuccessEvent -> {
                        if (it.data?.data?.isRegister == true) {
                            _otpResponseChannel.send(RemoteEvent.SuccessEvent(it.data))
                            setOtpObj(it.data)
                        } else {
                            _otpResponseChannel.send(RemoteEvent.SuccessEvent(it.data!!))
                            setOtpObj(it.data)
                        }
                        setLoadingState(false)
                    }
                }
            }
        }
    }

    /** otp response validation */
    private val _validateOtpObj = MutableSharedFlow<RemoteEvent<ValidateOtpResponse>>()
    val validateOtpObj: SharedFlow<RemoteEvent<ValidateOtpResponse>> get() = _validateOtpObj
    fun validateOtp(
        otpCode: String
    ) {
        viewModelScope.launch {
            authRepo.validateOTP(
                countryId = selectedCountry.value!!.id,
                mobile = selectedPhone.value ?: "",
                otpCode = otpCode,
            ).collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        setLoadingState(false)
                    }
                    is RemoteEvent.LoadingEvent -> {
                        setLoadingState(true)
                    }
                    is RemoteEvent.SuccessEvent -> {
                        _validateOtpObj.emit(it)
                        setLoadingState(false)

                    }
                }

            }
        }
    }

    /** posting sign up to (Register ) */
    private val _signupRequest = Channel<RemoteEvent<LoginResponse>>()
    val signupRequest get() = _signupRequest.receiveAsFlow()

    private fun postSignUp(
        countryId: Int,
        name: String,
        mobile: String,
        password: String,
        securityQuestionUsage: HashSet<QuestAndAnsObj>,
    ) {
        viewModelScope.launch {
            authRepo.signUp(
                countryId = countryId,
                name = name,
                mobile = mobile,
                password = password,
                securityQuestionUsage = securityQuestionUsage,
            ).collect {

                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        setLoadingState(false)
                        //todo : show something
                    }

                    is RemoteEvent.LoadingEvent -> {
                        setLoadingState(true)
                    }
                    is RemoteEvent.SuccessEvent -> when (it.data!!.status) {
                        Constant.STATUS_SUCCESS -> {
                            setLoadingState(false)
                            //todo : save tokens.
                            val isSavedAuthState = async {
                                saveAuthState()
                            }
                            val isSavedUser = async {
                                saveUserEntity(
                                    user = it.data.data!!.profileInfo
                                )
                            }
                            if (isSavedAuthState.await() && isSavedUser.await()) {
                                Log.d("2_Loading_false_state", isLoading.value.toString())
                                _signupRequest.send(RemoteEvent.SuccessEvent(it.data))
                            }

                        }
                    }
                }
            }
        }
    }

    var timerSignUp: CountDownTimer? = null

    /** getting signUp user answer to viewModel */
    private val _answerA = MutableLiveData<String>()
    private val answerA: LiveData<String> get() = _answerA
    fun setAnswerA(answer: String) {
        viewModelScope.launch {
            _answerA.postValue(answer)
        }
    }

    private val _answerB = MutableLiveData<String>()
    private val answerB: LiveData<String> get() = _answerB
    fun setAnswerB(answer: String) {
        viewModelScope.launch {
            _answerB.postValue(answer)
        }
    }

    private val _answerC = MutableLiveData<String>()
    private val answerC: LiveData<String> get() = _answerC
    fun setAnswerC(answer: String) {
        viewModelScope.launch {
            _answerC.postValue(answer)
        }
    }

    /** user have twoFactor auth? to store in viewModel */
    private val _isTwoFactor = MutableLiveData<Boolean>()
    val isTwoFactor: LiveData<Boolean> get() = _isTwoFactor
    fun setTwoFactorState(isTwoFactor: Boolean) {
        viewModelScope.launch {
            _isTwoFactor.postValue(isTwoFactor)
        }
    }

    /** getting forget pwd question's answer to viewModel */
    private val _forgetPwdAnswerA = MutableLiveData<String>()
    private val forgetPwdAnswerA: LiveData<String> get() = _forgetPwdAnswerA
    fun setForgetPwdAnswerA(answer: String) {
        viewModelScope.launch {
            _forgetPwdAnswerA.postValue(answer)
            //todo we got answers
        }
    }

    private val _forgetPwdAnswerB = MutableLiveData<String>()
    private val forgetPwdAnswerB: LiveData<String> get() = _forgetPwdAnswerB
    fun setForgetPwdAnswerB(answer: String) {
        viewModelScope.launch {
            _forgetPwdAnswerB.postValue(answer)
        }
    }

    private val _forgetPwdAnswerC = MutableLiveData<String>()
    private val forgetPwdAnswerC: LiveData<String> get() = _forgetPwdAnswerC
    fun setForgetPwdAnswerC(answer: String) {
        viewModelScope.launch {
            _forgetPwdAnswerC.postValue(answer)
        }
    }

    private val _securityQuestAObj = MutableLiveData<RemoteEvent<QuestionsResponse>>()
    val questionsAObj: LiveData<RemoteEvent<QuestionsResponse>> get() = _securityQuestAObj

    /** request user data prepare to signup */
    private val questAndAnswerList: HashSet<QuestAndAnsObj> = hashSetOf()
    fun requestSignUp() {
        //todo form validate
        questAndAnswerList.clear()
        val questAndAnsObjA = QuestAndAnsObj(
            questionType = questAObj.value?.questionType!!,
            questionId = questAObj.value?.questionId!!,
            question = questAObj.value?.question!!,
            answer = answerA.value!!
        )
        questAndAnswerList.add(questAndAnsObjA)

        val questAndAnsObjB = QuestAndAnsObj(
            questionType = questBObj.value?.questionType!!,
            questionId = questBObj.value?.questionId!!,
            question = questBObj.value?.question!!,
            answer = answerB.value!!
        )
        questAndAnswerList.add(questAndAnsObjB)

        val questAndAnsObjC = QuestAndAnsObj(
            questionType = questCObj.value?.questionType!!,
            questionId = questCObj.value?.questionId!!,
            question = questCObj.value?.question!!,
            answer = answerC.value!!
        )
        questAndAnswerList.add(questAndAnsObjC)
        postSignUp(
            countryId = selectedCountry.value?.id ?: -1,
            name = selectedName,
            mobile = selectedPhone.value ?: "",
            password = selectedPassword,
            securityQuestionUsage = questAndAnswerList
        )
    }

    /** requesting forget pwd question and answer confirmation */
    private val _forgetPwdQuestAndAnsConfirmation = Channel<RemoteEvent<VerifiedResponse>>()
    val forgetPwdQuestAndAnsRequestConfirmation get() = _forgetPwdQuestAndAnsConfirmation.receiveAsFlow()
    fun validateConfirmedQuestions() {
        //todo : we set questions and answers in this
        val answerList = HashSet<ConfirmedQuestionsAndAnswers>().apply {
            this.add(
                ConfirmedQuestionsAndAnswers(
                    id = selectedConfirmedQuestions[0].id,
                    questionId = selectedConfirmedQuestions[0].questionId,
                    answer = forgetPwdAnswerA.value ?: ""
                )
            )
            this.add(
                ConfirmedQuestionsAndAnswers(
                    id = selectedConfirmedQuestions[1].id,
                    questionId = selectedConfirmedQuestions[1].questionId,
                    answer = forgetPwdAnswerB.value ?: ""
                )
            )
            this.add(
                ConfirmedQuestionsAndAnswers(
                    id = selectedConfirmedQuestions[2].id,
                    questionId = selectedConfirmedQuestions[2].questionId,
                    answer = forgetPwdAnswerC.value ?: ""
                )
            )
        }

        /** verify quest and answer in forget pwd verification */
        viewModelScope.launch {
            authRepo.verifyConfirmedQuestions(
                countryId = selectedCountry.value?.id ?: -1,
                mobile = selectedPhone.value ?: "",
                answerLists = answerList

            ).collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        setLoadingState(false)
                        _forgetPwdQuestAndAnsConfirmation.send(
                            RemoteEvent.ErrorEvent(
                                it.message ?: "Error"
                            )
                        )
                    }
                    is RemoteEvent.LoadingEvent -> {
                        setLoadingState(true)
                    }
                    is RemoteEvent.SuccessEvent -> {
                        _forgetPwdQuestAndAnsConfirmation.send(RemoteEvent.SuccessEvent(data = it.data!!))
                        setLoadingState(false)
                    }
                }

            }
        }
    }


    /** country code re-set to Login Screen when country filter bottom sheet */
    private val _signUpPassword = MutableLiveData<String>()
    val signUpPassword: LiveData<String> get() = _signUpPassword
    fun setSignUpPassword(password: String) {
        viewModelScope.launch {
            _signUpPassword.postValue(password)
        }
    }

    private val _countryId = MutableLiveData<Int>()
    val countryId: LiveData<Int> get() = _countryId
    fun setCountryId(country: Int) {
        viewModelScope.launch {
            _countryId.postValue(country)
            Timber.tag("country").d("country$country")
        }
    }

    /** just save mobile number to view model*/
    private val _mobile = MutableLiveData<String>()
    val mobile: LiveData<String> get() = _mobile
    fun setMobileNumber(mobile: String) {
        viewModelScope.launch {
            _mobile.postValue(mobile)
        }
    }

    /** just save signup mobile number to view model*/
    private val _signUpOtpMobile = MutableLiveData<String>("0")
    val signUpOtpMobile: LiveData<String> get() = _signUpOtpMobile
    fun setSignUpOtpMobileNumber(mobile: String) {
        viewModelScope.launch {
            _signUpOtpMobile.postValue(mobile)
        }
    }

    /** just save signup mobile number to view model*/
    private val _forgotPwdOtpMobile = MutableLiveData<String>("0")
    val forgotPwdOtpMobile: LiveData<String> get() = _forgotPwdOtpMobile
    fun setForgotPwdOtpMobileNumber(mobile: String) {
        viewModelScope.launch {
            _forgotPwdOtpMobile.postValue(mobile)
        }
    }

    /** saving user question obj */
    private val _questAObj = MutableLiveData<SecurityQuestObj>()
    val questAObj: LiveData<SecurityQuestObj> get() = _questAObj
    fun setQuestAObj(questObj: SecurityQuestObj) {
        viewModelScope.launch {
            _questAObj.postValue(questObj)
        }
    }

    private val _questBObj = MutableLiveData<SecurityQuestObj>()
    val questBObj: LiveData<SecurityQuestObj> get() = _questBObj
    fun setQuestBObj(questObj: SecurityQuestObj) {
        viewModelScope.launch {
            _questBObj.postValue(questObj)
        }
    }

    private val _questCObj = MutableLiveData<SecurityQuestObj>()
    val questCObj: LiveData<SecurityQuestObj> get() = _questCObj
    fun setQuestCObj(questObj: SecurityQuestObj) {
        viewModelScope.launch {
            _questCObj.postValue(questObj)
        }
    }

    /** saving otp response obj to view model */
    private val _getOtpResponse = MutableLiveData<GetOtpResponse>()
    val getOtpResponse: LiveData<GetOtpResponse> get() = _getOtpResponse
    private fun setOtpObj(otpResponse: GetOtpResponse) {
        viewModelScope.launch {
            _getOtpResponse.postValue(otpResponse)
        }
    }

    /** save authState? ( true, false ) in db for some condition */
    private suspend fun saveAuthState(): Boolean {
        viewModelScope.launch {
            appRepo.storeAuthState(true)
        }
        return true
    }


    /** save user data to db when login or register SUCCESS*/
    private suspend fun saveUserEntity(user: ProfileInfo): Boolean {
        viewModelScope.launch {
            userRepo.saveUserToDb(
                userEntity = user.toEntity()
            )
        }
        return true
    }


    /** request forget pwd question for associated user */
    private val _forgetPwdQuestChannel = Channel<RemoteEvent<GetConfirmedQuestionsResponse>>()
    val forgetPwdQuestEvent get() = _forgetPwdQuestChannel.receiveAsFlow()
    fun getConfirmedQuestions() {
        viewModelScope.launch {
            authRepo.getConfirmedQuestions(
                countryId = selectedCountry.value?.id ?: -1,
                mobile = selectedPhone.value ?: ""
            ).collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {}
                    is RemoteEvent.LoadingEvent -> {
                        setLoadingState(true)
                    }
                    is RemoteEvent.SuccessEvent -> {
                        _forgetPwdQuestChannel.send(RemoteEvent.SuccessEvent(it.data!!))
                    }
                }

            }
        }
    }

    /** (post)checking user profile info have two factor or not in Forget Pwd */
    private val _checkAccountStatus = Channel<RemoteEvent<CheckAccountByMobileResponse>>()
    val checkAccountStatus get() = _checkAccountStatus.receiveAsFlow()

    fun checkAccountStatus() {
        viewModelScope.launch {
            authRepo.checkAccount(
                countryId = selectedCountry.value?.id ?: -1,
                mobile = selectedPhone.value ?: ""
            ).collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        setLoadingState(false)
                        _checkAccountStatus.send(
                            RemoteEvent.ErrorEvent(
                                errorMessage = it.data!!.error ?: "Error"
                            )
                        )
                    }
                    is RemoteEvent.LoadingEvent -> {
                        setLoadingState(true)
                    }
                    is RemoteEvent.SuccessEvent -> {
                        _checkAccountStatus.send(RemoteEvent.SuccessEvent(it.data!!))
                        if (it.data.status == Constant.STATUS_SUCCESS) {
                            if (it.data.data!!.headUrl.isNullOrEmpty()) {
                                setSelectedAccountUrl(Constant.SERVER_IMAGE_URL)
                            } else {
                                setSelectedAccountUrl(it.data.data.headUrl)
                            }
                            setSelectedAccountName(it.data.data.name)
                        }
                        setLoadingState(false)
                    }
                }
            }
        }
    }

    /** request forget pwd change confirmation for associated user */
    private val _forgetPwdChangeChannel = Channel<RemoteEvent<VerifiedResponse>>()
    val forgetPwdChangeEvent get() = _forgetPwdChangeChannel.receiveAsFlow()

    fun resetPassword(
        newPassword: String,
        confirmPassword: String
    ) {
        viewModelScope.launch {
            authRepo.resetPassword(
                countryId = selectedCountry.value?.id ?: -1,
                mobile = selectedPhone.value ?: "",
                newPassword = newPassword,
                confirmPassword = confirmPassword
            ).collect {
                when (it) {
                    is RemoteEvent.ErrorEvent -> {
                        setLoadingState(false)
                        _forgetPwdChangeChannel.send(RemoteEvent.ErrorEvent(errorMessage = it.data?.error!!))
                    }
                    is RemoteEvent.LoadingEvent -> {
                        setLoadingState(true)
                    }
                    is RemoteEvent.SuccessEvent -> {
                        setLoadingState(false)
                        _forgetPwdChangeChannel.send(RemoteEvent.SuccessEvent(it.data!!))
                    }
                }
            }
        }
    }


    private val _signUpOtpTimber = MutableStateFlow(0)
    val signUpOtpTimber: StateFlow<Int> get() = _signUpOtpTimber

    fun setSignUpOtpTimberValue(value: Int) {
        viewModelScope.launch {
            _signUpOtpTimber.emit(value)
        }
    }

    fun setSignUpOtpTimber(minute: Int) {
        val counter: Long = minute.toLong() * 60000
        val countInterval: Long = 1000
        fun countDown() {
            timerSignUp = object : CountDownTimer(counter, countInterval) {
                override fun onTick(millisUntilFinished: Long) {
                    _signUpOtpTimber.value = (millisUntilFinished / countInterval).toInt()
                }

                override fun onFinish() {
                    _signUpOtpTimber.value = 0
                }
            }.start()
        }

        if (timerSignUp != null) {
            timerSignUp!!.cancel()
            _signUpOtpTimber.value = 0
            countDown()
        } else {
            countDown()
        }

    }

    /** forget pwd otp timer phone number checking */
    private val _forgotPwdOtpTimber = MutableStateFlow(0)
    val forgotPwdOtpTimber: StateFlow<Int> get() = _forgotPwdOtpTimber
    fun setForgotPwdOtpTimber(minute: Int) {

        val counter: Long = minute.toLong() * 60000
        val countInterval: Long = 1000
        fun countDown() {
            timerSignUp = object : CountDownTimer(counter, countInterval) {
                override fun onTick(millisUntilFinished: Long) {
                    _forgotPwdOtpTimber.value = (millisUntilFinished / countInterval).toInt()
                }

                override fun onFinish() {
                    _forgotPwdOtpTimber.value = 0
                }
            }.start()
        }
        if (timerSignUp != null) {
            timerSignUp!!.cancel()
            _forgotPwdOtpTimber.value = 0
            countDown()
        } else {
            countDown()
        }

    }
}