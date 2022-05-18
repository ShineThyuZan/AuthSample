package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.newmessage.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.galaxytechno.chat.R
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.bodyMedium
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.sizing
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.spacing
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component.HorizontalSpacerExtraSmall

@Composable
fun CreateRoomSearchWidget(
    modifier: Modifier = Modifier,
    text: String,
    onTextChanged: (String) -> Unit,
    onSearchClicked: (String) -> Unit,
    onClearClicked: () -> Unit,
    searchPlaceholder: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(MaterialTheme.sizing.textFieldHeight)
            .padding(
                start = MaterialTheme.spacing.medium,
                end = MaterialTheme.spacing.medium,
                top = MaterialTheme.spacing.extraSmall,
                bottom = MaterialTheme.spacing.extraSmall,
            )
            .clip(CircleShape)
            .background(
                color = MaterialTheme.colors.background
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(
                    start = MaterialTheme.spacing.medium,
                    end = MaterialTheme.spacing.medium
                )
                .align(Alignment.CenterVertically),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = text,
                onValueChange = onTextChanged,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colors.onSurface,
                ),
                decorationBox = {
                    if (text.isEmpty()) {
                        Row(
                            modifier = modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = "search",
                                tint = MaterialTheme.colors.onSurface.copy(0.5f)
                            )
                            HorizontalSpacerExtraSmall()
                            Text(
                                text = searchPlaceholder,
                                color = MaterialTheme.colors.onSurface.copy(0.7f),
                            )
                        }
                    }

                    it()
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearchClicked(text)
                    }
                ),
                cursorBrush = SolidColor(
                    value = MaterialTheme.colors.primary,
                ),
            )
        }

        if (text.isNotEmpty()) {
            IconButton(onClick = onClearClicked) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = "Close Text",
                    tint = MaterialTheme.colors.onSurface.copy(0.5f)

                )
            }
        }
    }
}