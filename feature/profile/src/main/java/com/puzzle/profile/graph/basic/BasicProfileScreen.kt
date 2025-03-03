package com.puzzle.profile.graph.basic

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
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
import coil3.compose.AsyncImage
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.common.ui.addFocusCleaner
import com.puzzle.common.ui.throttledClickable
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceChip
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.component.PieceSubTopBar
import com.puzzle.designsystem.component.PieceTextInputDefault
import com.puzzle.designsystem.component.PieceTextInputDropDown
import com.puzzle.designsystem.component.PieceTextInputSnsDropDown
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.ContactType
import com.puzzle.profile.graph.basic.contract.BasicProfileIntent
import com.puzzle.profile.graph.basic.contract.BasicProfileState
import com.puzzle.profile.graph.basic.contract.InputState
import com.puzzle.profile.graph.basic.contract.NickNameGuideMessage
import com.puzzle.profile.graph.basic.contract.ScreenState
import com.puzzle.profile.graph.register.bottomsheet.ContactBottomSheet
import com.puzzle.profile.graph.register.bottomsheet.JobBottomSheet
import com.puzzle.profile.graph.register.bottomsheet.LocationBottomSheet

@Composable
internal fun BasicProfileRoute(
    viewModel: BasicProfileViewModel = mavericksViewModel()
) {
    val state by viewModel.collectAsState()

    BasicProfileScreen(
        state = state,
        onSaveClick = { viewModel.onIntent(BasicProfileIntent.SaveBasicProfile) },
        onBackClick = { viewModel.onIntent(BasicProfileIntent.OnBackClick) },
        onNickNameChanged = { viewModel.onIntent(BasicProfileIntent.UpdateNickName(it)) },
        onProfileImageChanged = { viewModel.onIntent(BasicProfileIntent.UpdateProfileImage(it)) },
        onDuplicationCheckClick = { viewModel.onIntent(BasicProfileIntent.CheckNickNameDuplication) },
        onDescribeMySelfChanged = { viewModel.onIntent(BasicProfileIntent.UpdateDescribeMySelf(it)) },
        onBirthdayChanged = { viewModel.onIntent(BasicProfileIntent.UpdateBirthday(it)) },
        onHeightChanged = { viewModel.onIntent(BasicProfileIntent.UpdateHeight(it)) },
        onWeightChanged = { viewModel.onIntent(BasicProfileIntent.UpdateWeight(it)) },
        onSmokingStatusChanged = { viewModel.onIntent(BasicProfileIntent.UpdateSmokingStatus(it)) },
        onSnsActivityChanged = { viewModel.onIntent(BasicProfileIntent.UpdateSnsActivity(it)) },
        onAddContactClick = {
            viewModel.onIntent(
                BasicProfileIntent.ShowBottomSheet {
                    ContactBottomSheet(
                        usingContactType = state.usingSnsPlatforms,
                        isEdit = false,
                        onButtonClicked = {
                            viewModel.onIntent(BasicProfileIntent.AddContact(it))
                        },
                    )
                }
            )
        },
        onSnsPlatformChange = { idx ->
            viewModel.onIntent(
                BasicProfileIntent.ShowBottomSheet {
                    ContactBottomSheet(
                        usingContactType = state.usingSnsPlatforms,
                        nowContactType = state.contacts[idx].type,
                        isEdit = true,
                        onButtonClicked = {
                            viewModel.onIntent(
                                BasicProfileIntent.UpdateContact(
                                    idx, state.contacts[idx].copy(type = it)
                                )
                            )

                            viewModel.onIntent(BasicProfileIntent.HideBottomSheet)
                        },
                    )
                }
            )
        },
        onJobDropDownClicked = {
            viewModel.onIntent(
                BasicProfileIntent.ShowBottomSheet {
                    JobBottomSheet(
                        selectedJob = state.job,
                        updateSelectJob = {
                            viewModel.onIntent(
                                BasicProfileIntent.UpdateJob(it)
                            )
                        },
                    )
                }
            )
        },
        onLocationDropDownClicked = {
            viewModel.onIntent(
                BasicProfileIntent.ShowBottomSheet {
                    LocationBottomSheet(
                        selectedLocation = state.location,
                        updateSelectLocation = {
                            viewModel.onIntent(BasicProfileIntent.UpdateRegion(it))
                        },
                    )
                }
            )
        },
        onDeleteClick = { viewModel.onIntent(BasicProfileIntent.DeleteContact(it)) },
        onContactChange = { idx, contact ->
            viewModel.onIntent(BasicProfileIntent.UpdateContact(idx, contact))
        },
    )
}

