package com.puzzle.profile.graph.register.page

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
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
import com.puzzle.common.ui.clickable
import com.puzzle.common.ui.throttledClickable
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceChip
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.component.PieceTextInputDefault
import com.puzzle.designsystem.component.PieceTextInputDropDown
import com.puzzle.designsystem.component.PieceTextInputSnsDropDown
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.ContactType
import com.puzzle.profile.graph.basic.contract.InputState
import com.puzzle.profile.graph.basic.contract.NickNameGuideMessage
import com.puzzle.profile.graph.register.contract.RegisterProfileState

@Composable
internal fun BasicProfilePage(
    state: RegisterProfileState,
    onProfileImageChanged: (String) -> Unit,
    onNickNameChanged: (String) -> Unit,
    onDescribeMySelfChanged: (String) -> Unit,
    onBirthdateChanged: (String) -> Unit,
    onLocationDropDownClicked: () -> Unit,
    onHeightChanged: (String) -> Unit,
    onWeightChanged: (String) -> Unit,
    onJobDropDownClicked: () -> Unit,
    onSmokingStatusChanged: (Boolean) -> Unit,
    onSnsActivityChanged: (Boolean) -> Unit,
    onDuplicationCheckClick: () -> Unit,
    onContactChange: (Int, Contact) -> Unit,
    onSnsPlatformChange: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onAddContactClick: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .verticalScroll(scrollState)
            .clickable { focusManager.clearFocus() },
    ) {
        Text(
            text = stringResource(R.string.basic_profile_page_header),
            style = PieceTheme.typography.headingLSB,
            color = PieceTheme.colors.black,
            modifier = Modifier.padding(top = 20.dp),
        )

        Text(
            text = stringResource(R.string.basic_profile_page_sub_header),
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.dark3,
            modifier = Modifier.padding(top = 12.dp),
        )

        PhotoContent(
            profileImageUri = state.imageUrl,
            profileImageUriInputState = state.imageUrlInputState,
            onProfileImageChanged = onProfileImageChanged,
            modifier = Modifier.padding(top = 40.dp),
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
            onBirthdateChanged = onBirthdateChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        )

        LocationContent(
            location = state.location,
            locationInputState = state.locationInputState,
            onLocationDropDownClicked = {
                focusManager.clearFocus()
                onLocationDropDownClicked()
            },
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
            onJobDropDownClicked = {
                focusManager.clearFocus()
                onJobDropDownClicked()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        SmokeContent(
            isSmoke = state.isSmoke,
            isSmokeInputState = state.isSmokeInputState,
            onSmokingStatusChanged = onSmokingStatusChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        )

        SnsActivityContent(
            isSnsActive = state.isSnsActive,
            isSnsActiveInputState = state.isSnsActiveInputState,
            onSnsActivityChanged = onSnsActivityChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        )

        SnsPlatformContent(
            contacts = state.contacts,
            contactsInputState = state.contactsInputState,
            onContactChange = onContactChange,
            onSnsPlatformChange = {
                focusManager.clearFocus()
                onSnsPlatformChange(it)
            },
            onDeleteClick = {
                focusManager.clearFocus()
                onDeleteClick(it)
            },
            onAddContactClick = {
                focusManager.clearFocus()
                onAddContactClick()
            },
        )
    }
}


@Composable
private fun ColumnScope.SnsPlatformContent(
    contacts: List<Contact>,
    contactsInputState: InputState,
    onContactChange: (Int, Contact) -> Unit,
    onSnsPlatformChange: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onAddContactClick: () -> Unit,
) {
    val isSaveFailed: Boolean =
        contactsInputState == InputState.WARNIING

    SectionTitle(title = stringResource(R.string.basic_profile_contact_header))

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
            onDropDownClick = { onSnsPlatformChange(idx) },
            onDeleteClick = { onDeleteClick(idx) },
            keyboardType = if (contact.type == ContactType.PHONE_NUMBER) KeyboardType.Phone
            else KeyboardType.Text,
            isMandatory = (idx == 0),
            modifier = Modifier.padding(top = 8.dp),
        )
    }

    AnimatedVisibility(
        visible = contacts.size < 4,
        enter = fadeIn() + slideInVertically(),
        exit = shrinkOut() + slideOutVertically(),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            if (isSaveFailed) {
                Text(
                    text = stringResource(R.string.basic_profile_required_field),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = PieceTheme.typography.bodySM,
                    color = PieceTheme.colors.error,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp, bottom = 60.dp)
                    .clickable { onAddContactClick() },
            ) {
                Text(
                    text = stringResource(R.string.basic_profile_contact_add),
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
    isSnsActive: Boolean?,
    isSnsActiveInputState: InputState,
    onSnsActivityChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isSaveFailed: Boolean =
        isSnsActiveInputState == InputState.WARNIING && isSnsActive == null

    SectionTitle(title = stringResource(R.string.basic_profile_sns_activity_header))

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        PieceChip(
            label = stringResource(R.string.basic_profile_sns_activity_active),
            selected = isSnsActive ?: false,
            onChipClicked = { onSnsActivityChanged(true) },
            enabled = true,
            modifier = Modifier.weight(1f),
        )

        PieceChip(
            label = stringResource(R.string.basic_profile_sns_activity_inactive),
            selected = isSnsActive?.not() ?: false,
            onChipClicked = { onSnsActivityChanged(false) },
            enabled = true,
            modifier = Modifier.weight(1f),
        )
    }

    AnimatedVisibility(
        visible = isSaveFailed,
        enter = fadeIn() + slideInVertically(),
        exit = shrinkOut() + slideOutVertically(),
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

@Composable
private fun SmokeContent(
    isSmoke: Boolean?,
    isSmokeInputState: InputState,
    onSmokingStatusChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isSaveFailed: Boolean =
        isSmokeInputState == InputState.WARNIING && isSmoke == null

    SectionTitle(title = stringResource(R.string.basic_profile_issmoking_header))

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        PieceChip(
            label = stringResource(R.string.basic_profile_issmoking_smoking),
            selected = isSmoke ?: false,
            onChipClicked = { onSmokingStatusChanged(true) },
            modifier = Modifier.weight(1f),
        )

        PieceChip(
            label = stringResource(R.string.basic_profile_issmoking_not_smoking),
            selected = isSmoke?.not() ?: false,
            onChipClicked = { onSmokingStatusChanged(false) },
            modifier = Modifier.weight(1f),
        )
    }

    AnimatedVisibility(
        visible = isSaveFailed,
        enter = fadeIn() + slideInVertically(),
        exit = shrinkOut() + slideOutVertically(),
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

@Composable
private fun JobContent(
    job: String,
    jobInputState: InputState,
    onJobDropDownClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isSaveFailed: Boolean =
        jobInputState == InputState.WARNIING && job.isBlank()

    SectionTitle(title = stringResource(R.string.basic_profile_job_header))

    PieceTextInputDropDown(
        value = job,
        onDropDownClick = onJobDropDownClicked,
        modifier = modifier
    )

    AnimatedVisibility(
        visible = isSaveFailed,
        enter = fadeIn() + slideInVertically(),
        exit = shrinkOut() + slideOutVertically(),
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

@Composable
private fun WeightContent(
    weight: String,
    weightInputState: InputState,
    onWeightChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isSaveFailed: Boolean =
        weightInputState == InputState.WARNIING && weight.isBlank()

    SectionTitle(title = stringResource(R.string.basic_profile_weight_header))

    PieceTextInputDefault(
        value = weight,
        keyboardType = KeyboardType.Number,
        onValueChange = { weight ->
            if (weight.isDigitsOnly()) {
                onWeightChanged(weight)
            }
        },
        rightComponent = {
            Text(
                text = stringResource(R.string.basic_profile_weight_unit),
                style = PieceTheme.typography.bodySM,
                color = PieceTheme.colors.dark3,
            )
        },
        modifier = modifier,
    )

    val errorMessage = when {
        isSaveFailed -> stringResource(R.string.basic_profile_required_field)
        weight.length > 3 -> stringResource(R.string.basic_profile_number_validation_check)
        else -> null
    }

    AnimatedVisibility(
        visible = !errorMessage.isNullOrBlank(),
        enter = fadeIn() + slideInVertically(),
        exit = shrinkOut() + slideOutVertically(),
    ) {
        errorMessage?.let { message ->
            Text(
                text = message,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = PieceTheme.typography.bodySM,
                color = PieceTheme.colors.error,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun HeightContent(
    height: String,
    heightInputState: InputState,
    onHeightChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isSaveFailed: Boolean =
        heightInputState == InputState.WARNIING && height.isBlank()

    SectionTitle(title = stringResource(R.string.basic_profile_height_header))

    PieceTextInputDefault(
        value = height,
        keyboardType = KeyboardType.Number,
        onValueChange = { height ->
            if (height.isDigitsOnly()) {
                onHeightChanged(height)
            }
        },
        rightComponent = {
            Text(
                text = stringResource(R.string.basic_profile_height_unit),
                style = PieceTheme.typography.bodySM,
                color = PieceTheme.colors.dark3,
            )
        },
        modifier = modifier,
    )

    val errorMessage = when {
        isSaveFailed -> stringResource(R.string.basic_profile_required_field)
        // TODO : 소수점으로 입력하는 경우가 있을 것, 이 부분은 조금 더 고민 필요
        height.length > 5 -> stringResource(R.string.basic_profile_number_validation_check)
        else -> null
    }

    AnimatedVisibility(
        visible = !errorMessage.isNullOrBlank(),
        enter = fadeIn() + slideInVertically(),
        exit = shrinkOut() + slideOutVertically(),
    ) {
        errorMessage?.let { message ->
            Text(
                text = message,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = PieceTheme.typography.bodySM,
                color = PieceTheme.colors.error,
                modifier = modifier,
            )
        }
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

    SectionTitle(title = stringResource(R.string.basic_profile_region))

    PieceTextInputDropDown(
        value = location,
        onDropDownClick = onLocationDropDownClicked,
        modifier = modifier,
    )

    AnimatedVisibility(
        visible = isSaveFailed,
        enter = fadeIn() + slideInVertically(),
        exit = shrinkOut() + slideOutVertically(),
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

@Composable
private fun BirthdateContent(
    birthdate: String,
    birthdateInputState: InputState,
    onBirthdateChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isSaveFailed: Boolean =
        birthdateInputState == InputState.WARNIING && birthdate.isBlank()
    var isInputFocused by remember { mutableStateOf(false) }
    val isGuideMessageVisible = isInputFocused || birthdate.isNotBlank() || isSaveFailed

    SectionTitle(title = stringResource(R.string.basic_profile_birthday))

    PieceTextInputDefault(
        value = birthdate,
        hint = stringResource(R.string.basic_profile_birthday_guide),
        keyboardType = KeyboardType.Number,
        onValueChange = onBirthdateChanged,
        rightComponent = {
            if (birthdate.isNotBlank()) {
                Image(
                    painter = painterResource(R.drawable.ic_delete_circle),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(20.dp)
                        .clickable { onBirthdateChanged("") },
                )
            }
        },
        modifier = modifier.onFocusChanged { isInputFocused = it.isFocused },
    )

    AnimatedVisibility(
        visible = isGuideMessageVisible,
        enter = fadeIn() + slideInVertically(),
        exit = shrinkOut() + slideOutVertically(),
    ) {
        Text(
            text = if (isSaveFailed) {
                stringResource(R.string.basic_profile_required_field)
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
    val isSaveFailed: Boolean =
        descriptionInputState == InputState.WARNIING
    var isInputFocused by remember { mutableStateOf(false) }
    val isGuidanceVisible: Boolean = isInputFocused || description.isNotBlank() || isSaveFailed

    SectionTitle(title = stringResource(R.string.basic_profile_self_description_header))

    PieceTextInputDefault(
        value = description,
        hint = stringResource(R.string.basic_profile_self_description_guide),
        keyboardType = KeyboardType.Text,
        onValueChange = {
            if (it.length <= 20) onDescribeMySelfChanged(it)
        },
        rightComponent = {
            if (description.isNotBlank()) {
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

    AnimatedVisibility(
        visible = isGuidanceVisible,
        enter = fadeIn() + slideInVertically(),
        exit = shrinkOut() + slideOutVertically(),
    ) {
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = if (isSaveFailed) {
                    stringResource(R.string.basic_profile_required_field)
                } else {
                    stringResource(R.string.basic_profile_self_description_guide)
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
                    append("/" + stringResource(R.string.basic_profile_self_description_limit))
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

    Column(
        modifier = modifier
    ) {
        SectionTitle(title = stringResource(R.string.basic_profile_nickname_header))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp),
        ) {
            PieceTextInputDefault(
                value = nickName,
                hint = stringResource(R.string.basic_profile_nickname_length_guide),
                keyboardType = KeyboardType.Text,
                onValueChange = onNickNameChanged,
                rightComponent = {
                    if (nickName.isNotBlank()) {
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
                label = stringResource(R.string.basic_profile_nickname_duplication_check),
                onClick = {
                    onDuplicationCheckClick()
                    focusManager.clearFocus()
                },
                enabled = isCheckingButtonAvailable,
                modifier = Modifier.padding(start = 8.dp),
            )
        }

        AnimatedVisibility(
            visible = isGuideMessageVisible,
            enter = fadeIn() + slideInVertically(),
            exit = shrinkOut() + slideOutVertically(),
        ) {
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
                        append("/" + stringResource(R.string.basic_profile_nickname_length_limit))
                    },
                    maxLines = 1,
                    style = PieceTheme.typography.bodySM,
                    color = PieceTheme.colors.dark3,
                    modifier = Modifier.padding(start = 5.dp),
                )
            }
        }
    }
}

@Composable
private fun PhotoContent(
    profileImageUriInputState: InputState,
    onProfileImageChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    profileImageUri: String? = null,
) {
    // TODO : 이미지 조건 추가, screenState == BasicProfileState.ScreenState.SAVE_FAILED && 이미지가 없을 경우
    val isSaveFailed: Boolean =
        profileImageUriInputState == InputState.WARNIING && profileImageUri == null

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
                model = profileImageUri ?: R.drawable.ic_profile_default,
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
            visible = isSaveFailed,
            enter = fadeIn() + slideInVertically(),
            exit = shrinkOut() + slideOutVertically(),
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
private fun BasicProfilePagePreview() {
    PieceTheme {
        BasicProfilePage(
            state = RegisterProfileState(),
            onNickNameChanged = {},
            onProfileImageChanged = {},
            onDescribeMySelfChanged = {},
            onBirthdateChanged = {},
            onLocationDropDownClicked = {},
            onHeightChanged = {},
            onWeightChanged = {},
            onJobDropDownClicked = {},
            onSmokingStatusChanged = {},
            onSnsActivityChanged = {},
            onDeleteClick = {},
            onContactChange = { _, _ -> },
            onSnsPlatformChange = {},
            onDuplicationCheckClick = {},
            onAddContactClick = {}
        )
    }
}
