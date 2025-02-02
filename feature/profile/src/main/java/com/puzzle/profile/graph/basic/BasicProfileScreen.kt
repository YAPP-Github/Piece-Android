package com.puzzle.profile.graph.basic

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
import com.puzzle.common.ui.repeatOnStarted
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceChip
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.component.PieceSubTopBar
import com.puzzle.designsystem.component.PieceTextInputDefault
import com.puzzle.designsystem.component.PieceTextInputDropDown
import com.puzzle.designsystem.component.PieceTextInputSnsDropDown
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.SnsPlatform
import com.puzzle.profile.graph.basic.contract.BasicProfileIntent
import com.puzzle.profile.graph.basic.contract.BasicProfileSideEffect
import com.puzzle.profile.graph.basic.contract.BasicProfileState
import com.puzzle.profile.graph.register.bottomsheet.ContactBottomSheet
import com.puzzle.profile.graph.register.bottomsheet.JobBottomSheet
import com.puzzle.profile.graph.register.bottomsheet.LocationBottomSheet

@Composable
internal fun BasicProfileRoute(
    viewModel: BasicProfileViewModel = mavericksViewModel()
) {
    val state by viewModel.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel) {
        lifecycleOwner.repeatOnStarted {
            viewModel.sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is BasicProfileSideEffect.Navigate ->
                        viewModel.navigationHelper.navigate(sideEffect.navigationEvent)
                }
            }
        }
    }

    BasicProfileScreen(
        state = state,
        isEdited = state.isEdited,
        onSaveClick = {},
        onBackClick = { viewModel.onIntent(BasicProfileIntent.OnBackClick) },
        onNickNameChanged = { viewModel.onIntent(BasicProfileIntent.UpdateNickName(it)) },
        onDescribeMySelfChanged = { viewModel.onIntent(BasicProfileIntent.UpdateDescribeMySelf(it)) },
        onBirthdayChanged = { viewModel.onIntent(BasicProfileIntent.UpdateBirthday(it)) },
        onHeightChanged = { viewModel.onIntent(BasicProfileIntent.UpdateHeight(it)) },
        onWeightChanged = { viewModel.onIntent(BasicProfileIntent.UpdateWeight(it)) },
        onSmokeStatusChanged = { viewModel.onIntent(BasicProfileIntent.UpdateSmokeStatus(it)) },
        onSnsActivityChanged = { viewModel.onIntent(BasicProfileIntent.UpdateSnsActivity(it)) },
        onAddContactClick = {
            viewModel.onIntent(
                BasicProfileIntent.ShowBottomSheet {
                    ContactBottomSheet(
                        usingSnsPlatform = state.usingSnsPlatforms,
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
                        usingSnsPlatform = state.usingSnsPlatforms,
                        nowSnsPlatform = state.contacts[idx].snsPlatform,
                        isEdit = true,
                        onButtonClicked = {
                            viewModel.onIntent(
                                BasicProfileIntent.UpdateContact(
                                    idx, state.contacts[idx].copy(snsPlatform = it)
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
    isEdited: Boolean,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
    onNickNameChanged: (String) -> Unit,
    onDescribeMySelfChanged: (String) -> Unit,
    onBirthdayChanged: (String) -> Unit,
    onLocationDropDownClicked: () -> Unit,
    onHeightChanged: (String) -> Unit,
    onWeightChanged: (String) -> Unit,
    onJobDropDownClicked: () -> Unit,
    onSmokeStatusChanged: (Boolean) -> Unit,
    onSnsActivityChanged: (Boolean) -> Unit,
    onContactChange: (Int, Contact) -> Unit,
    onSnsPlatformChange: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onAddContactClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    var previousContactSize by remember { mutableStateOf(1) }

    LaunchedEffect(state.contacts) {
        if (previousContactSize < state.contacts.size) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }

        previousContactSize = state.contacts.size
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PieceTheme.colors.white)
            .verticalScroll(scrollState)
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
                    color = if (isEdited) {
                        PieceTheme.colors.primaryDefault
                    } else {
                        PieceTheme.colors.dark3
                    },
                    modifier = Modifier.clickable {
                        if (isEdited) {
                            onSaveClick()
                        }
                    },
                )
            },
        )

        PhotoContent(
            onEditPhotoClick = {},
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 40.dp)
                .size(120.dp),
        )

        NickNameContent(
            nickName = state.nickName,
            onNickNameChanged = onNickNameChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        )

        SelfDescriptionContent(
            description = state.description,
            onDescribeMySelfChanged = onDescribeMySelfChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        )

        BirthdateContent(
            birthdate = state.birthdate,
            onBirthdayChanged = onBirthdayChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        )

        LocationContent(
            location = state.location,
            onLocationDropDownClicked = onLocationDropDownClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        HeightContent(
            height = state.height,
            onHeightChanged = onHeightChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        WeightContent(
            weight = state.weight,
            onWeightChanged = onWeightChanged,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        )

        JobContent(
            job = state.job,
            onJobDropDownClicked = onJobDropDownClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        SmokeContent(
            isSmoke = state.isSmoke,
            onSmokeStatusChanged = onSmokeStatusChanged,
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

@Composable
private fun ColumnScope.SnsPlatformContent(
    contacts: List<Contact>,
    onContactChange: (Int, Contact) -> Unit,
    onSnsPlatformChange: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onAddContactClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionTitle(title = "연락처")

    contacts.forEachIndexed { idx, contact ->
        val image = when (contact.snsPlatform) {
            SnsPlatform.KAKAO -> R.drawable.ic_sns_kakao
            SnsPlatform.OPENKAKAO -> R.drawable.ic_sns_openchatting
            SnsPlatform.INSTA -> R.drawable.ic_sns_instagram
            SnsPlatform.PHONE -> R.drawable.ic_sns_call
            else -> R.drawable.ic_delete_circle // 임시
        }

        PieceTextInputSnsDropDown(
            value = contact.content,
            image = image,
            onValueChange = { onContactChange(idx, contact.copy(content = it)) },
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
    onSmokeStatusChanged: (Boolean) -> Unit,
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
            onChipClicked = { onSmokeStatusChanged(true) },
            modifier = Modifier.weight(1f),
        )

        PieceChip(
            label = "비흡연",
            selected = !isSmoke,
            onChipClicked = { onSmokeStatusChanged(false) },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun JobContent(
    job: String,
    onJobDropDownClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionTitle(title = "직업")

    PieceTextInputDropDown(
        value = job,
        onDropDownClick = onJobDropDownClicked,
        modifier = modifier
    )
}

@Composable
private fun WeightContent(
    weight: String,
    onWeightChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionTitle(title = "몸무게")
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
                text = "kg",
                style = PieceTheme.typography.bodySM,
                color = PieceTheme.colors.dark3,
            )
        },
        modifier = modifier,
    )

    if (weight.isEmpty()) {
        Text(
            text = "숫자가 정확한 지 확인해 주세요",
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
    onHeightChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionTitle(title = "키")

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
                text = "cm",
                style = PieceTheme.typography.bodySM,
                color = PieceTheme.colors.dark3,
            )
        },
        modifier = modifier,
    )

    if (height.isEmpty()) {
        Text(
            text = "숫자가 정확한 지 확인해 주세요",
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
    onLocationDropDownClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionTitle(title = "활동 지역")

    PieceTextInputDropDown(
        value = location,
        onDropDownClick = onLocationDropDownClicked,
        modifier = modifier,
    )
}

@Composable
private fun BirthdateContent(
    birthdate: String,
    onBirthdayChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionTitle(title = "생년월일")

    PieceTextInputDefault(
        value = birthdate,
        hint = "6자리(YYMMDD) 형식으로 입력해 주세요",
        keyboardType = KeyboardType.Number,
        onValueChange = onBirthdayChanged,
        rightComponent = {
            if (birthdate.isNotEmpty()) {
                Image(
                    painter = painterResource(R.drawable.ic_delete_circle),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun SelfDescriptionContent(
    description: String,
    onDescribeMySelfChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionTitle(title = "나를 표현하는 한 마디")

    PieceTextInputDefault(
        value = description,
        hint = "수식어 형태로 작성해 주세요",
        keyboardType = KeyboardType.Text,
        onValueChange = onDescribeMySelfChanged,
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
        modifier = modifier,
    )

    Row(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(),
    ) {
        Text(
            text = "수식어 형태로 작성해주세요.",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.dark3,
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

@Composable
private fun ColumnScope.NickNameContent(
    nickName: String,
    onNickNameChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
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
            modifier = Modifier.weight(1f)
        )

        PieceSolidButton(
            label = "중복검사",
            onClick = { },
            enabled = nickName.isNotEmpty(),
            modifier = Modifier.padding(start = 8.dp)
        )
    }

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

@Composable
private fun ColumnScope.PhotoContent(
    onEditPhotoClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_profile_default),
            contentDescription = null,
        )

        Image(
            painter = painterResource(R.drawable.ic_edit),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .clickable {
                    onEditPhotoClick()
                }
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

@Preview
@Composable
private fun BasicProfilePreview() {
    PieceTheme {
        BasicProfileScreen(
            state = BasicProfileState(),
            isEdited = false,
            onSaveClick = {},
            onBackClick = {},
            onNickNameChanged = {},
            onDescribeMySelfChanged = {},
            onBirthdayChanged = {},
            onLocationDropDownClicked = {},
            onHeightChanged = {},
            onWeightChanged = {},
            onJobDropDownClicked = {},
            onSmokeStatusChanged = {},
            onSnsPlatformChange = {},
            onContactChange = { _, _ -> },
            onSnsActivityChanged = {},
            onDeleteClick = {},
            onAddContactClick = {},
        )
    }
}