@Composable
private fun BasicProfileScreen(
    state: BasicProfileState,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
    onNickNameChanged: (String) -> Unit,
    onProfileImageChanged: (String) -> Unit,
    onDuplicationCheckClick: () -> Unit,
    onDescribeMySelfChanged: (String) -> Unit,
    onBirthdayChanged: (String) -> Unit,
    onLocationDropDownClicked: () -> Unit,
    onHeightChanged: (String) -> Unit,
    onWeightChanged: (String) -> Unit,
    onJobDropDownClicked: () -> Unit,
    onSmokingStatusChanged: (Boolean) -> Unit,
    onSnsActivityChanged: (Boolean) -> Unit,
    onContactChange: (Int, Contact) -> Unit,
    onSnsPlatformChange: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onAddContactClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val isSaveButtonEnabled = state.profileScreenState == ScreenState.EDITING ||
            state.profileScreenState == ScreenState.SAVE_FAILED

    Column(
        modifier = modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager)
            .background(PieceTheme.colors.white)
            .padding(horizontal = 20.dp)
            .padding(top = 24.dp, bottom = 60.dp),
    ) {
        PieceSubTopBar(
            title = "기본 정보 수정",
            onNavigationClick = onBackClick,
            rightComponent = {
                Text(
                    text = stringResource(R.string.value_pick_profile_topbar_save),
                    style = PieceTheme.typography.bodyMM,
                    color = if (isSaveButtonEnabled) {
                        PieceTheme.colors.primaryDefault
                    } else {
                        PieceTheme.colors.dark3
                    },
                    modifier = Modifier.throttledClickable(
                        throttleTime = 2000L,
                        enabled = isSaveButtonEnabled
                    ) { onSaveClick() },
                )
            },
        )

        Column(modifier = Modifier.verticalScroll(scrollState)) {
            PhotoContent(
                imageUrl = state.imageUrl,
                imageUrlInputState = state.imageUrlInputState,
                onProfileImageChanged = onProfileImageChanged,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 40.dp)
                    .size(120.dp),
            )

            NickNameContent(
                nickName = state.nickname,
                nickNameGuideMessage = state.nickNameGuideMessage,
                isCheckingButtonAvailable = state.isCheckingButtonEnabled,
                onNickNameChanged = onNickNameChanged,
                onDuplicationCheckClick = onDuplicationCheckClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            )

            SelfDescriptionContent(
                description = state.description,
                descriptionInputState = state.descriptionInputState,
                onDescribeMySelfChanged = onDescribeMySelfChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            )

            BirthdateContent(
                birthdate = state.birthdate,
                birthdateInputState = state.birthdateInputState,
                onBirthdayChanged = onBirthdayChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            )

            LocationContent(
                location = state.location,
                locationInputState = state.locationInputState,
                onLocationDropDownClicked = onLocationDropDownClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            HeightContent(
                height = state.height,
                heightInputState = state.heightInputState,
                onHeightChanged = onHeightChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            WeightContent(
                weight = state.weight,
                weightInputState = state.weightInputState,
                onWeightChanged = onWeightChanged,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            )

            JobContent(
                job = state.job,
                jobInputState = state.jobInputState,
                onJobDropDownClicked = onJobDropDownClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            SmokeContent(
                isSmoke = state.isSmoke,
                onSmokingStatusChanged = onSmokingStatusChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            )

            SnsActivityContent(
                isSnsActive = state.isSnsActive,
                onSnsActivityChanged = onSnsActivityChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            )

            SnsPlatformContent(
                contacts = state.contacts,
                screenState = state.profileScreenState,
                onContactChange = onContactChange,
                onSnsPlatformChange = onSnsPlatformChange,
                onDeleteClick = onDeleteClick,
                onAddContactClick = onAddContactClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun ColumnScope.SnsPlatformContent(
    contacts: List<Contact>,
    screenState: ScreenState,
    onContactChange: (Int, Contact) -> Unit,
    onSnsPlatformChange: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onAddContactClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isSaveFailed: Boolean =
        screenState == ScreenState.SAVE_FAILED && contacts.isEmpty()

    SectionTitle(title = "연락처")

    contacts.forEachIndexed { idx, contact ->
        val image = when (contact.type) {
            ContactType.KAKAO_TALK_ID -> R.drawable.ic_sns_kakao
            ContactType.OPEN_CHAT_URL -> R.drawable.ic_sns_openchatting
            ContactType.INSTAGRAM_ID -> R.drawable.ic_sns_instagram
            ContactType.PHONE_NUMBER -> R.drawable.ic_sns_call
            else -> R.drawable.ic_delete_circle // 임시
        }

        PieceTextInputSnsDropDown(
            value = contact.content,
            image = image,
            onValueChange = { onContactChange(idx, contact.copy(content = it)) },
            keyboardType = if (contact.type == ContactType.PHONE_NUMBER) KeyboardType.Phone
            else KeyboardType.Text,
            onDropDownClick = { onSnsPlatformChange(idx) },
            onDeleteClick = { onDeleteClick(idx) },
            isMandatory = (idx == 0),
            modifier = modifier,
        )
    }

    AnimatedVisibility(
        visible = contacts.size < 4,
        modifier = Modifier.fillMaxWidth(),
    ) {
        if (isSaveFailed) {
            Text(
                text = "필수 항목을 입력해 주세요.",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = PieceTheme.typography.bodySM,
                color = PieceTheme.colors.error,
                modifier = Modifier.padding(top = 8.dp),
            )
        } else {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp)
                    .clickable { onAddContactClick() },
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
    }
}

@Composable
private fun SnsActivityContent(
    isSnsActive: Boolean,
    onSnsActivityChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionTitle(title = "SNS 활동")

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        PieceChip(
            label = "활동",
            selected = isSnsActive,
            onChipClicked = { onSnsActivityChanged(true) },
            enabled = true,
            modifier = Modifier.weight(1f),
        )

        PieceChip(
            label = "은둔",
            selected = !isSnsActive,
            onChipClicked = { onSnsActivityChanged(false) },
            enabled = true,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun SmokeContent(
    isSmoke: Boolean,
    onSmokingStatusChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionTitle(title = "흡연")

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        PieceChip(
            label = "흡연",
            selected = isSmoke,
            onChipClicked = { onSmokingStatusChanged(true) },
            modifier = Modifier.weight(1f),
        )

        PieceChip(
            label = "비흡연",
            selected = !isSmoke,
            onChipClicked = { onSmokingStatusChanged(false) },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun JobContent(
    job: String,
    jobInputState: InputState,
    onJobDropDownClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isSaveFailed: Boolean =
        jobInputState == InputState.WARNIING && job.isEmpty()

    SectionTitle(title = "직업")

    PieceTextInputDropDown(
        value = job,
        onDropDownClick = onJobDropDownClicked,
        modifier = modifier
    )

    AnimatedVisibility(
        visible = isSaveFailed
    ) {
        if (isSaveFailed) {
            Text(
                text = "필수 항목을 입력해 주세요.",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = PieceTheme.typography.bodySM,
                color = PieceTheme.colors.error,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

@Composable
private fun WeightContent(
    weight: String,
    weightInputState: InputState,
    onWeightChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionTitle(title = "몸무게")

    PieceTextInputDefault(
        value = weight,
        keyboardType = KeyboardType.Number,
        onValueChange = { weight ->
            if (weight.isDigitsOnly() && weight.length <= 3) {
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
        modifier = modifier,
    )

    AnimatedVisibility(
        visible = weightInputState == InputState.WARNIING
    ) {
        Text(
            text = when {
                weight.isBlank() -> "필수 항목을 입력해 주세요."
                else -> "숫자가 정확한 지 확인해 주세요"
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.error,
            modifier = modifier,
        )
    }
}

@Composable
private fun HeightContent(
    height: String,
    heightInputState: InputState,
    onHeightChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionTitle(title = "키")

    PieceTextInputDefault(
        value = height,
        keyboardType = KeyboardType.Number,
        onValueChange = { height ->
            if (height.isDigitsOnly() && height.length <= 3) {
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
        modifier = modifier,
    )

    AnimatedVisibility(
        visible = heightInputState == InputState.WARNIING
    ) {
        Text(
            text = when {
                height.isBlank() -> "필수 항목을 입력해 주세요."
                else -> "숫자가 정확한 지 확인해 주세요"
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.error,
            modifier = modifier,
        )
    }
}

@Composable
private fun LocationContent(
    location: String,
    locationInputState: InputState,
    onLocationDropDownClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isSaveFailed: Boolean =
        locationInputState == InputState.WARNIING && location.isBlank()

    SectionTitle(title = "활동 지역")

    PieceTextInputDropDown(
        value = location,
        onDropDownClick = onLocationDropDownClicked,
        modifier = modifier,
    )

    AnimatedVisibility(
        visible = isSaveFailed
    ) {
        Text(
            text = "필수 항목을 입력해 주세요.",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.error,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

@Composable
private fun BirthdateContent(
    birthdate: String,
    birthdateInputState: InputState,
    onBirthdayChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isSaveFailed: Boolean = birthdateInputState == InputState.WARNIING
    var isInputFocused by remember { mutableStateOf(false) }
    val isGuideMessageVisible = isInputFocused || birthdate.isNotBlank() || isSaveFailed

    SectionTitle(title = "생년월일")

    PieceTextInputDefault(
        value = birthdate,
        hint = stringResource(R.string.basic_profile_birthday_guide),
        keyboardType = KeyboardType.Number,
        onValueChange = { if (it.length <= 8) onBirthdayChanged(it) },
        rightComponent = {
            if (birthdate.isNotEmpty()) {
                Image(
                    painter = painterResource(R.drawable.ic_delete_circle),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(20.dp)
                        .clickable { onBirthdayChanged("") },
                )
            }
        },
        modifier = modifier.onFocusChanged { isInputFocused = it.isFocused },
    )

    AnimatedVisibility(visible = isGuideMessageVisible) {
        Text(
            text = if (isSaveFailed && birthdate.isBlank()) {
                "필수 항목을 입력해 주세요."
            } else {
                stringResource(R.string.basic_profile_birthday_guide)
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = PieceTheme.typography.bodySM,
            color = if (isSaveFailed) {
                PieceTheme.colors.error
            } else {
                PieceTheme.colors.dark3
            },
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

@Composable
private fun SelfDescriptionContent(
    description: String,
    descriptionInputState: InputState,
    onDescribeMySelfChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isSaveFailed: Boolean = descriptionInputState == InputState.WARNIING
    var isInputFocused by remember { mutableStateOf(false) }
    val isGuidanceVisibe: Boolean = isInputFocused || description.isNotBlank() || isSaveFailed

    SectionTitle(title = "나를 표현하는 한 마디")

    PieceTextInputDefault(
        value = description,
        hint = "수식어 형태로 작성해 주세요",
        keyboardType = KeyboardType.Text,
        onValueChange = onDescribeMySelfChanged,
        limit = 20,
        rightComponent = {
            if (description.isNotEmpty()) {
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
        modifier = modifier.onFocusChanged { isInputFocused = it.isFocused },
    )

    AnimatedVisibility(visible = isGuidanceVisibe) {
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = if (isSaveFailed) {
                    "필수 항목을 입력해 주세요."
                } else {
                    "수식어 형태로 작성해주세요."
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = PieceTheme.typography.bodySM,
                color = if (isSaveFailed) {
                    PieceTheme.colors.error
                } else {
                    PieceTheme.colors.dark3
                },
                modifier = Modifier.weight(1f),
            )

            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = PieceTheme.colors.primaryDefault)) {
                        append(description.length.toString())
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
}

@Composable
private fun NickNameContent(
    nickName: String,
    nickNameGuideMessage: NickNameGuideMessage,
    isCheckingButtonAvailable: Boolean,
    onDuplicationCheckClick: () -> Unit,
    onNickNameChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    var isInputFocused by remember { mutableStateOf(false) }
    val isGuideMessageVisible: Boolean = isInputFocused ||
            nickName.isNotBlank() ||
            nickNameGuideMessage.inputState == InputState.WARNIING

    val textColor = when (nickNameGuideMessage.inputState) {
        InputState.DEFAULT ->
            if (nickNameGuideMessage == NickNameGuideMessage.AVAILABLE) {
                PieceTheme.colors.primaryDefault
            } else {
                PieceTheme.colors.dark3
            }

        InputState.WARNIING -> PieceTheme.colors.error
    }

    SectionTitle(title = "닉네임")

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        PieceTextInputDefault(
            value = nickName,
            hint = "6자 이하로 작성해주세요",
            keyboardType = KeyboardType.Text,
            onValueChange = onNickNameChanged,
            limit = 6,
            rightComponent = {
                if (nickName.isNotEmpty()) {
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
                .onFocusChanged { isInputFocused = it.isFocused },
        )

        PieceSolidButton(
            label = "중복검사",
            onClick = {
                onDuplicationCheckClick()
                focusManager.clearFocus()
            },
            enabled = isCheckingButtonAvailable,
            modifier = Modifier.padding(start = 8.dp),
        )
    }

    AnimatedVisibility(visible = isGuideMessageVisible) {
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(nickNameGuideMessage.guideMessageId),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = PieceTheme.typography.bodySM,
                color = textColor,
                modifier = Modifier.weight(1f),
            )

            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = PieceTheme.colors.primaryDefault)) {
                        append(nickName.length.toString())
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
}

@Composable
private fun PhotoContent(
    imageUrl: String,
    imageUrlInputState: InputState,
    onProfileImageChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isSaveFailed: Boolean = imageUrlInputState == InputState.WARNIING && imageUrl.isEmpty()

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                onProfileImageChanged(uri.toString())
            }
        }
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.throttledClickable(throttleTime = 2000L) {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
        ) {
            AsyncImage(
                model = imageUrl.ifEmpty { R.drawable.ic_profile_default },
                placeholder = painterResource(R.drawable.ic_profile_default),
                contentScale = ContentScale.FillBounds,
                onError = { error ->
                    Log.e(
                        "RegisterProfileScreen", error.result.throwable.stackTraceToString()
                    )
                },
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
            )

            Image(
                painter = if (isSaveFailed) {
                    painterResource(R.drawable.ic_photo_error)
                } else {
                    painterResource(R.drawable.ic_edit)
                },
                contentDescription = null,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }

        AnimatedVisibility(
            visible = isSaveFailed
        ) {
            Text(
                text = stringResource(R.string.basic_profile_required_field),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = PieceTheme.typography.bodySM,
                color = PieceTheme.colors.error,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
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

@Preview
@Composable
private fun BasicProfilePreview() {
    PieceTheme {
        BasicProfileScreen(
            state = BasicProfileState(),
            onDuplicationCheckClick = {},
            onSaveClick = {},
            onBackClick = {},
            onNickNameChanged = {},
            onDescribeMySelfChanged = {},
            onBirthdayChanged = {},
            onLocationDropDownClicked = {},
            onHeightChanged = {},
            onWeightChanged = {},
            onJobDropDownClicked = {},
            onSmokingStatusChanged = {},
            onSnsPlatformChange = {},
            onContactChange = { _, _ -> },
            onSnsActivityChanged = {},
            onDeleteClick = {},
            onAddContactClick = {},
            onProfileImageChanged = {},
        )
    }
}
