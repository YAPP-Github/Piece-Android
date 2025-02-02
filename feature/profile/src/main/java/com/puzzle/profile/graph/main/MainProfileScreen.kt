package com.puzzle.profile.graph.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.common.ui.repeatOnStarted
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceMainTopBar
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.profile.graph.main.contract.MainProfileIntent
import com.puzzle.profile.graph.main.contract.MainProfileSideEffect
import com.puzzle.profile.graph.main.contract.MainProfileState

@Composable
internal fun MainProfileRoute(
    viewModel: MainProfileViewModel = mavericksViewModel()
) {
    val state by viewModel.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel) {
        lifecycleOwner.repeatOnStarted {
            viewModel.sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is MainProfileSideEffect.Navigate ->
                        viewModel.navigationHelper.navigate(sideEffect.navigationEvent)
                }
            }
        }
    }

    MainProfileScreen(
        state = state,
        onMyProfileClick = { viewModel.onIntent(MainProfileIntent.OnMyProfileClick) },
        onValueTalkClick = { viewModel.onIntent(MainProfileIntent.OnValueTalkClick) },
        onValuePickClick = { viewModel.onIntent(MainProfileIntent.OnValuePickClick) },
    )
}

@Composable
private fun MainProfileScreen(
    state: MainProfileState,
    onMyProfileClick: () -> Unit,
    onValueTalkClick: () -> Unit,
    onValuePickClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PieceTheme.colors.white),
    ) {
        PieceMainTopBar(
            title = stringResource(R.string.main_profile_topbar_title),
            rightComponent = {
                Image(
                    painter = painterResource(R.drawable.ic_alarm_black),
                    contentDescription = "알람",
                )
            },
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 20.dp),
        )

        MyProfile(
            nickName = state.nickName,
            selfDescription = state.selfDescription,
            age = state.age,
            birthYear = state.birthYear,
            height = state.height,
            activityRegion = state.activityRegion,
            occupation = state.occupation,
            smokeStatue = state.smokeStatue,
            weight = state.weight,
            onMyProfileClick = onMyProfileClick,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        HorizontalDivider(
            thickness = 12.dp,
            color = PieceTheme.colors.light3,
        )

        MyMatchingPiece(
            onValueTalkClick = onValueTalkClick,
            onValuePickClick = onValuePickClick,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    }
}

@Composable
private fun MyProfile(
    nickName: String,
    selfDescription: String,
    age: String,
    birthYear: String,
    height: String,
    activityRegion: String,
    occupation: String,
    smokeStatue: String,
    weight: String,
    onMyProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(top = 20.dp)
            .clickable { onMyProfileClick() }
    ) {
        Image(
            painter = painterResource(R.drawable.ic_profile_default),
            contentDescription = null,
            modifier = Modifier.size(80.dp),
        )

        Column(
            modifier = Modifier
                .padding(vertical = 9.dp)
                .padding(start = 20.dp)
                .align(Alignment.CenterVertically),
        ) {
            Text(
                text = selfDescription,
                style = PieceTheme.typography.bodyMR,
                modifier = Modifier.padding(bottom = 6.dp),
            )

            Text(
                text = nickName,
                color = PieceTheme.colors.primaryDefault,
                style = PieceTheme.typography.headingLSB,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(R.drawable.ic_arrow_right_black),
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.CenterVertically),
        )
    }

    BasicInfoCard(
        age = age,
        birthYear = birthYear,
        height = height,
        activityRegion = activityRegion,
        occupation = occupation,
        smokeStatue = smokeStatue,
        weight = weight,
        modifier = modifier,
    )
}

@Composable
private fun MyMatchingPiece(
    onValueTalkClick: () -> Unit,
    onValuePickClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(R.string.main_profile_my_matching_piece_title),
        style = PieceTheme.typography.bodyMM,
        color = PieceTheme.colors.dark2,
        modifier = modifier.padding(top = 24.dp, bottom = 12.dp),
    )

    MyMatchingPieceDetail(
        imageId = R.drawable.ic_talk,
        title = stringResource(R.string.main_profile_value_talk_title),
        content = stringResource(R.string.main_profile_value_talk_content),
        onMyMatchingPieceDetailClick = onValueTalkClick,
        modifier = modifier.padding(vertical = 16.dp),
    )

    HorizontalDivider(
        thickness = 1.dp,
        color = PieceTheme.colors.light2,
        modifier = modifier.fillMaxWidth(),
    )

    MyMatchingPieceDetail(
        imageId = R.drawable.ic_question,
        title = stringResource(R.string.main_profile_value_pick_title),
        content = stringResource(R.string.main_profile_value_pick_content),
        onMyMatchingPieceDetailClick = onValuePickClick,
        modifier = modifier.padding(vertical = 16.dp),
    )
}

