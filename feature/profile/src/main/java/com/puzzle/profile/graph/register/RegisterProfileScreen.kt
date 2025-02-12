package com.puzzle.profile.graph.register

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.common.ui.addFocusCleaner
import com.puzzle.common.ui.repeatOnStarted
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PiecePageIndicator
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.component.PieceSubBackTopBar
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.domain.model.profile.Contact
import com.puzzle.profile.graph.register.bottomsheet.ContactBottomSheet
import com.puzzle.profile.graph.register.bottomsheet.JobBottomSheet
import com.puzzle.profile.graph.register.bottomsheet.LocationBottomSheet
import com.puzzle.profile.graph.register.contract.RegisterProfileIntent
import com.puzzle.profile.graph.register.contract.RegisterProfileSideEffect
import com.puzzle.profile.graph.register.contract.RegisterProfileState
import com.puzzle.profile.graph.register.model.ValuePickRegisterRO
import com.puzzle.profile.graph.register.model.ValueTalkRegisterRO
import com.puzzle.profile.graph.register.page.BasicProfilePage
import com.puzzle.profile.graph.register.page.FinishPage
import com.puzzle.profile.graph.register.page.ValuePickPage
import com.puzzle.profile.graph.register.page.ValueTalkPage

@Composable
internal fun RegisterProfileRoute(
    viewModel: RegisterProfileViewModel = mavericksViewModel()
) {
    val state by viewModel.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel) {
        lifecycleOwner.repeatOnStarted {
            viewModel.sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is RegisterProfileSideEffect.Navigate -> viewModel.navigationHelper
                        .navigate(sideEffect.navigationEvent)
                }
            }
        }
    }

    RegisterProfileScreen(
        state = state,
        onSaveClick = { viewModel.onIntent(RegisterProfileIntent.OnSaveClick(it)) },
        onBackClick = { viewModel.onIntent(RegisterProfileIntent.OnBackClick) },
        onProfileImageChanged = {  viewModel.onIntent(RegisterProfileIntent.OnProfileImageChanged(it))},
        onDuplicationCheckClick = { viewModel.onIntent(RegisterProfileIntent.OnDuplicationCheckClick) },
        onNickNameChanged = { viewModel.onIntent(RegisterProfileIntent.OnNickNameChange(it)) },
        onDescribeMySelfChanged = {
            viewModel.onIntent(
                RegisterProfileIntent.OnSelfDescriptionChange(
                    it
                )
            )
        },
        onBirthdateChanged = { viewModel.onIntent(RegisterProfileIntent.OnBirthdateChange(it)) },
        onHeightChanged = { viewModel.onIntent(RegisterProfileIntent.OnHeightChange(it)) },
        onWeightChanged = { viewModel.onIntent(RegisterProfileIntent.OnWeightChange(it)) },
        onSmokeStatusChanged = { viewModel.onIntent(RegisterProfileIntent.OnIsSmokeClick(it)) },
        onSnsActivityChanged = { viewModel.onIntent(RegisterProfileIntent.OnSnsActivityClick(it)) },
        onAddContactClick = {
            viewModel.onIntent(
                RegisterProfileIntent.ShowBottomSheet {
                    ContactBottomSheet(
                        usingContactType = state.usingSnsPlatforms,
                        isEdit = false,
                        onButtonClicked = {
                            viewModel.onIntent(RegisterProfileIntent.OnAddContactClick(it))
                        },
                    )
                }
            )
        },
        onSnsPlatformChange = { idx ->
            viewModel.onIntent(
                RegisterProfileIntent.ShowBottomSheet {
                    ContactBottomSheet(
                        usingContactType = state.usingSnsPlatforms,
                        nowContactType = state.contacts[idx].type,
                        isEdit = true,
                        onButtonClicked = {
                            viewModel.onIntent(
                                RegisterProfileIntent.OnContactSelect(
                                    idx, state.contacts[idx].copy(type = it)
                                )
                            )

                            viewModel.onIntent(RegisterProfileIntent.HideBottomSheet)
                        },
                    )
                }
            )
        },
        onJobDropDownClicked = {
            viewModel.onIntent(
                RegisterProfileIntent.ShowBottomSheet {
                    JobBottomSheet(
                        selectedJob = state.job,
                        updateSelectJob = {
                            viewModel.onIntent(
                                RegisterProfileIntent.OnJobClick(it)
                            )
                        },
                    )
                }
            )
        },
        onLocationDropDownClicked = {
            viewModel.onIntent(
                RegisterProfileIntent.ShowBottomSheet {
                    LocationBottomSheet(
                        selectedLocation = state.location,
                        updateSelectLocation = {
                            viewModel.onIntent(RegisterProfileIntent.OnRegionClick(it))
                        },
                    )
                }
            )
        },
        onDeleteContactClick = { viewModel.onIntent(RegisterProfileIntent.OnDeleteContactClick(it)) },
        onContactChange = { idx, contact ->
            viewModel.onIntent(RegisterProfileIntent.OnContactSelect(idx, contact))
        },
        onHomeClick = {

        },
    )
}

