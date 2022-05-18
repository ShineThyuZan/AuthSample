package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.creategroup.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActionScope
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
import androidx.compose.ui.unit.dp
import com.galaxytechno.chat.R
import com.galaxytechno.chat.common.Constant
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component.VisibilityAnimator
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.bodyMedium
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.labelSmall
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.sizing
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.spacing

@Composable
fun GroupNameTextField(
    modifier: Modifier = Modifier,
    placeholder: String,
    value: String,
    onValueChanged: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    onValueCleared: () -> Unit,
    isError: Boolean,
    errorMessage: String,
    keyboardAction: (KeyboardActionScope) -> Unit = {},
    groupNameCountLimit: Int = Constant.GROUP_MEMBER_LENGTH,
    groupNameCountInput: Int = 0,
) {

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(MaterialTheme.sizing.textFieldHeight)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = if (isError) MaterialTheme.colors.error else
                        MaterialTheme.colors.onSurface.copy(0.5f),
                    shape = CircleShape
                )
                .background(
                    color = MaterialTheme.colors.surface
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                modifier = modifier
                    .wrapContentHeight(align = Alignment.CenterVertically)
                    .padding(start = 16.dp)
                    .weight(1f),
                value = value,
                onValueChange = {
                    if (it.length <= groupNameCountLimit) {
                        onValueChanged(it)
                    }
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colors.onSurface,
                ),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = MaterialTheme.colors.onSurface.copy(0.7f),
                        )
                    }
                    innerTextField()
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = imeAction
                ),
                keyboardActions = KeyboardActions(
                    onDone = keyboardAction
                ),
                cursorBrush = SolidColor(
                    value = MaterialTheme.colors.primary
                )
            )
            if (value.isNotEmpty()) {
                IconButton(onClick = onValueCleared) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "Close Text",
                        tint = MaterialTheme.colors.onSurface.copy(0.5f)

                    )
                }
            }
        }

        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                VisibilityAnimator(
                    isVisible = isError,
                    errorMessage = errorMessage,
                )
            }
            Text(
                modifier = modifier.padding(
                    start = MaterialTheme.spacing.medium,
                    end = MaterialTheme.spacing.regular
                ),
                text = stringResource(
                    id = R.string.group_name_count,
                    groupNameCountInput,
                    groupNameCountLimit
                ),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
            )
        }
    }
}


@Composable
@Preview
private fun PlaceholderPreview() {
    Surface {
        GroupNameTextField(
            placeholder = stringResource(id = R.string.group_name_placeholder),
            value = "",
            onValueChanged = {},
            onValueCleared = { },
            isError = false,
            errorMessage = ""
        )
    }
}

@Composable
@Preview
private fun ValuePreview() {
    Surface {
        GroupNameTextField(
            placeholder = stringResource(id = R.string.group_name_placeholder),
            value = "R Pay 2",
            onValueChanged = {},
            onValueCleared = { },
            isError = false,
            errorMessage = ""
        )
    }
}

@Composable
@Preview
fun ChatTextFieldErrorPreview() {
    Surface {
        GroupNameTextField(
            placeholder = stringResource(id = R.string.group_name_placeholder),
            value = "",
            onValueChanged = {},
            onValueCleared = { },
            isError = true,
            errorMessage = "Error message"
        )
    }
}