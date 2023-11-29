package dev.awd.injaaz.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.awd.injaaz.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InjaazSearchBar(
    modifier: Modifier = Modifier,
    hint: String,
    onValueChanged: (String) -> Unit,
    onFilter: () -> Unit
) {
    var text by remember {
        mutableStateOf("")
    }
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        TextField(
            value = text,
            placeholder = { Text(text = hint) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = null,
                    tint = Color(0xFF6F8793)
                )
            },
            onValueChange = {
                text = it
                onValueChanged(it)
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.primary,
                unfocusedPlaceholderColor = Color(0xFF6F8793),
                unfocusedContainerColor = MaterialTheme.colorScheme.onBackground,
                focusedContainerColor = MaterialTheme.colorScheme.onBackground,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 4.dp)
        )
        IconButton(
            onClick = onFilter,
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = Color.Black,
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .padding(4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.filter),
                contentDescription = null,
            )
        }
    }
}
