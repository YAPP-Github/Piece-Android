package com.puzzle.auth

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kakao.sdk.user.UserApiClient
import com.puzzle.navigation.AuthGraph
import com.puzzle.navigation.MatchingGraph
import com.puzzle.navigation.NavigationEvent

@Composable
fun AuthRoute(viewModel: AuthViewModel = hiltViewModel()) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                viewModel.navigationHelper.navigate(
                    NavigationEvent.NavigateTo(
                        route = MatchingGraph,
                        popUpTo = AuthGraph,
                    )
                )
            },
    ) {
        Text(
            text = "카카오 로그인",
            fontSize = 30.sp,
            modifier = Modifier.clickable {
                UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                    if (error != null) {
                        Log.e("test", "로그인 실패", error)
                    } else if (token != null) {
                        Log.i("test", "로그인 성공 ${token.accessToken}")
                    }
                }
            }
        )

        Text(
            text = "AuthRoute",
            fontSize = 30.sp,
        )
    }
}

@Composable
fun AuthScreen() {
}