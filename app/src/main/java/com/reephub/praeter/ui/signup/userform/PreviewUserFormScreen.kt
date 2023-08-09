package com.reephub.praeter.ui.signup.userform

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
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
fun UserFormContent(navController: NavHostController, viewModel: SignUpViewModel) {
    val context = LocalContext.current
    val lazyListState = rememberLazyListState()

    PraeterTheme {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState
        ) {
            item {
                OutlinedTextField(
                    value = viewModel.lastname,
                    onValueChange = { viewModel.updateLastname(it) },
                    label = { Text(text = stringResource(id = R.string.hint_last_name)) })
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
    val navController = rememberNavController()
    val viewModel: SignUpViewModel = hiltViewModel()

    PraeterTheme {
        UserFormContent(navController = navController, viewModel = viewModel)
    }
}