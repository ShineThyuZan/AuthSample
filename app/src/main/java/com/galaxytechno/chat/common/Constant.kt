package com.galaxytechno.chat.common

object Constant {
    const val DB_NAME = "chat_db"
    const val DS_NAME = "chat_ds"
    const val USER_TABLE = "chat_user_table"
    const val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.+)(\\.)(.{1,})"

    const val EMAIL_REGEX_ADDRESS = ("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")

    const val STATUS_FAIL = "FAIL"
    const val STATUS_SUCCESS = "SUCCESS"

    const val CHAT_MINE = 0
    const val CHAT_PARTNER = 1
    const val GROUP_MEMBER_LENGTH = 50

    const val NOT_FRIEND_STATUS = 1
    const val YOU_REQUESTED_TO_OTHER_STATUS = 2
    const val OTHER_REQUEST_TO_YOU_STATUS = 3
    const val ALREADY_FRIEND = 4
    const val YOU_BLOCK_TO_USER = 5
    const val USER_BLOCK_TO_YOU = 6
    const val BLOCK_TO_EACH_OTHER = 7
    const val PAGE_SIZE = 30
    const val INITIAL_PAGE = 0

    const val SERVER_IMAGE_URL =
        "https://galaxyshopbucket.s3.ap-southeast-1.amazonaws.com/PROFILE_IMAGES/deedabfc-8955-41ba-8863-539109a8c281.png"

}