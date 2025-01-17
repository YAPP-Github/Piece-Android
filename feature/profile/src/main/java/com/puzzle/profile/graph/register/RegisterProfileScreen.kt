package com.puzzle.profile.graph.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.navigation.NavigationEvent
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
    )
}

@Composable
private fun RegisterProfileScreen(
    state: RegisterProfileState,
    navigate: (NavigationEvent) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    var isNicknameFocus by remember { mutableStateOf(false) }
    var isOneLinerFocus by remember { mutableStateOf(false) }
    var isBirthdayFocus by remember { mutableStateOf(false) }
    var isRegionFocus by remember { mutableStateOf(false) }
    var isHeightFocus by remember { mutableStateOf(false) }
    var isJobFocus by remember { mutableStateOf(false) }
    var isContactFocus by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager),
    ) {
        PieceSubBackTopBar(
            title = "",
            onBackClick = { navigate(NavigationEvent.NavigateUp) },
            modifier = Modifier
                .padding(horizontal = 20.dp)
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
                    painter = painterResource(R.drawable.ic_plus),
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
                    value = "",
                    hint = "6자 이하로 작성해주세요",
                    imageId = R.drawable.ic_delete,
                    keyboardType = KeyboardType.Text,
                    onValueChange = {},
                    onImageClick = {},
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged { isNicknameFocus = it.isFocused },
                )

                PieceSolidButton(
                    label = "중복검사",
                    onClick = { },
                    enabled = false,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            if (isNicknameFocus) {

            }


            SectionTitle(title = "나를 표현하는 한 마디")
            PieceTextInputDefault(
                value = "",
                hint = "수식어 형태로 작성해주세요",
                imageId = R.drawable.ic_delete,
                keyboardType = KeyboardType.Text,
                onValueChange = {},
                onImageClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .onFocusChanged { isOneLinerFocus = it.isFocused },
            )

            SectionTitle(title = "생년월일")
            PieceTextInputDefault(
                value = "",
                hint = "2000.00.00.",
                readOnly = true,
                imageId = R.drawable.ic_delete,
                keyboardType = KeyboardType.Number,
                onValueChange = {},
                onImageClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .onFocusChanged { isBirthdayFocus = it.isFocused },
            )

            SectionTitle(title = "활동 지역")
            PieceTextInputDefault(
                value = "",
                imageId = R.drawable.ic_delete,
                keyboardType = KeyboardType.Number,
                onValueChange = {},
                onImageClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .onFocusChanged { isRegionFocus = it.isFocused },
            )

            SectionTitle(title = "키")
            PieceTextInputDefault(
                value = "",
                imageId = R.drawable.ic_delete,
                keyboardType = KeyboardType.Number,
                onValueChange = {},
                onImageClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .onFocusChanged { isHeightFocus = it.isFocused },
            )

            SectionTitle(title = "직업")
            PieceTextInputDropDown(
                value = "",
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
                    selected = false,
                    onChipClicked = {},
                    modifier = Modifier.weight(1f),
                )

                PieceChip(
                    label = "비흡연",
                    selected = false,
                    onChipClicked = {},
                    modifier = Modifier.weight(1f),
                )
            }

            SectionTitle(title = "연락처")
            PieceTextInputDefault(
                value = "",
                keyboardType = KeyboardType.Text,
                imageId = R.drawable.ic_delete,
                onImageClick = {},
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .onFocusChanged { isContactFocus = it.isFocused },
            )
        }

        PieceSolidButton(
            label = stringResource(R.string.next),
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
        )
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = PieceTheme.typography.bodySM,
        color = PieceTheme.colors.dark3,
        modifier = Modifier.padding(top = 40.dp),
    )
}

@Preview(heightDp = 1700)
@Composable
private fun PreviewRegisterProfileScreen() {
    PieceTheme {
        RegisterProfileScreen(
            state = RegisterProfileState(),
            navigate = { },
        )
    }
}