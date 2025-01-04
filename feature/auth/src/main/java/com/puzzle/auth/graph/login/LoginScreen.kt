package com.puzzle.auth.graph.login

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.kakao.sdk.user.UserApiClient
import com.puzzle.auth.graph.login.contract.LoginIntent.Navigate
import com.puzzle.auth.graph.login.contract.LoginState
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.navigation.AuthGraph
import com.puzzle.navigation.AuthGraphDest
import com.puzzle.navigation.NavigationEvent

@Composable
fun LoginRoute(
    viewModel: LoginViewModel = mavericksViewModel(),
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current

    LoginScreen(
        state = state,
        loginKakao = {
            UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                if (error != null) {
                    Log.e("test", "로그인 실패", error)
                } else if (token != null) {
                    Log.i("test", "로그인 성공 ${token.accessToken}")
                }
            }
        },
        navigate = { viewModel.onIntent(Navigate(it)) },
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    loginKakao: () -> Unit,
    navigate: (NavigationEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                navigate(
                    NavigationEvent.NavigateTo(
                        route = AuthGraphDest.RegistrationRoute,
                        popUpTo = AuthGraph,
                    )
                )
            },
    ) {
        Text(
            text = "카카오 로그인",
            fontSize = 30.sp,
            modifier = Modifier.clickable { loginKakao() },
        )

        Text(
            text = "AuthRoute",
            fontSize = 30.sp,
        )
    }
}

@Preview
@Composable
fun PreviewAuthScreen() {
    PieceTheme {
        LoginScreen(
            state = LoginState(),
            loginKakao = {},
            navigate = {},
        )
    }
}