package com.puzzle.profile.graph.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.component.PieceSubBackTopBar
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.domain.model.profile.Contact
import com.puzzle.navigation.NavigationEvent
import com.puzzle.profile.graph.register.bottomsheet.ContactBottomSheet
import com.puzzle.profile.graph.register.bottomsheet.JobBottomSheet
import com.puzzle.profile.graph.register.bottomsheet.LocationBottomSheet
import com.puzzle.profile.graph.register.contract.RegisterProfileIntent
import com.puzzle.profile.graph.register.contract.RegisterProfileSideEffect
import com.puzzle.profile.graph.register.contract.RegisterProfileState
import com.puzzle.profile.graph.register.page.BasicProfilePage

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
        navigate = { viewModel.onIntent(RegisterProfileIntent.Navigate(it)) },
        onSaveClick = { viewModel.onIntent(RegisterProfileIntent.SaveClick) },
        onBackClick = { viewModel.onIntent(RegisterProfileIntent.BackClick) },
        onProfileImageChanged = { viewModel.onIntent(RegisterProfileIntent.UpdateProfileImage(it)) },
        onEditPhotoClick = { viewModel.onIntent(RegisterProfileIntent.EditPhotoClick(it)) },
        onDuplicationCheckClick = { viewModel.onIntent(RegisterProfileIntent.DuplicationCheckClick) },
        onNickNameChanged = { viewModel.onIntent(RegisterProfileIntent.UpdateNickName(it)) },
        onDescribeMySelfChanged = { viewModel.onIntent(RegisterProfileIntent.UpdateDescribeMySelf(it)) },
        onBirthdayChanged = { viewModel.onIntent(RegisterProfileIntent.UpdateBirthday(it)) },
        onHeightChanged = { viewModel.onIntent(RegisterProfileIntent.UpdateHeight(it)) },
        onWeightChanged = { viewModel.onIntent(RegisterProfileIntent.UpdateWeight(it)) },
        onSmokeStatusChanged = { viewModel.onIntent(RegisterProfileIntent.UpdateSmokeStatus(it)) },
        onSnsActivityChanged = { viewModel.onIntent(RegisterProfileIntent.UpdateSnsActivity(it)) },
        onAddContactClick = {
            viewModel.onIntent(
                RegisterProfileIntent.ShowBottomSheet
                {
                    ContactBottomSheet(
                        usingSnsPlatform = state.usingSnsPlatforms,
                        isEdit = false,
                        onButtonClicked = {
                            viewModel.onIntent(RegisterProfileIntent.AddContact(it))
                        },
                    )
                }
            )
        },
        onSnsPlatformChange = { idx ->
            viewModel.onIntent(
                RegisterProfileIntent.ShowBottomSheet
                {
                    ContactBottomSheet(
                        usingSnsPlatform = state.usingSnsPlatforms,
                        nowSnsPlatform = state.contacts[idx].snsPlatform,
                        isEdit = true,
                        onButtonClicked = {
                            viewModel.onIntent(
                                RegisterProfileIntent.UpdateContact(
                                    idx, state.contacts[idx].copy(snsPlatform = it)
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
                RegisterProfileIntent.ShowBottomSheet
                {
                    JobBottomSheet(
                        selectedJob = state.job,
                        updateSelectJob = {
                            viewModel.onIntent(
                                RegisterProfileIntent.UpdateJob(it)
                            )
                        },
                    )
                }
            )
        },
        onLocationDropDownClicked = {
            viewModel.onIntent(
                RegisterProfileIntent.ShowBottomSheet
                {
                    LocationBottomSheet(
                        selectedLocation = state.location,
                        updateSelectLocation = {
                            viewModel.onIntent(RegisterProfileIntent.UpdateRegion(it))
                        },
                    )
                }
            )
        },
        onDeleteContactClick = { viewModel.onIntent(RegisterProfileIntent.DeleteContact(it)) },
        onContactChange = { idx, contact ->
            viewModel.onIntent(RegisterProfileIntent.UpdateContact(idx, contact))
        },
    )
}

@Composable
private fun RegisterProfileScreen(
    state: RegisterProfileState,
    navigate: (NavigationEvent) -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onEditPhotoClick: (String) -> Unit,
    onProfileImageChanged: (String) -> Unit,
    onDuplicationCheckClick: () -> Unit,
    onNickNameChanged: (String) -> Unit,
    onDescribeMySelfChanged: (String) -> Unit,
    onBirthdayChanged: (String) -> Unit,
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager),
    ) {
        PieceSubBackTopBar(
            title = "",
            onBackClick = { navigate(NavigationEvent.NavigateUp) },
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 20.dp, end = 20.dp),
        ) {
            BasicProfilePage(
                state = state,
                onSaveClick = onSaveClick,
                onBackClick = onBackClick,
                onEditPhotoClick = onEditPhotoClick,
                onProfileImageChanged = onProfileImageChanged,
                onNickNameChanged = onNickNameChanged,
                onDescribeMySelfChanged = onDescribeMySelfChanged,
                onBirthdayChanged = onBirthdayChanged,
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
                modifier = Modifier,
            )
        }

        PieceSolidButton(
            label = stringResource(R.string.next),
            onClick = {
                onSaveClick()
//                navigate(TopLevelNavigateTo(MatchingRoute))
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
            navigate = {},
            onNickNameChanged = {},
            onProfileImageChanged = {},
            onDescribeMySelfChanged = {},
            onBirthdayChanged = {},
            onHeightChanged = {},
            onWeightChanged = {},
            onJobDropDownClicked = {},
            onLocationDropDownClicked = {},
            onSmokeStatusChanged = {},
            onSnsActivityChanged = {},
            onSaveClick = { },
            onBackClick = { },
            onEditPhotoClick = {},
            onDuplicationCheckClick = { },
            onSnsPlatformChange = {},
            onAddContactClick = {},
            onDeleteContactClick = {},
            onContactChange = { _, _ -> },
            modifier = Modifier.background(PieceTheme.colors.white)
        )
    }
}