@Composable
private fun RegisterProfileScreen(
    state: RegisterProfileState,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onSaveClick: (RegisterProfileState) -> Unit,
    onProfileImageChanged: (String) -> Unit,
    onDuplicationCheckClick: () -> Unit,
    onNickNameChanged: (String) -> Unit,
    onDescribeMySelfChanged: (String) -> Unit,
    onBirthdateChanged: (String) -> Unit,
    onHeightChanged: (String) -> Unit,
    onWeightChanged: (String) -> Unit,
    onJobDropDownClicked: () -> Unit,
    onLocationDropDownClicked: () -> Unit,
    onSmokeStatusChanged: (Boolean) -> Unit,
    onSnsActivityChanged: (Boolean) -> Unit,
    onSnsPlatformChange: (Int) -> Unit,
    onAddContactClick: () -> Unit,
    onDeleteContactClick: (Int) -> Unit,
    onContactChange: (Int, Contact) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    var valueTalks: List<ValueTalkRegisterRO> by remember(state.valueTalks) { mutableStateOf(state.valueTalks) }
    var valuePicks: List<ValuePickRegisterRO> by remember(state.valuePicks) { mutableStateOf(state.valuePicks) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager),
    ) {
        PieceSubBackTopBar(
            title = "",
            onBackClick = onBackClick,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
        )

        if (state.currentPage != RegisterProfileState.Page.FINISH) {
            PiecePageIndicator(
                currentStep = state.currentPage.ordinal + 1,
                totalSteps = RegisterProfileState.Page.entries.size,
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            AnimatedContent(
                targetState = state.currentPage,
                transitionSpec = {
                    fadeIn(tween(700)) togetherWith fadeOut(tween(700))
                },
                modifier = Modifier.fillMaxSize(),
                label = "",
            ) {
                when (it) {
                    RegisterProfileState.Page.BASIC_PROFILE ->
                        BasicProfilePage(
                            state = state,
                            onProfileImageChanged = onProfileImageChanged,
                            onNickNameChanged = onNickNameChanged,
                            onDescribeMySelfChanged = onDescribeMySelfChanged,
                            onBirthdateChanged = onBirthdateChanged,
                            onLocationDropDownClicked = onLocationDropDownClicked,
                            onHeightChanged = onHeightChanged,
                            onWeightChanged = onWeightChanged,
                            onJobDropDownClicked = onJobDropDownClicked,
                            onSmokeStatusChanged = onSmokeStatusChanged,
                            onSnsActivityChanged = onSnsActivityChanged,
                            onDuplicationCheckClick = onDuplicationCheckClick,
                            onContactChange = onContactChange,
                            onSnsPlatformChange = onSnsPlatformChange,
                            onAddContactClick = onAddContactClick,
                            onDeleteClick = onDeleteContactClick,
                            modifier = Modifier.padding(horizontal = 20.dp),
                        )

                    RegisterProfileState.Page.VALUE_TALK ->
                        ValueTalkPage(
                            valueTalks = valueTalks,
                            onValueTalkContentChange = { updatedValueTalks ->
                                valueTalks = updatedValueTalks
                            },
                            modifier = Modifier.fillMaxSize(),
                        )

                    RegisterProfileState.Page.VALUE_PICK ->
                        ValuePickPage(
                            valuePicks = valuePicks,
                            onValuePickContentChange = { updatedValuePicks ->
                                valuePicks = updatedValuePicks
                            },
                            modifier = Modifier.fillMaxSize(),
                        )

                    RegisterProfileState.Page.FINISH ->
                        FinishPage(
                            onHomeClick = onHomeClick,
                            modifier = Modifier.fillMaxSize(),
                        )
                }
            }
        }

        PieceSolidButton(
            label = stringResource(R.string.next),
            onClick = {
                onSaveClick(
                    state.copy(
                        valueTalks = valueTalks,
                        valuePicks = valuePicks,
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 10.dp),
        )
    }
}

@Preview
@Composable
private fun PreviewRegisterProfileScreen() {
    PieceTheme {
        RegisterProfileScreen(
            state = RegisterProfileState(),
            onNickNameChanged = {},
            onProfileImageChanged = {},
            onDescribeMySelfChanged = {},
            onBirthdateChanged = {},
            onHeightChanged = {},
            onWeightChanged = {},
            onJobDropDownClicked = {},
            onLocationDropDownClicked = {},
            onSmokeStatusChanged = {},
            onSnsActivityChanged = {},
            onSaveClick = { },
            onBackClick = { },
            onDuplicationCheckClick = { },
            onSnsPlatformChange = {},
            onAddContactClick = {},
            onDeleteContactClick = {},
            onContactChange = { _, _ -> },
            modifier = Modifier.background(PieceTheme.colors.white),
            onHomeClick = {}
        )
    }
}
