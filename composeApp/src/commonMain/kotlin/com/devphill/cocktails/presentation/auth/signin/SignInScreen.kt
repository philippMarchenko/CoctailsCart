package com.devphill.cocktails.presentation.auth.signin

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cocktailscart.composeapp.generated.resources.Res
import cocktailscart.composeapp.generated.resources.continue_as_guest
import cocktailscart.composeapp.generated.resources.dont_have_account
import cocktailscart.composeapp.generated.resources.email_address
import cocktailscart.composeapp.generated.resources.or
import cocktailscart.composeapp.generated.resources.password
import cocktailscart.composeapp.generated.resources.sign_in
import cocktailscart.composeapp.generated.resources.sign_in_with_google
import cocktailscart.composeapp.generated.resources.sign_up
import cocktailscart.composeapp.generated.resources.welcome_back
import com.devphill.cocktails.presentation.auth.AuthState
import com.devphill.cocktails.presentation.auth.AuthViewModel
import com.devphill.cocktails.presentation.common.AuthTextField
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignInScreen(
    onSignInSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = koinViewModel(),
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Use ViewModel state instead of local state
    val isLoading = viewModel.authState is AuthState.Loading
    val errorMessage = (viewModel.authState as? AuthState.Error)?.message

    // Handle auth state changes
    LaunchedEffect(viewModel.authState) {
        when (viewModel.authState) {
            is AuthState.Authenticated -> {
                onSignInSuccess()
                viewModel.resetToUnauthenticated()
            }
            is AuthState.GoogleSignInRequired -> {
                // On platforms other than Android, show an error
                // Android platform will handle this state with platform-specific UI
            }
            else -> { /* Handle other states if needed */ }
        }
    }

    SignInScreenContent(
        email = email,
        password = password,
        passwordVisible = passwordVisible,
        isLoading = isLoading,
        errorMessage = errorMessage,
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
        onSignIn = {
            viewModel.signInWithEmailAndPassword(email, password)
        },
        onGoogleSignIn = {
            viewModel.signInWithGoogle()
        },
        onContinueAsGuest = {
            viewModel.signInAsGuest()
        },
        onNavigateToSignUp = onNavigateToSignUp,
        modifier = modifier,
    )
}

@Composable
private fun WelcomeHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // App Icon
        Text(
            text = "🍸",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 4.dp),
        )

        // Welcome Text
        Text(
            text = stringResource(Res.string.welcome_back),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )

        Text(
            text = "Sign in to access your cocktails",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }
}

@Composable
private fun SignInForm(
    email: String,
    password: String,
    passwordVisible: Boolean,
    isLoading: Boolean,
    errorMessage: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityToggle: () -> Unit,
    onSignIn: () -> Unit,
    onGoogleSignIn: () -> Unit,
    onContinueAsGuest: () -> Unit,
    onNavigateToSignUp: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(Res.string.sign_in),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            AuthTextField(
                value = email,
                onValueChange = onEmailChange,
                label = stringResource(Res.string.email_address),
                leadingIcon = Icons.Default.Email,
                keyboardType = KeyboardType.Email,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                singleLine = true,
            )

            AuthTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = stringResource(Res.string.password),
                leadingIcon = Icons.Default.Lock,
                trailingIcon = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                onTrailingIconClick = onPasswordVisibilityToggle,
                keyboardType = KeyboardType.Password,
                visualTransformation =
                    if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                singleLine = true,
            )

            // Error message display
            errorMessage?.let { message ->
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }

            // Sign In Button
            Button(
                onClick = onSignIn,
                enabled = !isLoading && email.isNotBlank() && password.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                } else {
                    Text(stringResource(Res.string.sign_in))
                }
            }

            // Divider with OR
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(Res.string.or),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                HorizontalDivider(modifier = Modifier.weight(1f))
            }

            Row(
                modifier = Modifier.wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    onClick = onGoogleSignIn,
                    enabled = !isLoading,
                    modifier = Modifier.weight(1f).alpha(1f),
                ) {
                    Text(stringResource(Res.string.sign_in_with_google))
                }

                Button(
                    onClick = onContinueAsGuest,
                    modifier = Modifier.weight(1f).alpha(1f),
                ) {
                    Text(stringResource(Res.string.continue_as_guest))
                }
            }

            // Navigate to Sign Up
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(Res.string.dont_have_account),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(
                    onClick = onNavigateToSignUp,
                    enabled = !isLoading,
                ) {
                    Text(stringResource(Res.string.sign_up))
                }
            }
        }
    }
}

@Composable
private fun SignInScreenContent(
    email: String,
    password: String,
    passwordVisible: Boolean,
    isLoading: Boolean,
    errorMessage: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityToggle: () -> Unit,
    onSignIn: () -> Unit,
    onGoogleSignIn: () -> Unit,
    onContinueAsGuest: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var startAnimation by remember { mutableStateOf(true) }

    val alphaAnimation =
        animateFloatAsState(
            targetValue = if (startAnimation) 1f else 0f,
            animationSpec = tween(durationMillis = 800),
            label = "alpha",
        )

    val offsetAnimation =
        animateDpAsState(
            targetValue = if (startAnimation) 0.dp else 50.dp,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            label = "offset",
        )

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .alpha(alphaAnimation.value)
                .offset(y = offsetAnimation.value)
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(
                        rememberScrollState(),
                        enabled = true,
                    ),
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // App Logo and Welcome Text
            WelcomeHeader()

            Spacer(modifier = Modifier.height(16.dp))

            // Sign In Form
            SignInForm(
                email = email,
                password = password,
                passwordVisible = passwordVisible,
                isLoading = isLoading,
                errorMessage = errorMessage,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onPasswordVisibilityToggle = onPasswordVisibilityToggle,
                onSignIn = onSignIn,
                onGoogleSignIn = onGoogleSignIn,
                onContinueAsGuest = onContinueAsGuest,
                onNavigateToSignUp = onNavigateToSignUp,
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
