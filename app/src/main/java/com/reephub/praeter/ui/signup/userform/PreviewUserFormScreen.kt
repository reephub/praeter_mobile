package com.reephub.praeter.ui.signup.userform

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.reephub.praeter.R
import com.reephub.praeter.core.compose.annotation.DevicePreviews
import com.reephub.praeter.core.compose.theme.PraeterTheme
import com.reephub.praeter.ui.signup.SignUpViewModel
import kotlinx.coroutines.launch


/////////////////////////////////////
//
// COMPOSE
//
/////////////////////////////////////
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun UserFormContent(navController: NavHostController, viewModel: SignUpViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager: FocusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (viewModel.isGenderExpanded)
        Icons.Filled.ArrowDropUp //it requires androidx.compose.material:material-icons-extended
    else
        Icons.Filled.ArrowDropDown

    PraeterTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
            /* .onGloballyPositioned {

             }*/,
            state = lazyListState
        ) {
            item {
                ExposedDropdownMenuBox(
                    modifier = Modifier.focusProperties {
                        this.canFocus = false
                    },
                    expanded = viewModel.isGenderExpanded,
                    onExpandedChange = { viewModel.updateIsGenderExpanded(it) }
                ) {
                    TextField(
                        value = viewModel.gender,
                        onValueChange = { viewModel.updateGender(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusable(false)
                            .onGloballyPositioned { coordinates ->
                                //This value is used to assign to the DropDown the same width
                                textFieldSize = coordinates.size.toSize()
                            }
                            // .focusRequester(focusRequester)
                            .menuAnchor(),
                        label = { Text(stringResource(id = R.string.hint_gender)) },
                        trailingIcon = {
                            Icon(
                                modifier = Modifier.clickable { viewModel.updateIsGenderExpanded(!viewModel.isGenderExpanded) },
                                imageVector = icon,
                                contentDescription = "contentDescription"
                            )
                        },
                        readOnly = true,
                    )

                    ExposedDropdownMenu(
                        expanded = viewModel.isGenderExpanded,
                        onDismissRequest = { viewModel.updateIsGenderExpanded(false) },
                        modifier = Modifier
                            .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                    ) {
                        viewModel.genderOptions.forEachIndexed { _, gender ->

                            DropdownMenuItem(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    viewModel.updateGender(gender)
                                    viewModel.updateIsGenderExpanded(false)
                                    focusManager.clearFocus(true)
                                },
                                text = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = "$gender"
                                        )
                                    }
                                },
                            )
                        }
                    }
                }

                // First Name
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.firstname,
                    onValueChange = { viewModel.updateFirstname(it) },
                    label = { Text(text = stringResource(id = R.string.hint_first_name)) }
                )

                // Last name
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.lastname,
                    onValueChange = { viewModel.updateLastname(it) },
                    label = { Text(text = stringResource(id = R.string.hint_last_name)) }
                )

                // Email
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.email,
                    onValueChange = { viewModel.updateEmail(it) },
                    label = { Text(text = stringResource(id = R.string.hint_email)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                // Password
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bringIntoViewRequester(bringIntoViewRequester)
                        .onFocusEvent { focusState ->
                            if (focusState.isFocused) {
                                coroutineScope.launch {
                                    bringIntoViewRequester.bringIntoView()
                                }
                            }
                        },
                    value = viewModel.password,
                    onValueChange = { viewModel.updatePassword(it) },
                    label = { Text(text = stringResource(id = R.string.hint_password)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                // Confirm Password
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bringIntoViewRequester(bringIntoViewRequester)
                        .onFocusEvent { focusState ->
                            if (focusState.isFocused) {
                                coroutineScope.launch {
                                    bringIntoViewRequester.bringIntoView()
                                }
                            }
                        },
                    value = viewModel.confirmedPassword,
                    onValueChange = { viewModel.updateConfirmedPassword(it) },
                    label = { Text(text = stringResource(id = R.string.hint_confirm_password)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                // Phone Number
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bringIntoViewRequester(bringIntoViewRequester)
                        .onFocusEvent { focusState ->
                            if (focusState.isFocused) {
                                coroutineScope.launch {
                                    bringIntoViewRequester.bringIntoView()
                                }
                            }
                        },
                    value = viewModel.phoneNumber,
                    onValueChange = { viewModel.updatePhoneNumber(it) },
                    label = { Text(text = stringResource(id = R.string.hint_phone_number)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                // First name
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bringIntoViewRequester(bringIntoViewRequester)
                        .onFocusEvent { focusState ->
                            if (focusState.isFocused) {
                                coroutineScope.launch {
                                    bringIntoViewRequester.bringIntoView()
                                }
                            }
                        },
                    value = viewModel.dateOfBirth,
                    onValueChange = { viewModel.updateDateOfBirth(it) },
                    label = { Text(text = stringResource(id = R.string.hint_date_of_birth)) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                )
            }
        }
    }

    LaunchedEffect(viewModel.shouldShowToast) {
        if (viewModel.shouldShowToast.isNotBlank()) {
            Toast.makeText(context, viewModel.shouldShowToast, Toast.LENGTH_LONG).show()
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