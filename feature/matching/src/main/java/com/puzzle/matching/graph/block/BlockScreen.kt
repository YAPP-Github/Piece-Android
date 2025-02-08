package com.puzzle.matching.graph.block

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.common.ui.repeatOnStarted
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceDialog
import com.puzzle.designsystem.component.PieceDialogBottom
import com.puzzle.designsystem.component.PieceDialogDefaultTop
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.component.PieceSubBackTopBar
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.matching.graph.block.contract.BlockIntent
import com.puzzle.matching.graph.block.contract.BlockSideEffect
import com.puzzle.matching.graph.block.contract.BlockState

@Composable
internal fun BlockRoute(
    userId: Int,
    userName: String,
    viewModel: BlockViewModel = mavericksViewModel(),
) {
    val state by viewModel.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(viewModel) {
        lifecycleOwner.repeatOnStarted {
            viewModel.sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is BlockSideEffect.Navigate -> viewModel.navigationHelper
                        .navigate(sideEffect.navigationEvent)
                }
            }
        }
    }

    BlockScreen(
        state = state,
        userName = userName,
        onBackClick = { viewModel.onIntent(BlockIntent.OnBackClick) },
        onBlockButtonClick = { viewModel.onIntent(BlockIntent.OnBlockButtonClick) },
        onBlockDoneClick = { viewModel.onIntent(BlockIntent.OnBlockDoneClick) },
    )
}

@Composable
internal fun BlockScreen(
    state: BlockState,
    userName: String,
    onBackClick: () -> Unit,
    onBlockButtonClick: () -> Unit,
    onBlockDoneClick: () -> Unit,
) {
    var isBlockDialogShow by remember { mutableStateOf(false) }

    if (isBlockDialogShow) {
        PieceDialog(
            onDismissRequest = { isBlockDialogShow = false },
            dialogTop = {
                PieceDialogDefaultTop(
                    title = "${userName}님을\n차단하시겠습니까 ?",
                    subText = "차단하면 상대방을 영영 만날 수 없게 되며,\n되돌릴 수 없습니다.",
                )
            },
            dialogBottom = {
                PieceDialogBottom(
                    leftButtonText = "취소",
                    rightButtonText = stringResource(R.string.block),
                    onLeftButtonClick = { isBlockDialogShow = false },
                    onRightButtonClick = { onBlockButtonClick() },
                )
            },
        )
    }

    if (state.isBlockDone) {
        PieceDialog(
            onDismissRequest = {},
            dialogTop = {
                PieceDialogDefaultTop(
                    title = "${userName}님을 차단했습니다.",
                    subText = "매칭이 즉시 종료되며,\n상대방에게 차단 사실을 알리지 않습니다.",
                )
            },
            dialogBottom = {
                PieceSolidButton(
                    label = "홈으로",
                    onClick = { onBlockDoneClick() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp, top = 12.dp),
                )
            },
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        PieceSubBackTopBar(
            title = stringResource(R.string.block),
            onBackClick = onBackClick,
        )

        Text(
            text = "${userName}님을\n차단하시겠습니까?",
            textAlign = TextAlign.Start,
            style = PieceTheme.typography.headingLSB,
            color = PieceTheme.colors.black,
            modifier = Modifier.padding(top = 20.dp, bottom = 12.dp),
        )

        Text(
            text = "차단하면 상대방의 매칭이 즉시 종료되며,\n상대방에게 차단 사실을 알리지 않습니다.",
            textAlign = TextAlign.Start,
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.dark2,
            modifier = Modifier.padding(bottom = 52.dp),
        )

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_block_matching_end),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 12.dp),
                )

                Text(
                    text = "차단하는 즉시\n상대방과의 매칭이 종료됩니다.",
                    style = PieceTheme.typography.bodyMSB,
                    color = PieceTheme.colors.dark1,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_block_matching_no_more),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 12.dp),
                )

                Text(
                    text = "차단된 상대와\n더 이상 매칭되지 않습니다.",
                    style = PieceTheme.typography.bodyMSB,
                    color = PieceTheme.colors.dark1,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_block_notification),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 12.dp),
                )

                Text(
                    text = "상대방에게 \n차단한 사실을 알리지 않습니다.",
                    style = PieceTheme.typography.bodyMSB,
                    color = PieceTheme.colors.dark1,
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        PieceSolidButton(
            label = "다음",
            onClick = { isBlockDialogShow = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 12.dp),
        )
    }
}

@Preview
@Composable
private fun PreviewBlockScreen() {
    PieceTheme {
        BlockScreen(
            state = BlockState(),
            userName = "수줍은 수달",
            onBackClick = {},
            onBlockButtonClick = {},
            onBlockDoneClick = {},
        )
    }
}
