package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.creategroup

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.content.FileProvider
import com.galaxytechno.chat.BuildConfig
import com.galaxytechno.chat.R
import com.galaxytechno.chat.common.Constant
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.CreateRoomViewModel
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component.UploadGroupPhotoSheetView
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.creategroup.components.CreateGroupContent
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.creategroup.components.CreateGroupTopBar
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.creategroup.udf.CreateGroupAction
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.creategroup.udf.CreateGroupEvent
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.spacing
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun CreateGroupScreen(
    vm: CreateRoomViewModel,
    goBack: () -> Unit,
) {
    CreateGroupView(
        vm = vm,
        goBack = goBack
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateGroupView(
    vm: CreateRoomViewModel,
    goBack: () -> Unit,
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val state = vm.state.value
    val createGroupState = state.createGroupState
    val selectedFriends = state.selectedFriends.value

    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    var groupPhotoUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        it?.let { uri ->

            val bitmap = getBitmapFromUri(
                uri = uri,
                context = context
            )

            vm.onActionCreateGroup(
                CreateGroupAction.ChangePhoto(
                    image = getBitmapFromUri(
                        uri = uri,
                        context = context
                    )
                )
            )
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) {

        if (it) {

            groupPhotoUri?.let { uri ->
                val bitmap = getBitmapFromUri(
                    uri = uri,
                    context = context
                )

                vm.onActionCreateGroup(
                    CreateGroupAction.ChangePhoto(
                        image = getBitmapFromUri(
                            uri = uri,
                            context = context
                        )
                    )
                )
            }
        }
    }

    LaunchedEffect(key1 = true) {
        vm.createGroupEvent.collectLatest {
            when (it) {
                CreateGroupEvent.CameraPicker -> {
                    getTmpFileUri(
                        context = context
                    ).let { uri ->
                        groupPhotoUri = uri
                        cameraLauncher.launch(groupPhotoUri)
                    }
                }
                CreateGroupEvent.GalleryPicker -> {
                    galleryLauncher.launch("image/*")
                }
                CreateGroupEvent.NavigateToConversation -> {
                    //todo go to conversation with args
                }
                CreateGroupEvent.Popup -> {
                    goBack()
                }
                CreateGroupEvent.ShowImagePickerSheet -> {
                    scope.launch {
                        modalBottomSheetState.show()
                    }
                }
                CreateGroupEvent.HideImagePickerSheet -> {
                    scope.launch {
                        modalBottomSheetState.hide()
                    }
                }
                is CreateGroupEvent.ShowSnack -> {
                    scaffoldState.snackbarHostState.showSnackbar(message = it.message)
                }
            }
        }
    }

    ModalBottomSheetLayout(
        sheetContent = {
            UploadGroupPhotoSheetView(
                title = stringResource(id = R.string.upload_image),
                onCameraClicked = {
                    vm.onActionCreateGroup(
                        CreateGroupAction.UploadFromCamera
                    )
                    scope.launch {
                        modalBottomSheetState.hide()
                    }
                },
                onGalleryClicked = {
                    vm.onActionCreateGroup(
                        CreateGroupAction.UploadFromGallery
                    )
                    scope.launch {
                        modalBottomSheetState.hide()
                    }
                }
            )
        },
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(
            topStart = MaterialTheme.spacing.medium,
            topEnd = MaterialTheme.spacing.medium,
        ),
        sheetBackgroundColor = MaterialTheme.colors.surface,
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {

                CreateGroupTopBar(
                    title = stringResource(id = R.string.new_group),
                    navIcon = painterResource(id = R.drawable.ic_close),
                    menuIcon = painterResource(id = R.drawable.ic_mark),
                    onNavIconClicked = {
                        vm.onActionCreateGroup(
                            CreateGroupAction.ClickBack
                        )
                    },
                    onMenuIconClicked = {
                        vm.onActionCreateGroup(
                            CreateGroupAction.ClickCreate
                        )
                    })
            },
            content = {
                CreateGroupContent(
                    groupPhoto = createGroupState.groupPhoto,
                    onEditClicked = {
                        vm.onActionCreateGroup(
                            CreateGroupAction.ClickUpload
                        )
                    },
                    groupNamePlaceholder = stringResource(id = R.string.group_name_placeholder),
                    groupNameValue = createGroupState.groupName,
                    groupNameValueChanged = {
                        vm.onActionCreateGroup(
                            CreateGroupAction.ChangeName(
                                name = it
                            )
                        )
                    },
                    groupNameValueCleared = {
                        vm.onActionCreateGroup(
                            CreateGroupAction.ChangeName(
                                name = ""
                            )
                        )
                    },
                    isEmptyGroupName = false,
                    groupNameErrorMessage = stringResource(id = R.string.group_name_error),
                    totalMember = selectedFriends.count(),
                    memberLimit = Constant.GROUP_MEMBER_LENGTH,
                    selectedFriends = selectedFriends,
                    onItemDeleted = {
                        vm.deleteSelectedItem(item = it)
                    },

                    )
            }
        )
    }
}

private fun getBitmapFromUri(
    uri: Uri,
    context: Context
): Bitmap {

    return if (Build.VERSION.SDK_INT < 28) {
        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    } else {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        ImageDecoder.decodeBitmap(source)
    }
}

private fun getTmpFileUri(context: Context): Uri {
    val tmpFile =
        File.createTempFile("tmp_image_file", ".png", context.cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

    return FileProvider.getUriForFile(
        context,
        "${BuildConfig.APPLICATION_ID}.provider",
        tmpFile
    )
}