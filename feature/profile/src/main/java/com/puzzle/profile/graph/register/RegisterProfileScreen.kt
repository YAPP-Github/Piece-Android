package com.puzzle.profile.graph.register

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.common.ui.ANIMATION_DURATION
import com.puzzle.common.ui.addFocusCleaner
import com.puzzle.common.ui.blur
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceDialog
import com.puzzle.designsystem.component.PieceDialogBottom
import com.puzzle.designsystem.component.PieceDialogIconTop
import com.puzzle.designsystem.component.PiecePageIndicator
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.component.PieceSubBackTopBar
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.user.UserRole
import com.puzzle.profile.graph.register.bottomsheet.ContactBottomSheet
import com.puzzle.profile.graph.register.bottomsheet.JobBottomSheet
import com.puzzle.profile.graph.register.bottomsheet.LocationBottomSheet
import com.puzzle.profile.graph.register.contract.RegisterProfileIntent
import com.puzzle.profile.graph.register.contract.RegisterProfileState
import com.puzzle.profile.graph.register.model.ValuePickRegisterRO
import com.puzzle.profile.graph.register.model.ValueTalkRegisterRO
import com.puzzle.profile.graph.register.page.BasicProfilePage
import com.puzzle.profile.graph.register.page.FinishPage
import com.puzzle.profile.graph.register.page.SummationPage
import com.puzzle.profile.graph.register.page.ValuePickPage
import com.puzzle.profile.graph.register.page.ValueTalkPage

