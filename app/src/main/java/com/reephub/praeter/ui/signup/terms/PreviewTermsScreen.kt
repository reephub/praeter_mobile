package com.reephub.praeter.ui.signup.terms

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.reephub.praeter.R
import com.reephub.praeter.core.compose.annotation.DevicePreviews
import com.reephub.praeter.core.compose.theme.PraeterTheme
import com.reephub.praeter.ui.signup.SignUpViewModel

/////////////////////////////////////
//
// COMPOSE
//
/////////////////////////////////////
@Composable
fun TermsContent(viewModel: SignUpViewModel) {
    val lazyListState = rememberLazyListState()

    PraeterTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                state = lazyListState
            ) {
                item {
                    Text(
                        modifier = Modifier.fillMaxSize(),
                        text = stringResource(id = R.string.lorem_ipsum),
                        color = if (isSystemInDarkTheme()) Color.White else Color.Black
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = viewModel.isTermsChecked,
                    onCheckedChange = { viewModel.updateIsTermsChecked(it) })
                Text(
                    text = stringResource(id = R.string.accept_agreement_license),
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black
                )
            }
        }
    }
}


/////////////////////////////////////
//
// PREVIEWS
//
/////////////////////////////////////
@DevicePreviews
@Composable
private fun Preview() {
    val viewModel: SignUpViewModel = hiltViewModel()
    PraeterTheme {
        TermsContent(viewModel)
    }
}