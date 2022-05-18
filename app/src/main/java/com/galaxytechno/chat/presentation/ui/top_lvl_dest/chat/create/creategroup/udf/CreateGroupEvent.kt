package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.creategroup.udf

sealed class CreateGroupEvent {
    object Popup : CreateGroupEvent()
    object NavigateToConversation : CreateGroupEvent()

    object ShowImagePickerSheet : CreateGroupEvent()
    object HideImagePickerSheet : CreateGroupEvent()

    object CameraPicker : CreateGroupEvent()
    object GalleryPicker : CreateGroupEvent()

    data class ShowSnack(val message: String) : CreateGroupEvent()
}
