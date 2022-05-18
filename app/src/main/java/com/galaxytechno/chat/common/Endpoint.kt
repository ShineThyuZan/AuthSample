package com.galaxytechno.chat.common


object Endpoint {

    //mock
    const val API_KEY = "cdbea55de27a909b4aaa2cfc02eabb75"
    const val IMAGE_URL = "https://image.tmdb.org/t/p/original/"

    //API URLs
//    const val API_HOST = "http://192.168.100.241:8080/"
    const val API_HOST = "http://154.39.157.119/"
    const val SOCKET_HOST = "http://192.168.100.207:8888"

    private const val TOKEN = "token/"
    private const val SOCIAL_APP = "socialapp/"
    private const val PROFILE_PATH = "profile/"
    private const val FRIEND_PATH = "friend/"
    private const val CONTACT_PATH = "contact/"
    private const val REMOVE_PATH = "remove/"
    private const val MASTER_DATA = "master/"
    private const val REGISTER_PATH = "register/"
    private const val LOGIN_PATH = "login/"
    private const val QUESTIONS = "security-question/"
    private const val HOST = API_HOST + SOCIAL_APP
    const val AUTHORIZATION = "Authorization"
    private const val FRIEND = "friend/"

    //Master
    const val MASTER_QUESTIONS = HOST + MASTER_DATA + "getSecurityQuesList"
    const val MASTER_LANGUAGES = HOST + MASTER_DATA + "languages"
    const val MASTER_COUNTRIES = HOST + MASTER_DATA + "getCountryList"

    //Auth
    const val PROFILE_CHECK_BY_MOBILE = HOST + PROFILE_PATH + "checkByMobile"
    const val PROFILE_INFO_BY_USER_ID = HOST + PROFILE_PATH + "info"
    const val PROFILE_INFO_BY_FRIEND_ID = HOST + PROFILE_PATH + "info"
    const val REQUEST_CONFIRM_QUESTIONS = HOST + QUESTIONS + "confirm-lists"
    const val VERIFY_CONFIRM_QUESTIONS = HOST + QUESTIONS + "confirmation"
    const val RESET_PASSWORD = HOST + PROFILE_PATH + "forgotPasswordChange"
    const val REQUEST_OTP = HOST + REGISTER_PATH + "getOTP"
    const val VALIDATE_OTP = HOST + REGISTER_PATH + "validateOTP"
    const val LOGIN = HOST + LOGIN_PATH + "mobileLogin"
    const val SIGNUP = HOST + REGISTER_PATH + "userRegister"
    const val PROFILE_INFO = HOST + PROFILE_PATH + "info/{userId}"
    const val CREATE_CHAT_GROUP_MEMBER = HOST + "friendGroup/createGroup"
    const val LOGOUT = HOST + LOGIN_PATH + "logout"
    const val DO_TWO_FACTOR = HOST + PROFILE_PATH + "two-factor"
    const val FRI_CONTACT_CHECK = HOST + "friend/contacts/check"

    //Friend
    const val ADD_FRIEND_REQUEST = HOST + FRIEND_PATH + "request/send"
    const val UNFRIEND_REQUEST = HOST + FRIEND_PATH + "unfriend"
    const val BLOCK_REQUEST = HOST + FRIEND + "block"
    const val BLOCK_LIST = HOST + FRIEND + "block/lists"
    const val GET_FRI_LIST = HOST + FRIEND + "lists"
    const val GET_FRI_SEARCH = HOST + PROFILE_PATH + "search"
    const val GET_FRI_REQ_LIST = HOST + FRIEND + "request/lists"
    const val CONFIRM_FRIEND_REQUEST = HOST + FRIEND + "request/response"
    const val CANCEL_REQUEST_BY_REQUESTER = HOST + FRIEND+ "request/cancel"
    const val SEARCH_USER = HOST+ PROFILE_PATH + "search"


    const val REFRESH_TOKEN = "refresh"

    //todo : popo's test api
    const val MASTER_COUNTRY_LIST = API_HOST + SOCIAL_APP + "master/getCountryList"
}