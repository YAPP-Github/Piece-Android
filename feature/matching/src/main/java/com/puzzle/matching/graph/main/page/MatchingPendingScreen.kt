package com.puzzle.matching.graph.main.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceDialog
import com.puzzle.designsystem.component.PieceDialogIconTop
import com.puzzle.designsystem.component.PieceMainTopBar
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.foundation.PieceTheme

@Composable
internal fun MatchingPendingScreen(
    isImageRejected: Boolean,
    isDescriptionRejected: Boolean,
    onCheckMyProfileClick: () -> Unit,
    onEditProfileClick: () -> Unit,
) {
    if (isImageRejected || isDescriptionRejected) {
        PieceDialog(
            dialogTop = {
                PieceDialogIconTop(
                    iconId = R.drawable.ic_notice,
                    title = stringResource(R.string.please_edit_profile),
                    descriptionComposable = {
                        if (isImageRejected) EditPhotoGuideText()
                        if (isDescriptionRejected) EditValueTalkGuideText()
                    },
                )
            },
            dialogBottom = {
                PieceSolidButton(
                    label = stringResource(R.string.edit_profile),
                    onClick = onEditProfileClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 20.dp),
                )
            },
            onDismissRequest = {}
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PieceTheme.colors.black)
            .padding(horizontal = 20.dp),
    ) {
        PieceMainTopBar(
            title = stringResource(R.string.matching_title),
            textStyle = PieceTheme.typography.branding,
            titleColor = PieceTheme.colors.white,
            rightComponent = {
                Image(
                    painter = painterResource(R.drawable.ic_alarm_black),
                    contentDescription = "알람",
                    colorFilter = ColorFilter.tint(PieceTheme.colors.white),
                    modifier = Modifier.size(32.dp),
                )
            },
            modifier = Modifier.padding(bottom = 20.dp),
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(PieceTheme.colors.white.copy(alpha = 0.1f)),
        ) {
            Text(
                text = stringResource(R.string.matching_pending_description),
                style = PieceTheme.typography.bodySM,
                color = PieceTheme.colors.light1,
                modifier = Modifier.padding(vertical = 12.dp),
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(top = 8.dp, bottom = 30.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(PieceTheme.colors.white)
                .padding(20.dp),
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = PieceTheme.colors.primaryDefault)) {
                        append("진중한 만남")
                    }
                    append("을 이어가기 위해\n프로필을 살펴보고 있어요.")
                },
                style = PieceTheme.typography.headingMSB,
                color = PieceTheme.colors.black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 20.dp, bottom = 8.dp),
            )

            Text(
                text = stringResource(R.string.matching_pending_subtext),
                style = PieceTheme.typography.bodySM,
                color = PieceTheme.colors.dark3,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 34.dp),
            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(R.drawable.ic_user_pending_home),
                contentDescription = null,
                modifier = Modifier
                    .size(260.dp)
                    .padding(bottom = 14.dp),
            )

            Spacer(modifier = Modifier.weight(1f))

            PieceSolidButton(
                label = stringResource(R.string.check_my_profile),
                onClick = onCheckMyProfileClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
            )
        }
    }
}

@Composable
private fun EditPhotoGuideText() {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = PieceTheme.colors.subDefault)) {
                append("얼굴이 잘나온 사진")
            }

            append("으로 변경해주세요")
        },
        color = PieceTheme.colors.dark3,
        style = PieceTheme.typography.bodySM,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(PieceTheme.colors.light3)
    )
}

@Composable
private fun EditValueTalkGuideText() {
    Text(
        text = buildAnnotatedString {
            append("가치관 talk을 좀 더 ")

            withStyle(style = SpanStyle(color = PieceTheme.colors.subDefault)) {
                append("정성스럽게")
            }

            append(" 써주세요")
        },
        color = PieceTheme.colors.dark3,
        style = PieceTheme.typography.bodySM,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(PieceTheme.colors.light3)
    )
}

@Preview
@Composable
private fun PreviewMatchingPendingScreen() {
    PieceTheme {
        MatchingPendingScreen(
            isImageRejected = false,
            isDescriptionRejected = false,
            onCheckMyProfileClick = {},
            onEditProfileClick = {},
        )
    }
}
