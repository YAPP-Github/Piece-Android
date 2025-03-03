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
import com.puzzle.matching.graph.block.contract.BlockState

@Composable
internal fun BlockRoute(
    matchId: Int,
    userName: String,
    viewModel: BlockViewModel = mavericksViewModel(),
) {
    val state by viewModel.collectAsState()

    BlockScreen(
        state = state,
        userName = userName,
        onBackClick = { viewModel.onIntent(BlockIntent.OnBackClick) },
        onBlockButtonClick = { viewModel.onIntent(BlockIntent.OnBlockButtonClick(matchId)) },
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
                    title = stringResource(R.string.block_dialog_title, userName),
                    subText = stringResource(R.string.block_dialog_subtitle),
                )
            },
            dialogBottom = {
                PieceDialogBottom(
                    leftButtonText = stringResource(R.string.cancel),
                    rightButtonText = stringResource(R.string.block),
                    onLeftButtonClick = { isBlockDialogShow = false },
                    onRightButtonClick = {
                        isBlockDialogShow = false
                        onBlockButtonClick()
                    },
                )
            },
        )
    }

    if (state.isBlockDone) {
        PieceDialog(
            onDismissRequest = {},
            dialogTop = {
                PieceDialogDefaultTop(
                    title = stringResource(R.string.block_done_title, userName),
                    subText = stringResource(R.string.block_done_subtitle),
                )
            },
            dialogBottom = {
                PieceSolidButton(
                    label = stringResource(R.string.report_home),
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
            title = stringResource(R.string.block_title),
            onBackClick = onBackClick,
        )

        Text(
            text = stringResource(R.string.block_main_title, userName),
            textAlign = TextAlign.Start,
            style = PieceTheme.typography.headingLSB,
            color = PieceTheme.colors.black,
            modifier = Modifier.padding(top = 20.dp, bottom = 12.dp),
        )

        Text(
            text = stringResource(R.string.block_main_subtitle),
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
                    text = stringResource(R.string.block_matching_end),
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
                    text = stringResource(R.string.block_no_more_matching),
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
                    text = stringResource(R.string.block_no_notification),
                    style = PieceTheme.typography.bodyMSB,
                    color = PieceTheme.colors.dark1,
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        PieceSolidButton(
            label = stringResource(R.string.next),
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
