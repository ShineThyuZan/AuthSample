package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.initmember.components.shimmer

import android.os.Parcelable
import com.galaxytechno.chat.model.vos.AppUserVo
import com.galaxytechno.chat.model.vos.FriListVos
import kotlinx.parcelize.Parcelize

sealed class FriendsUiModel : Parcelable {
    @Parcelize
    data class Item(val item: FriListVos) : FriendsUiModel(), Parcelable

    @Parcelize
    data class Header(val header: String) : FriendsUiModel(), Parcelable
}

sealed class AppUserUiModel : Parcelable {
    @Parcelize
    data class Item(val item: AppUserVo) : AppUserUiModel(), Parcelable

    @Parcelize
    data class Header(val header: String) : AppUserUiModel(), Parcelable
}



