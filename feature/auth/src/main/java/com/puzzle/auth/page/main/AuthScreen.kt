package com.puzzle.auth.page.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel

@Composable
fun AuthRoute(
    viewModel: AuthViewModel = mavericksViewModel(),
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current

    AuthScreen(

    )
}

@Composable
fun AuthScreen() {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .clickable {
//                viewModel.navigationHelper.navigate(
//                    NavigationEvent.NavigateTo(
//                        route = MatchingGraph,
//                        popUpTo = AuthGraph,
//                    )
//                )
//            },
//    ) {
//        Text(
//            text = "카카오 로그인",
//            fontSize = 30.sp,
//            modifier = Modifier.clickable {
//                UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
//                    if (error != null) {
//                        Log.e("test", "로그인 실패", error)
//                    } else if (token != null) {
//                        Log.i("test", "로그인 성공 ${token.accessToken}")
//                    }
//                }
//            }
//        )
//
//        Text(
//            text = "AuthRoute",
//            fontSize = 30.sp,
//        )
//    }
}

@Preview
@Composable
fun PreviewAuthScreen() {

}