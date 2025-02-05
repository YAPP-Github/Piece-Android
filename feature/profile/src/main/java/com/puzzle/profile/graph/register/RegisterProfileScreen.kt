package com.puzzle.profile.graph.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.common.ui.addFocusCleaner
import com.puzzle.common.ui.repeatOnStarted
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceChip
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.component.PieceSubBackTopBar
import com.puzzle.designsystem.component.PieceTextInputDefault
import com.puzzle.designsystem.component.PieceTextInputDropDown
import com.puzzle.designsystem.component.PieceTextInputSnsDropDown
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.SnsPlatform
import com.puzzle.navigation.MatchingGraphDest.MatchingRoute
import com.puzzle.navigation.NavigationEvent
import com.puzzle.navigation.NavigationEvent.TopLevelNavigateTo
import com.puzzle.profile.graph.register.bottomsheet.ContactBottomSheet
import com.puzzle.profile.graph.register.bottomsheet.JobBottomSheet
import com.puzzle.profile.graph.register.bottomsheet.LocationBottomSheet
import com.puzzle.profile.graph.register.contract.RegisterProfileIntent
import com.puzzle.profile.graph.register.contract.RegisterProfileSideEffect
import com.puzzle.profile.graph.register.contract.RegisterProfileState

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
        onNickNameChanged = { viewModel.onIntent(RegisterProfileIntent.UpdateNickName(it)) },
        onDescribeMySelfChanged = { viewModel.onIntent(RegisterProfileIntent.UpdateDescribeMySelf(it)) },
        onBirthdayChanged = { viewModel.onIntent(RegisterProfileIntent.UpdateBirthday(it)) },
        onHeightChanged = { viewModel.onIntent(RegisterProfileIntent.UpdateHeight(it)) },
        onWeightChanged = { viewModel.onIntent(RegisterProfileIntent.UpdateWeight(it)) },
        onSmokeStatusChanged = { viewModel.onIntent(RegisterProfileIntent.UpdateSmokeStatus(it)) },
        onSnsActivityChanged = { viewModel.onIntent(RegisterProfileIntent.UpdateSnsActivity(it)) },
        onAddContactsClicked = {
            viewModel.onIntent(
                RegisterProfileIntent.ShowBottomSheet(
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
            )
        },
        onUpdateSnsPlatformClicked = { idx ->
            viewModel.onIntent(
                RegisterProfileIntent.ShowBottomSheet(
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
            )
        },
        onJobDropDownClicked = {
            viewModel.onIntent(
                RegisterProfileIntent.ShowBottomSheet(
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
            )
        },
        onLocationDropDownClicked = {
            viewModel.onIntent(
                RegisterProfileIntent.ShowBottomSheet(
                    {
                        LocationBottomSheet(
                            selectedLocation = state.location,
                            updateSelectLocation = {
                                viewModel.onIntent(RegisterProfileIntent.UpdateRegion(it))
                            },
                        )
                    }
                )
            )
        },
        onDeleteContact = { viewModel.onIntent(RegisterProfileIntent.DeleteContact(it)) },
        onUpdateContact = { idx, contact ->
            viewModel.onIntent(RegisterProfileIntent.UpdateContact(idx, contact))
        },
    )
}

@Composable
private fun RegisterProfileScreen(
    state: RegisterProfileState,
    navigate: (NavigationEvent) -> Unit,
    onNickNameChanged: (String) -> Unit,
    onDescribeMySelfChanged: (String) -> Unit,
    onBirthdayChanged: (String) -> Unit,
    onHeightChanged: (String) -> Unit,
    onWeightChanged: (String) -> Unit,
    onJobDropDownClicked: () -> Unit,
    onLocationDropDownClicked: () -> Unit,
    onSmokeStatusChanged: (Boolean) -> Unit,
    onSnsActivityChanged: (Boolean) -> Unit,
    onAddContactsClicked: () -> Unit,
    onUpdateSnsPlatformClicked: (Int) -> Unit,
    onDeleteContact: (Int) -> Unit,
    onUpdateContact: (Int, Contact) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    var isNicknameFocus by remember { mutableStateOf(false) }
    var isDescriptionFocus by remember { mutableStateOf(false) }
    var isBirthdateFocus by remember { mutableStateOf(false) }
    var isWeightFocus by remember { mutableStateOf(false) }
    var isHeightFocus by remember { mutableStateOf(false) }
    var isJobFocus by remember { mutableStateOf(false) }
    var isContactFocus by remember { mutableStateOf(false) }
    var previousContactSize by remember { mutableStateOf(1) }

    LaunchedEffect(state.contacts) {
        if (previousContactSize < state.contacts.size) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
        previousContactSize = state.contacts.size

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager),
    ) {
        PieceSubBackTopBar(
            title = "",
            onBackClick = { navigate(NavigationEvent.NavigateUp) },
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 14.dp)
                .align(Alignment.TopCenter),
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxSize()
                .padding(top = 64.dp, start = 20.dp, end = 20.dp, bottom = 74.dp)
                .verticalScroll(scrollState),
        ) {
            Text(
                text = "간단한 정보로\n당신을 표현하세요",
                style = PieceTheme.typography.headingLSB,
                color = PieceTheme.colors.black,
                modifier = Modifier.padding(top = 20.dp),
            )

            Text(
                text = "작성 후에도 언제든 수정 가능하니,\n편안하게 작성해 주세요.",
                style = PieceTheme.typography.bodySM,
                color = PieceTheme.colors.dark3,
                modifier = Modifier.padding(top = 12.dp),
            )

            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 40.dp)
                    .size(120.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_profile_default),
                    contentDescription = null,
                )

                Image(
                    painter = painterResource(R.drawable.ic_plus_circle),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }

            SectionTitle(title = "닉네임")
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            ) {
                PieceTextInputDefault(
                    value = state.nickName,
                    hint = "6자 이하로 작성해주세요",
                    keyboardType = KeyboardType.Text,
                    onValueChange = onNickNameChanged,
                    rightComponent = {
                        if (isNicknameFocus && state.nickName.isNotEmpty()) {
                            Image(
                                painter = painterResource(R.drawable.ic_delete_circle),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .size(20.dp)
                                    .clickable { onNickNameChanged("") }
                            )
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged { isNicknameFocus = it.isFocused },
                )

                PieceSolidButton(
                    label = "중복검사",
                    onClick = { },
                    enabled = state.nickName.isNotEmpty(),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            AnimatedVisibility(visible = isNicknameFocus) {
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = "닉네임 중복 검사를 진행해주세요.",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = PieceTheme.typography.bodySM,
                        color = PieceTheme.colors.dark3,
                        modifier = Modifier.weight(1f),
                    )

                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = PieceTheme.colors.primaryDefault)) {
                                append(state.nickName.length.toString())
                            }
                            append("/6")
                        },
                        maxLines = 1,
                        style = PieceTheme.typography.bodySM,
                        color = PieceTheme.colors.dark3,
                        modifier = Modifier.padding(start = 5.dp),
                    )
                }
            }

            SectionTitle(title = "나를 표현하는 한 마디")
            PieceTextInputDefault(
                value = state.description,
                hint = "수식어 형태로 작성해 주세요",
                keyboardType = KeyboardType.Text,
                onValueChange = onDescribeMySelfChanged,
                rightComponent = {
                    if (isDescriptionFocus && state.description.isNotEmpty()) {
                        Image(
                            painter = painterResource(R.drawable.ic_delete_circle),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(20.dp)
                                .clickable { onDescribeMySelfChanged("") },
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .onFocusChanged { isDescriptionFocus = it.isFocused },
            )
            AnimatedVisibility(visible = isDescriptionFocus) {
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = PieceTheme.typography.bodySM,
                        color = PieceTheme.colors.dark3,
                        modifier = Modifier.weight(1f),
                    )

                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = PieceTheme.colors.primaryDefault)) {
                                append(state.description.length.toString())
                            }
                            append("/20")
                        },
                        maxLines = 1,
                        style = PieceTheme.typography.bodySM,
                        color = PieceTheme.colors.dark3,
                        modifier = Modifier.padding(start = 5.dp),
                    )
                }
            }

            SectionTitle(title = "생년월일")
            PieceTextInputDefault(
                value = state.birthdate,
                hint = "6자리(YYMMDD) 형식으로 입력해 주세요",
                keyboardType = KeyboardType.Number,
                onValueChange = onBirthdayChanged,
                rightComponent = {
                    if (isBirthdateFocus && state.birthdate.isNotEmpty()) {
                        Image(
                            painter = painterResource(R.drawable.ic_delete_circle),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .onFocusChanged { isBirthdateFocus = it.isFocused },
            )

            SectionTitle(title = "활동 지역")
            PieceTextInputDropDown(
                value = state.location,
                onDropDownClick = onLocationDropDownClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            SectionTitle(title = "키")
            PieceTextInputDefault(
                value = state.height,
                keyboardType = KeyboardType.Number,
                onValueChange = { height ->
                    if (height.isDigitsOnly()) {
                        onHeightChanged(height)
                    }
                },
                rightComponent = {
                    Text(
                        text = "cm",
                        style = PieceTheme.typography.bodySM,
                        color = PieceTheme.colors.dark3,
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .onFocusChanged { isHeightFocus = it.isFocused },
            )
            AnimatedVisibility(visible = isHeightFocus) {
                Text(
                    text = if (state.height.isNotEmpty()) "숫자가 정확한 지 확인해 주세요" else "",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = PieceTheme.typography.bodySM,
                    color = PieceTheme.colors.error,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                )
            }

            SectionTitle(title = "몸무게")
            PieceTextInputDefault(
                value = state.weight,
                keyboardType = KeyboardType.Number,
                onValueChange = { weight ->
                    if (weight.isDigitsOnly()) {
                        onWeightChanged(weight)
                    }
                },
                rightComponent = {
                    Text(
                        text = "kg",
                        style = PieceTheme.typography.bodySM,
                        color = PieceTheme.colors.dark3,
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .onFocusChanged { isWeightFocus = it.isFocused },
            )
            AnimatedVisibility(visible = isWeightFocus) {
                Text(
                    text = if (state.weight.isNotEmpty()) "숫자가 정확한 지 확인해 주세요" else "",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = PieceTheme.typography.bodySM,
                    color = PieceTheme.colors.error,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                )
            }

            SectionTitle(title = "직업")
            PieceTextInputDropDown(
                value = state.job,
                onDropDownClick = onJobDropDownClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .onFocusChanged { isJobFocus = it.isFocused },
            )

            SectionTitle(title = "흡연")
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            ) {
                PieceChip(
                    label = "흡연",
                    selected = state.isSmoke == true,
                    onChipClicked = { onSmokeStatusChanged(true) },
                    modifier = Modifier.weight(1f),
                )

                PieceChip(
                    label = "비흡연",
                    selected = state.isSmoke == false,
                    onChipClicked = { onSmokeStatusChanged(false) },
                    modifier = Modifier.weight(1f),
                )
            }

            SectionTitle(title = "SNS 활동")
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            ) {
                PieceChip(
                    label = "활동",
                    selected = state.isSnsActive == true,
                    onChipClicked = { onSnsActivityChanged(true) },
                    modifier = Modifier.weight(1f),
                )

                PieceChip(
                    label = "은둔",
                    selected = state.isSnsActive == false,
                    onChipClicked = { onSnsActivityChanged(false) },
                    modifier = Modifier.weight(1f),
                )
            }

            SectionTitle(title = "연락처")
            state.contacts.forEachIndexed { idx, contact ->
                val image = when (contact.snsPlatform) {
                    SnsPlatform.KAKAO_TALK_ID -> R.drawable.ic_sns_kakao
                    SnsPlatform.OPEN_CHAT_URL -> R.drawable.ic_sns_openchatting
                    SnsPlatform.INSTAGRAM_ID -> R.drawable.ic_sns_instagram
                    SnsPlatform.PHONE_NUMBER -> R.drawable.ic_sns_call
                    else -> R.drawable.ic_delete_circle // 임시
                }

                PieceTextInputSnsDropDown(
                    value = contact.content,
                    image = image,
                    onValueChange = { onUpdateContact(idx, contact.copy(content = it)) },
                    onDropDownClick = { onUpdateSnsPlatformClicked(idx) },
                    onDeleteClick = { onDeleteContact(idx) },
                    isMandatory = (idx == 0),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .onFocusChanged { isContactFocus = it.isFocused },
                )
            }

            AnimatedVisibility(
                visible = state.contacts.size < 4,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                        .clickable { onAddContactsClicked() },
                ) {
                    Text(
                        text = "연락처 추가하기",
                        style = PieceTheme.typography.bodyMSB,
                        color = PieceTheme.colors.primaryDefault,
                    )

                    Image(
                        painter = painterResource(R.drawable.ic_plus),
                        contentDescription = null,
                    )
                }
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
            )
        }


        PieceSolidButton(
            label = stringResource(R.string.next),
            onClick = { navigate(TopLevelNavigateTo(MatchingRoute)) },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 10.dp),
        )
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = PieceTheme.typography.bodySM,
        color = PieceTheme.colors.dark3,
        modifier = Modifier.padding(top = 32.dp),
    )
}

@Preview(heightDp = 1600)
@Composable
private fun PreviewRegisterProfileScreen() {
    PieceTheme {
        RegisterProfileScreen(
            state = RegisterProfileState(),
            navigate = {},
            onNickNameChanged = {},
            onDescribeMySelfChanged = {},
            onBirthdayChanged = {},
            onHeightChanged = {},
            onWeightChanged = {},
            onJobDropDownClicked = {},
            onLocationDropDownClicked = {},
            onSmokeStatusChanged = {},
            onSnsActivityChanged = {},
            onAddContactsClicked = {},
            onDeleteContact = {},
            onUpdateContact = { _, _ -> },
            onUpdateSnsPlatformClicked = {},
        )
    }
}
