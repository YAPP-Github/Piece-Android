package com.puzzle.matching.graph.report

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.common.ui.addFocusCleaner
import com.puzzle.common.ui.repeatOnStarted
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceDialog
import com.puzzle.designsystem.component.PieceDialogBottom
import com.puzzle.designsystem.component.PieceDialogDefaultTop
import com.puzzle.designsystem.component.PieceRadio
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.component.PieceSubBackTopBar
import com.puzzle.designsystem.component.PieceTextInputLong
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.matching.graph.report.contract.ReportIntent
import com.puzzle.matching.graph.report.contract.ReportSideEffect
import com.puzzle.matching.graph.report.contract.ReportState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun ReportRoute(
    matchId: Int,
    userName: String,
    viewModel: ReportViewModel = mavericksViewModel(),
) {
    val state by viewModel.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel) {
        lifecycleOwner.repeatOnStarted {
            viewModel.sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is ReportSideEffect.Navigate ->
                        viewModel.navigationHelper.navigate(sideEffect.navigationEvent)
                }
            }
        }
    }
    ReportScreen(
        state = state,
        userName = userName,
        onBackClick = { viewModel.onIntent(ReportIntent.OnBackClick) },
        onReasonClick = { reason -> viewModel.onIntent(ReportIntent.SelectReportReason(reason)) },
        onReportClick = { reason ->
            viewModel.onIntent(
                ReportIntent.OnReportButtonClick(
                    userId = matchId,
                    reason = reason,
                )
            )
        },
        onReportDoneClick = { viewModel.onIntent(ReportIntent.OnReportDoneClick) },
    )
}

@Composable
internal fun ReportScreen(
    state: ReportState,
    userName: String,
    onBackClick: () -> Unit,
    onReasonClick: (ReportState.ReportReason) -> Unit,
    onReportClick: (String) -> Unit,
    onReportDoneClick: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var textInput by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var isReportDialogShow by remember { mutableStateOf(false) }
    if (isReportDialogShow) {
        PieceDialog(
            onDismissRequest = { isReportDialogShow = false },
            dialogTop = {
                PieceDialogDefaultTop(
                    title = stringResource(R.string.report_main_title, userName),
                    subText = stringResource(R.string.report_subtitle),
                )
            },
            dialogBottom = {
                PieceDialogBottom(
                    leftButtonText = stringResource(R.string.cancel),
                    rightButtonText = stringResource(R.string.report),
                    onLeftButtonClick = { isReportDialogShow = false },
                    onRightButtonClick = {
                        isReportDialogShow = false
                        onReportClick(
                            if (state.selectedReason == ReportState.ReportReason.OTHER) textInput
                            else state.selectedReason?.label ?: ""
                        )
                    },
                )
            },
        )
    }

    if (state.isReportDone) {
        PieceDialog(
            onDismissRequest = {},
            dialogTop = {
                PieceDialogDefaultTop(
                    title = stringResource(R.string.report_done_title, userName),
                    subText = stringResource(R.string.report_done_subtitle),
                )
            },
            dialogBottom = {
                PieceSolidButton(
                    label = stringResource(R.string.report_home),
                    onClick = { onReportDoneClick() },
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
            .addFocusCleaner(focusManager),
    ) {
        PieceSubBackTopBar(
            title = stringResource(R.string.report),
            onBackClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        )

        HorizontalDivider(
            color = PieceTheme.colors.light2,
            thickness = 1.dp,
            modifier = Modifier.padding(bottom = 6.dp),
        )

        var isKeyboardVisible by remember { mutableStateOf(false) }
        AnimatedVisibility(visible = !isKeyboardVisible) {
            Column {
                Text(
                    text = stringResource(R.string.report_main_title, userName),
                    textAlign = TextAlign.Start,
                    style = PieceTheme.typography.headingLSB,
                    color = PieceTheme.colors.black,
                    modifier = Modifier.padding(
                        top = 14.dp,
                        bottom = 12.dp,
                        start = 20.dp,
                        end = 20.dp
                    ),
                )

                Text(
                    text = stringResource(R.string.report_main_subtitle),
                    textAlign = TextAlign.Start,
                    style = PieceTheme.typography.bodySM,
                    color = PieceTheme.colors.dark2,
                    modifier = Modifier.padding(bottom = 40.dp, start = 20.dp, end = 20.dp),
                )

                ReportState.ReportReason.entries.forEach { reason ->
                    if (reason == ReportState.ReportReason.OTHER) return@forEach

                    PieceRadio(
                        selected = (reason == state.selectedReason),
                        label = reason.label,
                        onSelectedChange = { onReasonClick(reason) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                    )
                }
            }
        }

        PieceRadio(
            selected = state.selectedReason == ReportState.ReportReason.OTHER,
            label = ReportState.ReportReason.OTHER.label,
            onSelectedChange = {
                keyboardController?.hide()
                isKeyboardVisible = false
                onReasonClick(ReportState.ReportReason.OTHER)
            },
            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
        )

        AnimatedVisibility(state.selectedReason == ReportState.ReportReason.OTHER) {
            PieceTextInputLong(
                value = textInput,
                onValueChange = { input -> textInput = input },
                hint = stringResource(R.string.withdraw_other_reaseon_textfield_hint),
                limit = 100,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
                    .height(160.dp)
                    .onFocusEvent { focusState -> isKeyboardVisible = focusState.isFocused },
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        val isNextButtonEnabled = state.selectedReason != null &&
                (state.selectedReason != ReportState.ReportReason.OTHER || textInput.isNotEmpty())
        PieceSolidButton(
            label = stringResource(R.string.next),
            enabled = isNextButtonEnabled,
            onClick = {
                coroutineScope.launch {
                    keyboardController?.hide()
                    delay(300L)
                    isReportDialogShow = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 12.dp, start = 20.dp, end = 20.dp),
        )
    }
}

@Preview
@Composable
private fun ReportScreenPreview() {
    PieceTheme {
        ReportScreen(
            state = ReportState(
                selectedReason = ReportState.ReportReason.INAPPROPRIATE_INTRODUCTION
            ),
            userName = "수줍은 수달",
            onBackClick = {},
            onReasonClick = {},
            onReportClick = {},
            onReportDoneClick = {}
        )
    }
}

@Preview
@Composable
private fun ReportScreenOtherReasonPreview() {
    PieceTheme {
        ReportScreen(
            state = ReportState(
                selectedReason = ReportState.ReportReason.OTHER
            ),
            userName = "수줍은 수달",
            onBackClick = {},
            onReasonClick = {},
            onReportClick = {},
            onReportDoneClick = {}
        )
    }
}

@Preview
@Composable
private fun ReportDoneDialogPreview() {
    PieceTheme {
        ReportScreen(
            state = ReportState(
                isReportDone = true
            ),
            userName = "수줍은 수달",
            onBackClick = {},
            onReasonClick = {},
            onReportClick = {},
            onReportDoneClick = {}
        )
    }
}
