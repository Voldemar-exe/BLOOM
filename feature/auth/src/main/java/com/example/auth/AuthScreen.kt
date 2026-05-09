package com.example.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bloom.feature.auth.R
import com.example.designsystem.component.AnimatedText
import com.example.designsystem.component.ImagePlaceholder
import com.example.designsystem.picture.BloomIcons
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = koinViewModel(),
) {
    val state by viewModel.authUiState.collectAsStateWithLifecycle()

    AuthScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier,
    )
}

@Composable
internal fun AuthScreen(
    state: AuthState,
    onAction: (AuthAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ImagePlaceholder(
            modifier =
                Modifier
                    .size(200.dp)
                    .padding(bottom = 4.dp),
        )

        AnimatedContent(
            targetState = state.isRegisterMode,
            transitionSpec = {
                (
                    scaleIn(tween(300, easing = LinearOutSlowInEasing), initialScale = 0.96f) +
                        fadeIn(
                            tween(300, easing = LinearOutSlowInEasing),
                        )
                ).togetherWith(
                    scaleOut(
                        tween(300, easing = FastOutSlowInEasing),
                        targetScale = 0.96f,
                    ) + fadeOut(tween(300, easing = FastOutSlowInEasing)),
                )
            },
        ) { isRegister ->
            AuthFormCard(modifier = Modifier.padding(vertical = 16.dp)) {
                if (!isRegister) {
                    LoginForm(state, onAction)
                } else {
                    RegisterForm(state, onAction)
                }
            }
        }

        Button(
            onClick = {
                if (!state.isRegisterMode) {
                    onAction(AuthAction.LoginClicked(state.login, state.password))
                } else {
                    onAction(
                        AuthAction.RegisterClicked(
                            login = state.login,
                            email = state.email,
                            password = state.password,
                        ),
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
        ) {
            Icon(painterResource(BloomIcons.Login), contentDescription = null)
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            AnimatedText(
                stringResource(
                    if (!state.isRegisterMode) {
                        R.string.login_button
                    } else {
                        R.string.register_button
                    },
                ),
            )
        }

        Column(
            modifier = Modifier.padding(top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            AnimatedText(
                stringResource(
                    if (!state.isRegisterMode) {
                        R.string.login_to_register_text
                    } else {
                        R.string.register_to_login_text
                    },
                ),
            )
            TextButton(
                onClick = { onAction(AuthAction.ToggleAuthMode) },
                contentPadding = ButtonDefaults.TextButtonContentPadding,
            ) {
                Icon(
                    painterResource(
                        if (!state.isRegisterMode) {
                            BloomIcons.PersonAdd
                        } else {
                            BloomIcons.PersonShield
                        },
                    ),
                    contentDescription = null,
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                AnimatedText(
                    stringResource(
                        if (!state.isRegisterMode) {
                            R.string.login_to_register_text_button
                        } else {
                            R.string.register_to_login_text_button
                        },
                    ),
                )
            }
        }

        Spacer(Modifier.weight(1f))

        AnimatedVisibility(
            visible = !state.isRegisterMode,
        ) {
            Button(onClick = { onAction(AuthAction.SkipAuth) }) {
                Icon(
                    painterResource(BloomIcons.Login),
                    contentDescription = null,
                )
                Text(stringResource(R.string.login_skip_button))
            }
        }
    }
}

@Composable
private fun AuthFormCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        content()
    }
}

@Composable
private fun LoginForm(
    state: AuthState,
    onAction: (AuthAction) -> Unit,
) {
    val loginState = rememberTextFieldState(state.login)
    val passwordState = rememberTextFieldState(state.password)

    SyncToAction(loginState) { onAction(AuthAction.LoginChanged(it)) }
    SyncToAction(passwordState) { onAction(AuthAction.PasswordChanged(it)) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AuthTextField(
            state = loginState,
            leadingIcon = Icons.Outlined.Person,
            labelRes = R.string.login_label,
            onClear = { loginState.clearText() },
        )
        AuthSecureTextField(
            state = passwordState,
            leadingIcon = Icons.Outlined.Lock,
            labelRes = R.string.password_label,
            onClear = { passwordState.clearText() },
        )
    }
}

@Composable
private fun RegisterForm(
    state: AuthState,
    onAction: (AuthAction) -> Unit,
) {
    val loginState = rememberTextFieldState(state.login)
    val emailState = rememberTextFieldState(state.email)
    val passwordState = rememberTextFieldState(state.password)
    val confirmState = rememberTextFieldState(state.confirmPassword)

    SyncToAction(loginState) { onAction(AuthAction.LoginChanged(it)) }
    SyncToAction(emailState) { onAction(AuthAction.EmailChanged(it)) }
    SyncToAction(passwordState) { onAction(AuthAction.PasswordChanged(it)) }
    SyncToAction(confirmState) { onAction(AuthAction.ConfirmPasswordChanged(it)) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AuthTextField(
            state = loginState,
            leadingIcon = Icons.Outlined.Person,
            labelRes = R.string.login_label,
            onClear = { loginState.clearText() },
        )
        AuthTextField(
            state = emailState,
            leadingIcon = Icons.Outlined.Email,
            labelRes = R.string.email_label,
            keyboardType = KeyboardType.Email,
            onClear = { emailState.clearText() },
        )
        AuthSecureTextField(
            state = passwordState,
            leadingIcon = Icons.Outlined.Lock,
            labelRes = R.string.password_label,
            onClear = { passwordState.clearText() },
        )
        AuthSecureTextField(
            state = confirmState,
            leadingIcon = Icons.Outlined.Lock,
            labelRes = R.string.confirm_password_label,
            onClear = { confirmState.clearText() },
        )
    }
}

@Composable
private fun AuthTextField(
    state: TextFieldState,
    leadingIcon: ImageVector,
    labelRes: Int,
    keyboardType: KeyboardType = KeyboardType.Text,
    onClear: () -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        state = state,
        leadingIcon = { Icon(leadingIcon, contentDescription = null) },
        trailingIcon = {
            if (state.text.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(Icons.Default.Clear, contentDescription = null)
                }
            }
        },
        label = { Text(stringResource(labelRes)) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
    )
}

@Composable
private fun AuthSecureTextField(
    state: TextFieldState,
    leadingIcon: ImageVector,
    labelRes: Int,
    onClear: () -> Unit,
) {
    OutlinedSecureTextField(
        modifier = Modifier.fillMaxWidth(),
        state = state,
        leadingIcon = { Icon(leadingIcon, contentDescription = null) },
        trailingIcon = {
            if (state.text.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(Icons.Default.Clear, contentDescription = null)
                }
            }
        },
        label = { Text(stringResource(labelRes)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
    )
}

@Composable
private fun SyncToAction(
    textFieldState: TextFieldState,
    action: (String) -> Unit,
) {
    LaunchedEffect(textFieldState) {
        snapshotFlow { textFieldState.text.toString() }.collectLatest { action(it) }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
internal fun AuthLoginPreview() {
    val mockState =
        AuthState(
            login = "",
            email = "",
            password = "",
            confirmPassword = "",
            isRegisterMode = false,
        )
    AuthScreen(state = mockState, onAction = {})
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
internal fun AuthRegisterPreview() {
    val mockState =
        AuthState(
            login = "demo_user",
            email = "demo@example.com",
            password = "",
            confirmPassword = "",
            isRegisterMode = true,
        )
    AuthScreen(state = mockState, onAction = {})
}
