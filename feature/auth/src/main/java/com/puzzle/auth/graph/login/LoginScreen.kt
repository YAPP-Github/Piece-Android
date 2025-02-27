package com.puzzle.auth.graph.login

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.user.UserApiClient
import com.puzzle.auth.graph.login.contract.LoginIntent
import com.puzzle.auth.graph.login.contract.LoginSideEffect
import com.puzzle.common.ui.repeatOnStarted
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceLoginButton
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.domain.model.auth.OAuthProvider

@Composable
internal fun LoginRoute(
    viewModel: LoginViewModel = mavericksViewModel(),
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val authResultLauncher = rememberLauncherForActivityResult(
        contract = GoogleApiContract()
    ) { task ->
        handleGoogleSignIn(
            task = task,
            onSuccess = { accessToken -> viewModel.loginOAuth(OAuthProvider.GOOGLE, accessToken) },
            onFailure = { viewModel.loginFailure(it) },
        )
    }

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnStarted {
            viewModel.sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is LoginSideEffect.LoginKakao -> loginKakao(
                        context = context,
                        onFailure = { viewModel.loginFailure(it) },
                        onSuccess = { accessToken ->
                            viewModel.loginOAuth(
                                oAuthProvider = OAuthProvider.KAKAO,
                                token = accessToken,
                            )
                        },
                    )

                    is LoginSideEffect.LoginGoogle -> authResultLauncher.launch(Unit)
                }
            }
        }
    }

    LoginScreen(
        loginKakao = { viewModel.onIntent(LoginIntent.LoginOAuth(OAuthProvider.KAKAO)) },
        loginGoogle = { viewModel.onIntent(LoginIntent.LoginOAuth(OAuthProvider.GOOGLE)) },
    )
}

@Composable
private fun LoginScreen(
    loginKakao: () -> Unit,
    loginGoogle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PieceTheme.colors.white),
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = PieceTheme.colors.primaryDefault)) {
                    append("Piece")
                }

                append("에서 마음이 통하는\n이상형을 만나보세요")
            },
            style = PieceTheme.typography.headingLSB,
            color = PieceTheme.colors.black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp, bottom = 12.dp, start = 20.dp, end = 20.dp),
        )

        Text(
            text = stringResource(R.string.login_description),
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.dark3,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        )

        Image(
            painter = painterResource(R.drawable.ic_login_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        )

        PieceLoginButton(
            label = stringResource(R.string.kakao_login),
            imageId = R.drawable.ic_kakao_login,
            containerColor = Color(0xFFFFE812),
            onClick = loginKakao,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        )

        PieceLoginButton(
            label = stringResource(R.string.google_login),
            imageId = R.drawable.ic_google_login,
            containerColor = PieceTheme.colors.white,
            border = BorderStroke(
                width = 1.dp,
                color = PieceTheme.colors.light1,
            ),
            onClick = loginGoogle,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 20.dp),
        )
    }
}

private fun loginKakao(
    context: Context,
    onSuccess: (String) -> Unit,
    onFailure: (Throwable) -> Unit,
) {
    UserApiClient.instance.apply {
        if (isKakaoTalkLoginAvailable(context)) {
            // 카카오톡 로그인
            loginWithKakaoAccount(context) { token, error ->
                if (error != null && error !is ClientError) {
                    onFailure(error)
                } else if (token != null) {
                    onSuccess(token.accessToken)
                }
            }
        } else {
            // 카카오 계정 로그인 (웹)
            loginWithKakaoAccount(context) { token, error ->
                if (error != null && error !is ClientError) {
                    onFailure(error)
                } else if (token != null) {
                    onSuccess(token.accessToken)
                }
            }
        }
    }
}

private fun handleGoogleSignIn(
    task: Task<GoogleSignInAccount>?,
    onSuccess: (String) -> Unit,
    onFailure: (Throwable) -> Unit,
) {
    if (task == null) return

    try {
        val account = task.result
        if (account != null) {
            val idToken = account.idToken
            if (!idToken.isNullOrEmpty()) {
                onSuccess(idToken)
            } else {
                onFailure(IllegalStateException("ID Token is null or empty"))
            }
        } else {
            onFailure(IllegalStateException("GoogleSignInAccount is null"))
        }
    } catch (e: Exception) {
        onFailure(e)
    }
}

@Preview
@Composable
private fun PreviewAuthScreen() {
    PieceTheme {
        LoginScreen(
            loginKakao = {},
            loginGoogle = {},
        )
    }
}
