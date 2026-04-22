package com.example.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bloom.feature.auth.R
import com.example.designsystem.component.ImagePlaceholder
import com.example.designsystem.picture.BloomIcons
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    // TODO: Replace with koin injection
    viewModel: AuthViewModel = AuthViewModel(),
) {
    val authUiState by viewModel.authUiState.collectAsStateWithLifecycle()

    RegisterScreen(
        authUiState = authUiState,
        onAction = viewModel::onAction,
        modifier = modifier.padding(16.dp),
    )
}

@Composable
internal fun RegisterScreen(
    authUiState: AuthState,
    onAction: (AuthAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {},
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ImagePlaceholder(
                modifier =
                    Modifier
                        .padding(bottom = 4.dp)
                        .size(200.dp),
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val loginState = rememberTextFieldState(authUiState.login)
                LaunchedEffect(loginState) {
                    snapshotFlow { loginState.text.toString() }.collectLatest {
                        onAction(AuthAction.LoginChanged(it))
                    }
                }

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = loginState,
                    leadingIcon = {
                        Icon(Icons.Outlined.Person, contentDescription = null)
                    },
                    trailingIcon = {
                        if (loginState.text.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null,
                                modifier =
                                    Modifier
                                        .size(24.dp)
                                        .padding(4.dp),
                            )
                        }
                    },
                    label = { Text(text = stringResource(R.string.login_label)) },
                )

                val emailState = rememberTextFieldState(authUiState.email)
                LaunchedEffect(emailState) {
                    snapshotFlow { emailState.text.toString() }.collectLatest {
                        onAction(AuthAction.EmailChanged(it))
                    }
                }

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = emailState,
                    leadingIcon = {
                        Icon(Icons.Outlined.Email, contentDescription = null)
                    },
                    trailingIcon = {
                        if (emailState.text.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null,
                                modifier =
                                    Modifier
                                        .size(24.dp)
                                        .padding(4.dp),
                            )
                        }
                    },
                    label = { Text(text = stringResource(R.string.email_label)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                )

                val passwordState = rememberTextFieldState(authUiState.password)
                LaunchedEffect(passwordState) {
                    snapshotFlow { passwordState.text.toString() }.collectLatest {
                        onAction(AuthAction.PasswordChanged(it))
                    }
                }

                OutlinedSecureTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = passwordState,
                    leadingIcon = {
                        Icon(imageVector = Icons.Outlined.Lock, contentDescription = null)
                    },
                    trailingIcon = {
                        if (passwordState.text.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null,
                                modifier =
                                    Modifier
                                        .size(24.dp)
                                        .padding(4.dp),
                            )
                        }
                    },
                    label = { Text(text = stringResource(R.string.password_label)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                )

                val confirmPasswordState = rememberTextFieldState(authUiState.password)
                LaunchedEffect(confirmPasswordState) {
                    snapshotFlow { confirmPasswordState.text.toString() }.collectLatest {
                        onAction(AuthAction.ConfirmPasswordChanged(it))
                    }
                }

                OutlinedSecureTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = confirmPasswordState,
                    leadingIcon = {
                        Icon(imageVector = Icons.Outlined.Lock, contentDescription = null)
                    },
                    trailingIcon = {
                        if (confirmPasswordState.text.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null,
                                modifier =
                                    Modifier
                                        .size(24.dp)
                                        .padding(4.dp),
                            )
                        }
                    },
                    label = { Text(text = stringResource(R.string.confirm_password_label)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Button(
                        onClick = {
                            onAction(
                                AuthAction.RegisterClicked(
                                    login = loginState.text.toString(),
                                    email = emailState.text.toString(),
                                    password = passwordState.text.toString(),
                                ),
                            )
                        },
                        contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                    ) {
                        Icon(
                            painter = painterResource(BloomIcons.Login),
                            contentDescription = null,
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(text = stringResource(R.string.register_button))
                    }

                    Text(text = stringResource(R.string.register_to_login_text))
                    TextButton(
                        onClick = { onAction(AuthAction.ToggleAuthMode) },
                        contentPadding = ButtonDefaults.TextButtonContentPadding,
                    ) {
                        Icon(
                            painter = painterResource(BloomIcons.PersonShiel),
                            contentDescription = null,
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(text = stringResource(R.string.register_to_login_text_button))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
internal fun RegisterPreview() {
    RegisterScreen()
}
