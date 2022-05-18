package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.galaxytechno.chat.R
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.spacing
import com.google.accompanist.coil.rememberCoilPainter

@Composable
fun ProfileEditAvatarText(
    modifier: Modifier = Modifier,
    avatarBitmap: Bitmap? = null,
    size: Dp,
    onEditClicked: () -> Unit,
) {
    Box(
        modifier = modifier
            .size(size = size)
            .clip(CircleShape)
            .background(
                color = MaterialTheme.colors.surface
            ),
        contentAlignment = Alignment.Center
    ) {

        if (avatarBitmap == null) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.spacing.extraSmall)
                    .clip(CircleShape)
                    .background(
                        color = MaterialTheme.colors.primary.copy(alpha = 0.5f)
                    )
            )
        } else {
            Image(
                modifier = modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.spacing.extraSmall)
                    .clip(CircleShape),
                bitmap = avatarBitmap.asImageBitmap(),
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                alpha = 0.5f
            )
        }
        IconButton(onClick = onEditClicked) {
            Icon(
                painter = painterResource(id = R.drawable.ic_icon_camera),
                contentDescription = "Edit Avatar"
            )
        }
    }
}

@Composable
fun ProfileEditAvatar(
    modifier: Modifier = Modifier,
    avatarUrl: String,
    avatarBitmap: Bitmap? = null,
    size: Dp,
    onEditClicked: () -> Unit
) {
    val image = rememberCoilPainter(
        request = avatarUrl,
        fadeIn = true
    )
    Box(
        modifier = modifier
            .size(size = size)
            .clip(CircleShape)
            .background(color = MaterialTheme.colors.surface),
        contentAlignment = Alignment.Center
    ) {

        if (avatarBitmap == null) {
            Image(
                modifier = modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.spacing.tiny)
                    .clip(CircleShape),
                painter = image,
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                alpha = 0.5f
            )
        } else {
            Image(
                modifier = modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.spacing.tiny)
                    .clip(CircleShape),
                bitmap = avatarBitmap.asImageBitmap(),
                contentDescription = "upload avatar",
                contentScale = ContentScale.Crop,
                alpha = 0.5f
            )
        }

        IconButton(onClick = onEditClicked) {
            Icon(
                painter = painterResource(id = R.drawable.ic_camera),
                contentDescription = "Edit avatar"
            )
        }
    }
}
