package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.galaxytechno.chat.R
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.bodyMedium
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.spacing

@Composable
fun LoadingView(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
@Preview(showBackground = true)
private fun LoadingViewPreview() {
    LoadingView()
}

@Composable
fun LoadingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.medium),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(MaterialTheme.spacing.regular)
        )
    }

}

@Composable
@Preview(showBackground = true)
private fun LoadingItemPreview() {
    LoadingItem()
}

@Composable
fun ErrorItem(
    message: String,
    modifier: Modifier = Modifier,
    onClickRetry: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(MaterialTheme.spacing.medium)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
        )
        VerticalSpacerSmall()
        OutlinedButton(onClick = onClickRetry) {
            Text(text = stringResource(id = R.string.retry))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ErrorItemPreview() {
    ErrorItem(message = "Error is happened") {

    }
}