@Composable
internal fun RegisterProfileRoute(
    viewModel: RegisterProfileViewModel = mavericksViewModel()
) {
    val state by viewModel.collectAsState()

    RegisterProfileScreen(
        state = state,
        onSaveClick = { viewModel.onIntent(RegisterProfileIntent.OnSaveClick(it)) },
        onBackClick = { viewModel.onIntent(RegisterProfileIntent.OnBackClick) },
        onProfileImageChanged = { viewModel.onIntent(RegisterProfileIntent.OnProfileImageChanged(it)) },
        onDuplicationCheckClick = { viewModel.onIntent(RegisterProfileIntent.OnDuplicationCheckClick) },
        onNickNameChanged = { viewModel.onIntent(RegisterProfileIntent.OnNickNameChange(it)) },
        onDescribeMySelfChanged = {
            viewModel.onIntent(RegisterProfileIntent.OnSelfDescriptionChange(it))
        },
        onBirthdateChanged = { viewModel.onIntent(RegisterProfileIntent.OnBirthdateChange(it)) },
        onHeightChanged = { viewModel.onIntent(RegisterProfileIntent.OnHeightChange(it)) },
        onWeightChanged = { viewModel.onIntent(RegisterProfileIntent.OnWeightChange(it)) },
        onSmokingStatusChanged = { viewModel.onIntent(RegisterProfileIntent.OnIsSmokeClick(it)) },
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
        onHomeClick = { viewModel.onIntent(RegisterProfileIntent.OnHomeClick) },
        onCheckMyProfileClick = {
            viewModel.onIntent(RegisterProfileIntent.OnCheckMyProfileClick)
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
    onSmokingStatusChanged: (Boolean) -> Unit,
    onSnsActivityChanged: (Boolean) -> Unit,
    onSnsPlatformChange: (Int) -> Unit,
    onAddContactClick: () -> Unit,
    onDeleteContactClick: (Int) -> Unit,
    onContactChange: (Int, Contact) -> Unit,
    onCheckMyProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    var valueTalks: List<ValueTalkRegisterRO> by remember(state.valueTalks) { mutableStateOf(state.valueTalks) }
    var valuePicks: List<ValuePickRegisterRO> by remember(state.valuePicks) { mutableStateOf(state.valuePicks) }
    var showDialog by remember { mutableStateOf(false) }

    if (state.currentPage == RegisterProfileState.Page.BASIC_PROFILE) {
        BackHandler { showDialog = true }
    }

    if (showDialog) {
        PieceDialog(
            onDismissRequest = { showDialog = false },
            dialogTop = {
                PieceDialogIconTop(
                    iconId = R.drawable.ic_notice,
                    title = stringResource(R.string.profile_warning_title),
                    subText = stringResource(R.string.profile_warning_description),
                )
            },
            dialogBottom = {
                PieceDialogBottom(
                    leftButtonText = stringResource(R.string.back),
                    rightButtonText = stringResource(R.string.profile_continue),
                    onLeftButtonClick = {
                        showDialog = false
                        onBackClick()
                    },
                    onRightButtonClick = { showDialog = false },
                )
            },
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager)
            .blur(isBlur = showDialog),
    ) {
        PieceSubBackTopBar(
            title = "",
            onBackClick = onBackClick,
            isShowBackButton = state.currentPage !in setOf(
                RegisterProfileState.Page.FINISH,
                RegisterProfileState.Page.SUMMATION,
                RegisterProfileState.Page.BASIC_PROFILE
            ),
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        if (state.currentPage !in setOf(
                RegisterProfileState.Page.FINISH,
                RegisterProfileState.Page.SUMMATION,
            )
        ) {
            PiecePageIndicator(
                currentStep = state.currentPage.ordinal + 1,
                totalSteps = RegisterProfileState.Page.entries.size - 1,
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            AnimatedContent(
                targetState = state.currentPage,
                transitionSpec = {
                    fadeIn(tween(ANIMATION_DURATION)) togetherWith fadeOut(tween(ANIMATION_DURATION))
                },
                modifier = Modifier.fillMaxSize(),
                label = "",
            ) {
                when (it) {
                    RegisterProfileState.Page.BASIC_PROFILE -> BasicProfilePage(
                        state = state,
                        onProfileImageChanged = onProfileImageChanged,
                        onNickNameChanged = onNickNameChanged,
                        onDescribeMySelfChanged = onDescribeMySelfChanged,
                        onBirthdateChanged = onBirthdateChanged,
                        onLocationDropDownClicked = onLocationDropDownClicked,
                        onHeightChanged = onHeightChanged,
                        onWeightChanged = onWeightChanged,
                        onJobDropDownClicked = onJobDropDownClicked,
                        onSmokingStatusChanged = onSmokingStatusChanged,
                        onSnsActivityChanged = onSnsActivityChanged,
                        onDuplicationCheckClick = onDuplicationCheckClick,
                        onContactChange = onContactChange,
                        onSnsPlatformChange = onSnsPlatformChange,
                        onAddContactClick = onAddContactClick,
                        onDeleteClick = onDeleteContactClick,
                    )

                    RegisterProfileState.Page.VALUE_TALK -> ValueTalkPage(
                        valueTalks = valueTalks,
                        onValueTalkContentChange = { updatedValueTalks ->
                            valueTalks = updatedValueTalks
                        },
                    )

                    RegisterProfileState.Page.VALUE_PICK -> ValuePickPage(
                        valuePicks = valuePicks,
                        onValuePickContentChange = { updatedValuePicks ->
                            valuePicks = updatedValuePicks
                        },
                    )

                    RegisterProfileState.Page.SUMMATION -> SummationPage()
                    RegisterProfileState.Page.FINISH -> FinishPage(
                        userRole = state.userRole,
                        onHomeClick = onHomeClick
                    )
                }
            }
        }

        if (state.currentPage != RegisterProfileState.Page.SUMMATION) {
            PieceSolidButton(
                label = when (state.currentPage) {
                    RegisterProfileState.Page.FINISH -> stringResource(R.string.check_my_profile)
                    RegisterProfileState.Page.VALUE_PICK ->
                        if (state.userRole == UserRole.PENDING) stringResource(R.string.edit_profile)
                        else stringResource(R.string.generate_profile)

                    else -> stringResource(R.string.next)
                },
                onClick = {
                    when (state.currentPage) {
                        RegisterProfileState.Page.FINISH -> onCheckMyProfileClick()
                        else -> onSaveClick(
                            state.copy(
                                valueTalks = valueTalks,
                                valuePicks = valuePicks,
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 10.dp),
            )
        }
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
            onSmokingStatusChanged = {},
            onSnsActivityChanged = {},
            onSaveClick = { },
            onBackClick = { },
            onDuplicationCheckClick = { },
            onSnsPlatformChange = {},
            onAddContactClick = {},
            onDeleteContactClick = {},
            onContactChange = { _, _ -> },
            modifier = Modifier.background(PieceTheme.colors.white),
            onHomeClick = {},
            onCheckMyProfileClick = {},
        )
    }
}
