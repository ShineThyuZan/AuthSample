package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.creategroup.udf

import android.graphics.Bitmap
import com.galaxytechno.chat.model.vos.FriListVos

sealed class CreateGroupAction {
    object ClickUpload : CreateGroupAction()
    data class ChangeName(val name: String) : CreateGroupAction()
    data class ChangePhoto(val image: Bitmap) : CreateGroupAction()
    object UploadFromGallery : CreateGroupAction()
    object UploadFromCamera : CreateGroupAction()

    object ClickBack : CreateGroupAction()
    object ClickCreate : CreateGroupAction()
    data class RemoveItem(val friend: FriListVos) : CreateGroupAction()

}