@Composable
private fun MyMatchingPieceDetail(
    imageId: Int,
    title: String,
    content: String,
    onMyMatchingPieceDetailClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.clickable {
            onMyMatchingPieceDetailClick()
        },
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(imageId),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(PieceTheme.colors.dark1),
                    modifier = Modifier.size(20.dp),
                )

                Text(
                    text = title,
                    style = PieceTheme.typography.headingSSB,
                    color = PieceTheme.colors.dark1,
                    modifier = Modifier.padding(start = 8.dp),
                )
            }

            Text(
                text = content,
                style = PieceTheme.typography.captionM,
                color = PieceTheme.colors.dark3,
                modifier = Modifier.padding(start = 28.dp),
            )
        }

        Image(
            painter = painterResource(R.drawable.ic_arrow_right_black),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
        )
    }
}

@Composable
private fun BasicInfoCard(
    age: String,
    birthYear: String,
    height: String,
    weight: String,
    activityRegion: String,
    occupation: String,
    smokeStatue: String,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 4.dp),
    ) {
        InfoItem(
            title = stringResource(R.string.basicinfocard_age),
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.basicinfocard_age_particle),
                        style = PieceTheme.typography.bodySM,
                        color = PieceTheme.colors.black,
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = age,
                        style = PieceTheme.typography.headingSSB,
                        color = PieceTheme.colors.black,
                    )

                    Text(
                        text = stringResource(R.string.basicinfocard_age_classifier),
                        style = PieceTheme.typography.bodySM,
                        color = PieceTheme.colors.black,
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = birthYear + stringResource(R.string.basicinfocard_age_suffix),
                        style = PieceTheme.typography.bodySM,
                        color = PieceTheme.colors.dark2,
                        modifier = Modifier.padding(top = 1.dp),
                    )
                }
            },
            backgroundColor = PieceTheme.colors.light3,
            modifier = Modifier.size(
                width = 144.dp,
                height = 80.dp,
            ),
        )

        InfoItem(
            title = stringResource(R.string.basicinfocard_height),
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = height,
                        style = PieceTheme.typography.headingSSB,
                        color = PieceTheme.colors.black,
                    )

                    Text(
                        text = stringResource(R.string.basicinfocard_height_unit),
                        style = PieceTheme.typography.bodySM,
                        color = PieceTheme.colors.black,
                    )
                }
            },
            backgroundColor = PieceTheme.colors.light3,
            modifier = Modifier.weight(1f),
        )

        InfoItem(
            title = stringResource(R.string.basicinfocard_weight),
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = weight,
                        style = PieceTheme.typography.headingSSB,
                        color = PieceTheme.colors.black,
                    )

                    Text(
                        text = stringResource(R.string.basicinfocard_weight_unit),
                        style = PieceTheme.typography.bodySM,
                        color = PieceTheme.colors.black,
                    )
                }
            },
            backgroundColor = PieceTheme.colors.light3,
            modifier = Modifier.weight(1f),
        )
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
    ) {
        InfoItem(
            title = stringResource(R.string.basicinfocard_activityRegion),
            content = activityRegion,
            backgroundColor = PieceTheme.colors.light3,
            modifier = Modifier.size(
                width = 144.dp,
                height = 80.dp,
            ),
        )

        InfoItem(
            title = stringResource(R.string.basicinfocard_occupation),
            content = occupation,
            backgroundColor = PieceTheme.colors.light3,
            modifier = Modifier.weight(1f),
        )

        InfoItem(
            title = stringResource(R.string.basicinfocard_smokeStatue),
            content = smokeStatue,
            backgroundColor = PieceTheme.colors.light3,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun InfoItem(
    title: String,
    modifier: Modifier = Modifier,
    content: String? = null,
    backgroundColor: Color = PieceTheme.colors.white,
    text: @Composable ColumnScope.() -> Unit? = {},
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.CenterVertically,
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .height(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp),
    ) {
        Text(
            text = title,
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.dark2,
        )

        if (content != null) {
            Text(
                text = content,
                style = PieceTheme.typography.headingSSB,
                color = PieceTheme.colors.black,
            )
        } else {
            text()
        }
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    PieceTheme {
        MainProfileScreen(
            state = MainProfileState(
                nickName = "수줍은 수달",
                selfDescription = "음악과 요리를 좋아하는",
                age = "14",
                birthYear = "00",
                height = "100",
                activityRegion = "서울 특별시",
                occupation = "개발자",
                smokeStatue = "흡연",
            ),
            onMyProfileClick = {},
            onValueTalkClick = {},
            onValuePickClick = {},
        )
    }
}