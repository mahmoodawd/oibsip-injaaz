package dev.awd.injaaz.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import dev.awd.injaaz.R
import dev.awd.injaaz.ui.theme.InjaazTheme
import java.util.Locale

@Composable
fun AppLogo(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = stringResource(
                id = R.string.app_name
            )
        )

        Text(
            text = stringResource(id = R.string.app_name).replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ROOT
                ) else it.toString()
            },
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.26.sp,
                lineHeight = 15.3.sp,
                fontWeight = FontWeight(600),
            )
        )

    }
}

@Preview(showBackground = true)
@Composable
private fun AppLogoPreview() {
    InjaazTheme {
        AppLogo()
    }
}