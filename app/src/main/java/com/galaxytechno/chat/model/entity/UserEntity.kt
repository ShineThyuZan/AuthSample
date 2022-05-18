package com.galaxytechno.chat.model.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.galaxytechno.chat.common.Constant
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = Constant.USER_TABLE)
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    var userId: Long = -1,
    var name: String = "",
    var headUrl: String = "",
    var coverImgUrl: String = "",
    var bio: String = "",
    var birthDate: String = "",
    var gender: Int = -1,
    var countryId: Int = -1,
    var mobile: String = "",
    var email: String = "",
    var accountId: String = "",
    var locale: Int = -1,
    var isTwoFactor: Boolean = false,
    var isProfileLock: Boolean = false,
    var totalFriends: Int = 0,
    var friendStatus: Int = -1,
    var isBeingBlock: Boolean = false,
    var isBlock: Boolean = false
) : Parcelable
