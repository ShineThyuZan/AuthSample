package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.theme.*

@Composable
fun AuthHeader(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle,
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = text,
        style = style,
        textAlign = TextAlign.Center,
    )
}

@Composable
@Preview
fun AuthDisplayLargePreview() {
    Surface {
        AuthHeader(
            text = "Display Large",
            style = MaterialTheme.typography.displayLarge
        )
    }
}

@Composable
@Preview
fun AuthDisplayMediumPreview() {
    Surface {
        AuthHeader(
            text = "Display Medium",
            style = MaterialTheme.typography.displayMedium
        )
    }
}

@Composable
@Preview
fun AuthDisplaySmallPreview() {
    Surface {
        AuthHeader(
            text = "Display Small",
            style = MaterialTheme.typography.displaySmall
        )
    }
}

@Composable
@Preview
fun AuthHeadlineLargePreview() {
    Surface {
        AuthHeader(
            text = "Headline Large",
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

@Composable
@Preview
fun AuthHeadlineMediumPreview() {
    Surface {
        AuthHeader(
            text = "Headline Medium",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
@Preview
fun AuthHeadlineSmallPreview() {
    Surface {
        AuthHeader(
            text = "Headline Small",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
@Preview
fun AuthTitleLargePreview() {
    Surface {
        AuthHeader(
            text = "Title Large",
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
@Preview
fun AuthTitleMediumPreview() {
    Surface {
        AuthHeader(
            text = "Title Medium",
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
@Preview
fun AuthTitleSmallPreview() {
    Surface {
        AuthHeader(
            text = "Title Small",
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
@Preview
fun AuthLabelLargePreview() {
    Surface {
        AuthHeader(
            text = "Label Large",
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
@Preview
fun AuthLabelMediumPreview() {
    Surface {
        AuthHeader(
            text = "Label Medium",
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
@Preview
fun AuthLabelSmallPreview() {
    Surface {
        AuthHeader(
            text = "Label Small",
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
@Preview
fun AuthBodyLargePreview() {
    Surface {
        AuthHeader(
            text = "Body Large",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
@Preview
fun AuthBodyMediumPreview() {
    Surface {
        AuthHeader(
            text = "Body Medium",
            style =MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
@Preview
fun AuthBodySmallPreview() {
    Surface {
        AuthHeader(
            text = "Body Small",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

