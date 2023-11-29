package dev.awd.injaaz.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.awd.injaaz.R
import dev.awd.injaaz.ui.theme.InjaazTheme
import dev.awd.injaaz.ui.theme.pilat_extended
import dev.awd.injaaz.utils.capitalize

@Composable
fun AppLogo(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.wrapContentSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = stringResource(id = R.string.app_name),
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 8.dp)
        )
        Text(
            text = stringResource(id = R.string.app_name).capitalize(),
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.26.sp,
                lineHeight = 14.sp,
                fontFamily = pilat_extended,
                fontWeight = FontWeight(600),
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(top = 12.dp)
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

