package com.reephub.praeter.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reephub.praeter.R
import com.reephub.praeter.core.compose.annotation.DevicePreviews
import com.reephub.praeter.core.compose.theme.PraeterTheme
import com.reephub.praeter.data.local.model.LoginUiState


/////////////////////////////////////
//
// COMPOSE
//
/////////////////////////////////////
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Username(viewModel: LoginViewModel, focusRequester: FocusRequester) {
    val status by viewModel.loginUiState.collectAsStateWithLifecycle()

    PraeterTheme {
        Column {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(size = 35.dp)
                    )
                    .focusRequester(focusRequester),
                value = viewModel.username,
                onValueChange = { viewModel.updateUsername(it) },
                placeholder = { androidx.compose.material3.Text(text = "Login") },
                maxLines = 1,
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "user icon"
                    )
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.textFieldColors(
                    // containerColor = if (!focus.value) Color.DarkGray else lightBlue,
                    // textColor = if (!focus.value) Color.Gray else Color.White,
                    cursorColor = Color.Blue,
                    focusedIndicatorColor = Color.Transparent, //hide the indicator
                    unfocusedIndicatorColor = Color.Transparent
                ),
                enabled = status is LoginUiState.None || status is LoginUiState.Success
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Password(viewModel: LoginViewModel, focusRequester: FocusRequester) {
    val status by viewModel.loginUiState.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current

    PraeterTheme {
        Column {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = viewModel.password,
                onValueChange = { viewModel.updatePassword(it) },
                placeholder = { Text(text = "Password (6+ characters") },
                maxLines = 1,
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "password icon lock"
                    )
                },
                trailingIcon = {
                    IconButton(onClick = {
                        viewModel.updateIsPasswordVisible(!viewModel.isPasswordVisible)
                    }) {
                        Icon(
                            imageVector = if (!viewModel.isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (viewModel.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                colors = TextFieldDefaults.textFieldColors(
                    // containerColor = if (!focus.value) Color.DarkGray else lightBlue,
                    // textColor = if (!focus.value) Color.Gray else Color.White,
                    cursorColor = Color.Blue,
                    focusedIndicatorColor = Color.Transparent, //hide the indicator
                    unfocusedIndicatorColor = Color.Transparent
                ),
                enabled = status is LoginUiState.None || status is LoginUiState.Success
            )
        }
    }
}


@Composable
fun SubmitButton(viewModel: LoginViewModel) = PraeterTheme {
    val status by viewModel.loginUiState.collectAsStateWithLifecycle()

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        onClick = { viewModel.login() },
        enabled = status is LoginUiState.None || status is LoginUiState.Success
    ) {
        Text(text = stringResource(id = R.string.btn_sign_in))
    }
}


@Composable
fun Form(viewModel: LoginViewModel) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    PraeterTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
        ) {
            Username(viewModel = viewModel, focusRequester = focusRequester)
            Password(viewModel = viewModel, focusRequester = focusRequester)
            SubmitButton(viewModel = viewModel)
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
    val viewModel: LoginViewModel = hiltViewModel()

    PraeterTheme {
        Form(viewModel = viewModel)
    }
}