package com.puzzle.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.puzzle.navigation.AuthGraph
import com.puzzle.navigation.MatchingGraph
import com.puzzle.navigation.NavigationEvent

@Composable
fun AuthRoute(viewModel: AuthViewModel = hiltViewModel()) {

    Box(
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
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "AuthRoute",
            fontSize = 30.sp,
        )
    }
}

@Composable
fun AuthScreen() {
}