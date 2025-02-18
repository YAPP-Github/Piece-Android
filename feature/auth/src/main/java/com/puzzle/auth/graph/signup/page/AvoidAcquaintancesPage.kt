@file:OptIn(ExperimentalPermissionsApi::class)

package com.puzzle.auth.graph.signup.page

import android.Manifest.permission.READ_CONTACTS
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.puzzle.common.ui.clickable
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.component.PieceSubBackTopBar
import com.puzzle.designsystem.foundation.PieceTheme
import kotlinx.coroutines.delay

@Composable
internal fun AvoidAcquaintancesPage(
    isBlockContactsDone: Boolean,
    onBackClick: () -> Unit,
    goNextStep: () -> Unit,
    onAvoidAcquaintancesClick: () -> Unit,
) {
    BackHandler { onBackClick() }

    var showBlockContactsCompletePopUp by remember(isBlockContactsDone) {
        mutableStateOf(isBlockContactsDone)
    }

    LaunchedEffect(showBlockContactsCompletePopUp) {
        if (showBlockContactsCompletePopUp) {
            delay(2000L)
            showBlockContactsCompletePopUp = false
        }
    }

    val context = LocalContext.current
    val contactsPermission = rememberPermissionState(READ_CONTACTS)

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            PieceSubBackTopBar(
                title = "",
                onBackClick = onBackClick,
            )

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = PieceTheme.colors.primaryDefault)) {
                        append("아는 사람")
                    }

                    append("에게는\n프로필이 노출되지 않아요")
                },
                style = PieceTheme.typography.headingLSB,
                color = PieceTheme.colors.black,
                modifier = Modifier.padding(top = 20.dp),
            )

            Text(
                text = stringResource(R.string.avoid_acquaintances_description),
                style = PieceTheme.typography.bodySM,
                color = PieceTheme.colors.dark3,
                modifier = Modifier.padding(top = 12.dp),
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f),
            )

            Image(
                painter = painterResource(R.drawable.ic_avoid_acquaintances),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(300.dp),
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.2f),
            )

            Text(
                text = stringResource(R.string.try_next),
                style = PieceTheme.typography.bodyMM.copy(textDecoration = TextDecoration.Underline),
                color = PieceTheme.colors.dark3,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 12.dp)
                    .clickable(enabled = !showBlockContactsCompletePopUp) {
                        goNextStep()
                    },
            )

            PieceSolidButton(
                label = stringResource(R.string.avoid_acquaintances),
                onClick = {
                    if (!showBlockContactsCompletePopUp) {
                        if (contactsPermission.status == PermissionStatus.Granted) {
                            onAvoidAcquaintancesClick()
                        } else {
                            handlePermission(
                                context = context,
                                permission = contactsPermission,
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 10.dp),
            )
        }

        AnimatedVisibility(
            visible = showBlockContactsCompletePopUp,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 60.dp)
        ) {
            PieceAvoidAcquaintancesCompletePopUp()
        }
    }
}

@Composable
private fun PieceAvoidAcquaintancesCompletePopUp(
    modifier: Modifier = Modifier,
) {
    Card(
        colors = cardColors().copy(containerColor = PieceTheme.colors.dark2),
        shape = RoundedCornerShape(40.dp),
        modifier = modifier.width(200.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 40.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.ic_avoid_acquaintances_check),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .padding(bottom = 16.dp),
            )

            Text(
                text = stringResource(R.string.avoid_acquaintances_complete),
                style = PieceTheme.typography.headingMSB,
                color = PieceTheme.colors.white,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
private fun AvoidAcquaintancesPagePreview() {
    PieceTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
        ) {
            AvoidAcquaintancesPage(
                isBlockContactsDone = true,
                onBackClick = {},
                goNextStep = {},
                onAvoidAcquaintancesClick = {}
            )
        }
    }
}

@Preview
@Composable
private fun AvoidAcquaintancesDialogPreview() {
    PieceTheme {
        PieceAvoidAcquaintancesCompletePopUp()
    }
}
