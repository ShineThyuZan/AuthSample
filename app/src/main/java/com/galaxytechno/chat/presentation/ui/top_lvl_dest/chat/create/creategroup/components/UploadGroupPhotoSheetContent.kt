package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.creategroup.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.galaxytechno.chat.R
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component.UploadImageSheetItem
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component.VerticalSpacerMedium

@Composable
fun UploadGroupPhotoSheetContent(
    modifier: Modifier = Modifier,
    onCameraClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        UploadImageSheetItem(
            painter = painterResource(id = R.drawable.ic_camera),
            text = stringResource(id = R.string.camera),
            onItemClicked = onCameraClicked
        )
        Divider()
        UploadImageSheetItem(
            painter = painterResource(id = R.drawable.ic_gallery),
            text = stringResource(id = R.string.gallery),
            onItemClicked = onGalleryClicked
        )
        VerticalSpacerMedium()
    }
}

@Composable
@Preview
private fun UploadImageSheetContentPreview() {
    Surface {
        UploadGroupPhotoSheetContent(
            onCameraClicked = { },
            onGalleryClicked = { },
        )
    }
}