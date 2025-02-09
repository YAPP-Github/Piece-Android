package com.puzzle.matching.graph.contact

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.common.ui.repeatOnStarted
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceSubCloseTopBar
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.SnsPlatform
import com.puzzle.matching.graph.contact.contract.ContactSideEffect
import com.puzzle.matching.graph.contact.contract.ContactState

@Composable
internal fun ContactRoute(
    viewModel: ContactViewModel = mavericksViewModel(),
) {
    val state by viewModel.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel) {
        lifecycleOwner.repeatOnStarted {
            viewModel.sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is ContactSideEffect.Navigate ->
                        viewModel.navigationHelper.navigate(sideEffect.navigationEvent)
                }
            }
        }
    }

    ContactScreen(
        state = state,
        onCloseClick = {},
        onContactClick = {},
    )
}

@Composable
private fun ContactScreen(
    state: ContactState,
    onCloseClick: () -> Unit,
    onContactClick: (Contact) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box {
        BackgroundImage(modifier = modifier)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(horizontal = 20.dp),
        ) {
            PieceSubCloseTopBar(
                title = "",
                onCloseClick = onCloseClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp),
            )

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = PieceTheme.colors.primaryDefault)) {
                        append(state.nickName)
                    }
                    append(stringResource(R.string.maching_contact_header))
                },
                style = PieceTheme.typography.headingLSB,
                modifier = Modifier.padding(top = 20.dp),
            )

            Text(
                text = stringResource(R.string.maching_contact_sub_header),
                textAlign = TextAlign.Center,
                style = PieceTheme.typography.bodyMR,
                color = PieceTheme.colors.dark1,
                modifier = Modifier.padding(top = 8.dp),
            )

            Image(
                painter = painterResource(R.drawable.ic_textinput_check),
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .padding(top = 48.dp),
            )

            Spacer(modifier = Modifier.weight(1f))

            state.selectedContact?.let { selectedContact ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .wrapContentSize()
                        .clip(RoundedCornerShape(8.dp))
                        .background(color = PieceTheme.colors.white)
                        .fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier.padding(top = 24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        val (contactIconId, contactNameId) = when (selectedContact.snsPlatform) {
                            SnsPlatform.KAKAO_TALK_ID -> R.drawable.ic_sns_kakao to R.string.maching_contact_kakao_id
                            SnsPlatform.OPEN_CHAT_URL -> R.drawable.ic_sns_openchatting to R.string.maching_contact_open_chat_id
                            SnsPlatform.INSTAGRAM_ID -> R.drawable.ic_sns_instagram to R.string.maching_contact_insta_id
                            SnsPlatform.PHONE_NUMBER -> R.drawable.ic_sns_call to R.string.maching_contact_phone_number
                            SnsPlatform.UNKNOWN -> R.drawable.ic_info to R.string.maching_contact_null
                        }

                        Image(
                            painter = painterResource(contactIconId),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                        )

                        Text(
                            text = stringResource(contactNameId),
                            textAlign = TextAlign.Center,
                            style = PieceTheme.typography.bodySSB,
                            color = PieceTheme.colors.dark3,
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }

                    Row(
                        modifier = Modifier.padding(top = 8.dp, bottom = 24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = selectedContact.content,
                            textAlign = TextAlign.Center,
                            style = PieceTheme.typography.headingMSB,
                            color = PieceTheme.colors.black,
                        )

                        Image(
                            painter = painterResource(R.drawable.ic_copy),
                            contentDescription = null,
                            modifier = Modifier
                                .size(20.dp)
                                .padding(start = 8.dp),
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(top = 21.dp),
                ) {
                    state.contacts.forEach {
                        val contactIconId =
                            if (selectedContact == it) {
                                when (selectedContact.snsPlatform) {
                                    SnsPlatform.KAKAO_TALK_ID -> R.drawable.ic_kakao_on
                                    SnsPlatform.OPEN_CHAT_URL -> R.drawable.ic_open_chat_on
                                    SnsPlatform.INSTAGRAM_ID -> R.drawable.ic_insta_on
                                    SnsPlatform.PHONE_NUMBER -> R.drawable.ic_phone_on
                                    SnsPlatform.UNKNOWN -> R.drawable.ic_info
                                }
                            } else {
                                when (it.snsPlatform) {
                                    SnsPlatform.KAKAO_TALK_ID -> R.drawable.ic_kakao_off
                                    SnsPlatform.OPEN_CHAT_URL -> R.drawable.ic_open_chat_off
                                    SnsPlatform.INSTAGRAM_ID -> R.drawable.ic_insta_off
                                    SnsPlatform.PHONE_NUMBER -> R.drawable.ic_phone_off
                                    SnsPlatform.UNKNOWN -> R.drawable.ic_info
                                }
                            }

                        Image(
                            painter = painterResource(contactIconId),
                            contentDescription = null,
                            modifier = Modifier
                                .size(52.dp)
                                .clickable {
                                    onContactClick(it)
                                },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BackgroundImage(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PieceTheme.colors.white)
    ) {
        Image(
            painter = painterResource(id = R.drawable.matching_contact_bg),
            contentDescription = "basic info 배경화면",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize(),
        )
    }
}

@Preview
@Composable
private fun ContactScreenPreview() {
    PieceTheme {
        ContactScreen(
            state = ContactState(
                nickName = "수줍은 수달",
                contacts = listOf(
                    Contact(
                        snsPlatform = SnsPlatform.KAKAO_TALK_ID,
                        content = "Puzzle1234"
                    ),
                    Contact(
                        snsPlatform = SnsPlatform.OPEN_CHAT_URL,
                        content = "Puzzle1234"
                    ),
                    Contact(
                        snsPlatform = SnsPlatform.INSTAGRAM_ID,
                        content = "Puzzle1234"
                    ),
                    Contact(
                        snsPlatform = SnsPlatform.PHONE_NUMBER,
                        content = "Puzzle1234"
                    )
                ),
                selectedContact = Contact(
                    snsPlatform = SnsPlatform.OPEN_CHAT_URL,
                    content = "Puzzle1234"
                ),
            ),
            onCloseClick = {},
            onContactClick = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}