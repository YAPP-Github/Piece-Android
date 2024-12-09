package com.example.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun AuthRoute(
    onLoginSuccess: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "AuthRoute",
            fontSize = 30.sp,
            modifier = Modifier.clickable { onLoginSuccess() })
    }
}

@Composable
fun AuthScreen() {
}