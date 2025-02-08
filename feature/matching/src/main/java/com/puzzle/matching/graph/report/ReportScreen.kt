package com.puzzle.matching.graph.report

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
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
    userId: Int,
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
        onReportClick = { viewModel.onIntent(ReportIntent.OnReportButtonClick) },
        onReportDoneClick = { viewModel.onIntent(ReportIntent.OnReportDoneClick) },
    )
}

@Composable
internal fun ReportScreen(
    state: ReportState,
    userName: String,
    onBackClick: () -> Unit,
    onReasonClick: (ReportState.ReportReason) -> Unit,
    onReportClick: () -> Unit,
    onReportDoneClick: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var textInput by remember { mutableStateOf("") }
    var isKeyboardVisible by remember { mutableStateOf(false) }
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val focusManager = LocalFocusManager.current
    var isReportDialogShow by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val isNextButtonEnabled = state.selectedReason != null &&
            (state.selectedReason != ReportState.ReportReason.OTHER || textInput.isNotEmpty())

    if (isReportDialogShow) {
        PieceDialog(
            onDismissRequest = { isReportDialogShow = false },
            dialogTop = {
                PieceDialogDefaultTop(
                    title = "${userName}님을\n신고하시겠습니까 ?",
                    subText = "신고하면 되돌릴 수 없으니,\n신중한 신고 부탁드립니다.",
                )
            },
            dialogBottom = {
                PieceDialogBottom(
                    leftButtonText = "취소",
                    rightButtonText = stringResource(R.string.report),
                    onLeftButtonClick = { isReportDialogShow = false },
                    onRightButtonClick = { onReportClick() },
                )
            },
        )
    }

    if (state.isReportDone) {
        PieceDialog(
            onDismissRequest = {},
            dialogTop = {
                PieceDialogDefaultTop(
                    title = "${userName}님을 신고했습니다.",
                    subText = "신고된 내용은 신속하게 검토하여\n조치하겠습니다.",
                )
            },
            dialogBottom = {
                PieceSolidButton(
                    label = "홈으로",
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
        )

        if (isKeyboardVisible) {
            PieceRadio(
                selected = true,
                label = ReportState.ReportReason.OTHER.label,
                onSelectedChange = {
                    onReasonClick(ReportState.ReportReason.OTHER)
                },
                modifier = Modifier.padding(top = 6.dp, start = 20.dp, end = 20.dp),
            )
        } else {
            Text(
                text = "${userName}님을\n신고하시겠습니까?",
                textAlign = TextAlign.Start,
                style = PieceTheme.typography.headingLSB,
                color = PieceTheme.colors.black,
                modifier = Modifier.padding(
                    top = 20.dp,
                    bottom = 12.dp,
                    start = 20.dp,
                    end = 20.dp
                ),
            )

            Text(
                text = "신고된 내용은 신속하게 검토하여 조치하겠습니다.",
                textAlign = TextAlign.Start,
                style = PieceTheme.typography.bodySM,
                color = PieceTheme.colors.dark2,
                modifier = Modifier.padding(bottom = 40.dp, start = 20.dp, end = 20.dp),
            )

            ReportState.ReportReason.entries.forEach { reason ->
                PieceRadio(
                    selected = (reason == state.selectedReason),
                    label = reason.label,
                    onSelectedChange = {
                        onReasonClick(reason)
                        if (reason != ReportState.ReportReason.OTHER) {
                            textInput = ""
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                )
            }
        }

        if (state.selectedReason == ReportState.ReportReason.OTHER) {
            PieceTextInputLong(
                value = textInput,
                onValueChange = { input -> textInput = input },
                hint = stringResource(R.string.withdraw_other_reaseon_textfield_hint),
                limit = 100,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
                    .height(160.dp)
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .onFocusEvent { focusState ->
                        isKeyboardVisible = focusState.isFocused

                        if (focusState.isFocused) {
                            coroutineScope.launch {
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    },
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        PieceSolidButton(
            label = "다음",
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
