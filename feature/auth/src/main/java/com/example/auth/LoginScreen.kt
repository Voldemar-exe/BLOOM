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
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = koinViewModel(),
) {
    val authUiState by viewModel.authUiState.collectAsStateWithLifecycle()

    LoginScreen(
        authUiState = authUiState,
        onAction = viewModel::onAction,
        modifier = modifier.padding(16.dp),
    )
}

@Composable
internal fun LoginScreen(
    authUiState: AuthState,
    onAction: (AuthAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val loginState = rememberTextFieldState(authUiState.login)
    val passwordState = rememberTextFieldState(authUiState.password)

    LaunchedEffect(loginState) {
        snapshotFlow { loginState.text.toString() }.collectLatest {
            onAction(AuthAction.LoginChanged(it))
        }
    }

    LaunchedEffect(passwordState) {
        snapshotFlow { passwordState.text.toString() }.collectLatest {
            onAction(AuthAction.PasswordChanged(it))
        }
    }

    Scaffold(
        topBar = {},
        bottomBar = {
            Button(
                onClick = {
                    onAction(
                        AuthAction.LoginClicked(
                            loginState.text.toString(),
                            passwordState.text.toString(),
                        ),
                    )
                },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            ) {
                Icon(
                    painter = painterResource(BloomIcons.Login),
                    contentDescription = "login",
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = stringResource(R.string.login_button))
            }
        },
    ) { paddingValues ->
        Column(
            modifier =
                modifier
                    .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ImagePlaceholder(
                modifier =
                    Modifier
                        .padding(bottom = 4.dp)
                        .size(200.dp),
            )

            // TODO: Auto-change Supportive Text
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = loginState,
                    leadingIcon = {
                        Icon(Icons.Outlined.Person, contentDescription = "personal")
                    },
                    label = { Text(text = stringResource(R.string.login_label)) },
                )

                OutlinedSecureTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = passwordState,
                    leadingIcon = {
                        Icon(imageVector = Icons.Outlined.Lock, contentDescription = "password")
                    },
                    label = { Text(text = stringResource(R.string.password_label)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                )
            }
            Column(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Button(
                    onClick = {
                        onAction(
                            AuthAction.LoginClicked(
                                loginState.text.toString(),
                                passwordState.text.toString(),
                            ),
                        )
                    },
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                ) {
                    Icon(
                        painter = painterResource(BloomIcons.Login),
                        contentDescription = "login",
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = stringResource(R.string.login_button))
                }
                Text(text = stringResource(R.string.login_to_register_text))
                TextButton(onClick = { onAction(AuthAction.ToggleAuthMode) }) {
                    Icon(
                        painter = painterResource(BloomIcons.PersonAdd),
                        contentDescription = "login",
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = stringResource(R.string.login_to_register_text_button))
                }
            }
        }
    }
}

@Preview(showBackground = false, showSystemUi = true)
@Composable
internal fun LoginPreview() {
    LoginScreen()
}
