package com.puzzle.presentation.network

import android.content.Intent
import android.provider.Settings
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.puzzle.designsystem.R.drawable
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.presentation.network.NetworkState.NotConnected

@Composable
fun NetworkScreen(networkState: NetworkState) {
    val context = LocalContext.current
    var isDialogVisible by remember { mutableStateOf(false) }

    LaunchedEffect(networkState) {
        isDialogVisible = networkState is NotConnected
    }

    if (isDialogVisible) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            ),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .background(PieceTheme.colors.white)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "네트워크 연결이 불안정합니다",
                    style = PieceTheme.typography.headingLSB,
                    color = PieceTheme.colors.black,
                    modifier = Modifier.padding(bottom = 12.dp),
                )

                Text(
                    text = "네트워크가 원활한 곳으로 이동해 주세요!",
                    style = PieceTheme.typography.bodySM,
                    color = PieceTheme.colors.dark3,
                    modifier = Modifier.padding(bottom = 12.dp),
                )

                Image(
                    painter = painterResource(drawable.ic_network_error),
                    contentDescription = null,
                )

                Spacer(modifier = Modifier.weight(1f))

                PieceSolidButton(
                    label = "다시 연결 시도하기",
                    onClick = { context.startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS)) },
                    modifier = Modifier
                        .padding(bottom = 10.dp, top = 12.dp)
                        .fillMaxWidth(),
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewNetworkScreen() {
    NetworkScreen(networkState = NotConnected)
}
