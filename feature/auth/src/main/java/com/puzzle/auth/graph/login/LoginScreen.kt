package com.puzzle.auth.graph.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.kakao.sdk.user.UserApiClient
import com.puzzle.auth.graph.login.contract.LoginIntent.Navigate
import com.puzzle.auth.graph.login.contract.LoginState
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceLoginButton
import com.puzzle.designsystem.component.PieceSubCloseTopBar
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
//                    Log.e("test", "로그인 실패", error)
                } else if (token != null) {
//                    Log.i("test", "로그인 성공 ${token.accessToken}")
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = PieceTheme.colors.primaryDefault)) {
                    append("Piece")
                }

                append("에서 마음이 통하는\n이상형을 만나보세요")
            },
            style = PieceTheme.typography.headingLSB,
            color = PieceTheme.colors.black,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "서로의 빈 곳을 채우며 맞물리는 퍼즐처럼.\n서로의 가치관과 마음이 연결되는 순간을 만들어갑니다.",
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.dark3,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(70.dp))

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
            onClick = {},
            containerColor = Color(0xFFFFE812),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        PieceLoginButton(
            label = stringResource(R.string.google_login),
            imageId = R.drawable.ic_google_login,
            onClick = {},
            containerColor = PieceTheme.colors.white,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = PieceTheme.colors.light1,
                    shape = RoundedCornerShape(8.dp)
                ),
        )

        Spacer(modifier = Modifier.height(10.dp))
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