package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.initmember.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.galaxytechno.chat.R
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component.VerticalSpacerTiny
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.newmessage.components.CreateRoomSearchWidget

@Composable
fun InitMemberTopBar(
    modifier: Modifier = Modifier,
    title: String,
    navIcon: Painter,
    menuIcon: Painter,
    onNavIconClicked: () -> Unit,
    onMenuIconClicked :  ()-> Unit,
    searchQuery: String,
    onTextChanged: (String) -> Unit,
    onClearIconClicked: () -> Unit,
    onSearchCommitted: (String) -> Unit,
) {
    Surface {
        Column(modifier = modifier.fillMaxWidth()) {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavIconClicked
                    ) {
                        Icon(
                            painter = navIcon,
                            contentDescription = "toolbar icon",
                            tint = MaterialTheme.colors.onSurface.copy(0.8f)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onMenuIconClicked
                    ) {
                        Icon(
                            painter = menuIcon,
                            contentDescription = "menu icon",
                            tint = MaterialTheme.colors.onSurface.copy(0.8f)
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.onSurface,
                elevation = 0.dp,
            )
            CreateRoomSearchWidget(
                text = searchQuery,
                onTextChanged = onTextChanged,
                onSearchClicked = onSearchCommitted,
                onClearClicked = onClearIconClicked,
                searchPlaceholder = stringResource(id = R.string.search_to_create_friend)
            )
            VerticalSpacerTiny()
            Divider()
        }
    }

}