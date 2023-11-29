package dev.awd.injaaz.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.awd.injaaz.R
import dev.awd.injaaz.presentation.components.AppLogo
import dev.awd.injaaz.ui.theme.InjaazTheme

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onEmailButtonClick: () -> Unit,
    onGoogleButtonClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        AppLogo(modifier.align(Alignment.Start))

        Image(
            painter = painterResource(id = R.drawable.welcome_image),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.White)) {
                    append("Manage\nYour Tasks\nWith ")
                }
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append(stringResource(id = R.string.app_name))
                }
            },
            letterSpacing = 4.sp,
            fontSize = 58.sp,
            lineHeight = 62.sp,
            fontWeight = FontWeight(600),
            modifier = Modifier.align(Alignment.Start)
        )
        Button(
            onClick = onEmailButtonClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(size = 6.dp)
        ) {
            Text(text = "Continue With Email", color = Color.Black, fontWeight = FontWeight.Bold)
        }
        Button(
            onClick = onGoogleButtonClick,
            border = BorderStroke(2.dp, Color.White),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(size = 6.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = "Google",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = "Google")
            }
        }
    }

}

@Preview(showBackground = false)
@Composable
private fun WelcomePreview() {
    InjaazTheme {
        WelcomeScreen(onEmailButtonClick = {}, onGoogleButtonClick = {})
    }
}