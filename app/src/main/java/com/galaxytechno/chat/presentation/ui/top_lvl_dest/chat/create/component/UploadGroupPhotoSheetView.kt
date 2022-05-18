package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.spacing
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.titleMedium

@Composable
fun UploadGroupPhotoSheetView(
    modifier: Modifier = Modifier,
    title: String,
    onCameraClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
) {
    Column {
        AuthHeader(
            modifier = modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium),
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        Divider()
        UploadGroupPhotoSheetContent(
            onCameraClicked = onCameraClicked,
            onGalleryClicked = onGalleryClicked,
        )

    }
}