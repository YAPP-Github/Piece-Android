package com.puzzle.auth.graph.login

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.kakao.sdk.user.UserApiClient
import com.puzzle.auth.graph.login.contract.LoginIntent
import com.puzzle.auth.graph.login.contract.LoginIntent.Navigate
import com.puzzle.auth.graph.login.contract.LoginSideEffect
import com.puzzle.auth.graph.login.contract.LoginState
import com.puzzle.common.ui.repeatOnStarted
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceLoginButton
import com.puzzle.designsystem.component.PieceSubCloseTopBar
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.domain.model.auth.OAuthProvider
import com.puzzle.navigation.AuthGraph
import com.puzzle.navigation.AuthGraphDest
import com.puzzle.navigation.NavigationEvent

@Composable
internal fun LoginRoute(
    viewModel: LoginViewModel = mavericksViewModel(),
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnStarted {
            viewModel.sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is LoginSideEffect.LoginKakao -> loginKakao(
                        context = context,
                        onFailure = { viewModel.loginFailure(it) },
                        onSuccess = {
                            viewModel.loginOAuth(
                                oAuthProvider = OAuthProvider.KAKAO,
                                token = it,
                            )
                        },
                    )

                    is LoginSideEffect.LoginGoogle -> {}
                }
            }
        }
    }

    LoginScreen(
        state = state,
        loginKakao = { viewModel.onIntent(LoginIntent.LoginOAuth(OAuthProvider.KAKAO)) },
        loginGoogle = { viewModel.onIntent(LoginIntent.LoginOAuth(OAuthProvider.GOOGLE)) },
        navigate = { viewModel.onIntent(Navigate(it)) },
    )
}

@Composable
private fun LoginScreen(
    state: LoginState,
    loginKakao: () -> Unit,
    loginGoogle: () -> Unit,
    navigate: (NavigationEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PieceTheme.colors.white)
            .padding(horizontal = 20.dp)
            .clickable {
                navigate(
                    NavigationEvent.NavigateTo(
                        route = AuthGraphDest.VerificationRoute,
                        popUpTo = AuthGraph,
                    )
                )
            },
    ) {
        PieceSubCloseTopBar(
            title = "",
            onCloseClick = { },
            modifier = Modifier.fillMaxWidth(),
        )

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
                .padding(top = 20.dp, bottom = 12.dp),
        )

        Text(
            text = stringResource(R.string.login_description),
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.dark3,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 70.dp),
        )

        Image(
            painter = painterResource(R.drawable.ic_puzzle1),
            contentDescription = null,
            modifier = Modifier
                .size(240.dp)
                .align(Alignment.CenterHorizontally),
        )

        Spacer(modifier = Modifier.weight(1f))

        PieceLoginButton(
            label = stringResource(R.string.kakao_login),
            imageId = R.drawable.ic_kakao_login,
            containerColor = Color(0xFFFFE812),
            onClick = loginKakao,
            modifier = Modifier.fillMaxWidth(),
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
                .padding(vertical = 10.dp),
        )
    }
}

private fun loginKakao(
    context: Context,
    onFailure: (Throwable) -> Unit,
    onSuccess: (String) -> Unit,
) {
    UserApiClient.instance.apply {
        if (isKakaoTalkLoginAvailable(context)) {
            // 카카오톡 로그인
            loginWithKakaoAccount(context) { token, error ->
                if (error != null) {
                    onFailure(error)
                } else if (token != null) {
                    onSuccess(token.accessToken)
                }
            }
        } else {
            // 카카오 계정 로그인 (웹)
            loginWithKakaoAccount(context) { token, error ->
                if (error != null) {
                    onFailure(error)
                } else if (token != null) {
                    onSuccess(token.accessToken)
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAuthScreen() {
    PieceTheme {
        LoginScreen(
            state = LoginState(),
            loginKakao = {},
            loginGoogle = {},
            navigate = {},
        )
    }
}