package com.puzzle.matching.graph.contact

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.common.ui.clickable
import com.puzzle.common.ui.repeatOnStarted
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceSubCloseTopBar
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.ContactType
import com.puzzle.matching.graph.contact.contract.ContactIntent
import com.puzzle.matching.graph.contact.contract.ContactSideEffect
import com.puzzle.matching.graph.contact.contract.ContactState
import com.puzzle.matching.graph.contact.contract.getContactIconId
import com.puzzle.matching.graph.contact.contract.getContactNameId
import com.puzzle.matching.graph.contact.contract.getSelectedContactIconId
import com.puzzle.matching.graph.contact.contract.getUnSelectedContactIconId

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
        onCloseClick = {
            viewModel.onIntent(ContactIntent.OnCloseClick)
        },
        onContactClick = {
            viewModel.onIntent(ContactIntent.OnContactClick(it))
        },
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

            ContactBody(nickName = state.nickName)

            Spacer(modifier = Modifier.weight(1f))

            state.selectedContact?.let { selectedContact ->
                ContactInfo(
                    selectedContact = selectedContact,
                    contacts = state.contacts,
                    onContactClick = onContactClick,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun ContactBody(nickName: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = PieceTheme.colors.primaryDefault)) {
                append(nickName)
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
}

@Composable
private fun ContactInfo(
    selectedContact: Contact,
    contacts: List<Contact>,
    onContactClick: (Contact) -> Unit,
    modifier: Modifier = Modifier,
) {
    val contactIconId: Int? = selectedContact.type.getContactIconId()
    val contactNameId: Int? = selectedContact.type.getContactNameId()
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color = PieceTheme.colors.white)
            .padding(vertical = 24.dp, horizontal = 32.dp)
            .fillMaxWidth(),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (contactIconId != null && contactNameId != null) {
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
        }

        Row(
            modifier = Modifier.padding(top = 8.dp),
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
                    .padding(start = 8.dp)
                    .clickable {
                        clipboardManager.setText(AnnotatedString(selectedContact.content))
                    },
            )
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(top = 21.dp, bottom = 10.dp),
    ) {
        contacts.forEach {
            val contactOnOffIconId =
                if (selectedContact == it) {
                    it.type.getSelectedContactIconId()
                } else {
                    it.type.getUnSelectedContactIconId()
                }

            if (contactOnOffIconId != null) {
                Image(
                    painter = painterResource(contactOnOffIconId),
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

@Composable
private fun BackgroundImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.matching_contact_bg),
        contentDescription = "basic info 배경화면",
        contentScale = ContentScale.FillBounds,
        modifier = modifier
            .fillMaxSize(),
    )
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
                        type = ContactType.KAKAO_TALK_ID,
                        content = "Puzzle1234"
                    ),
                    Contact(
                        type = ContactType.OPEN_CHAT_URL,
                        content = "Puzzle1234"
                    ),
                    Contact(
                        type = ContactType.INSTAGRAM_ID,
                        content = "Puzzle1234"
                    ),
                    Contact(
                        type = ContactType.PHONE_NUMBER,
                        content = "Puzzle1234"
                    )
                ),
                selectedContact = Contact(
                    type = ContactType.OPEN_CHAT_URL,
                    content = "Puzzle1234"
                ),
            ),
            onCloseClick = {},
            onContactClick = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
