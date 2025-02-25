package com.puzzle.matching.graph.contact

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
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
import coil3.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
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
import kotlinx.coroutines.delay

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
        onCloseClick = { viewModel.onIntent(ContactIntent.OnCloseClick) },
        onContactClick = { viewModel.onIntent(ContactIntent.OnContactClick(it)) },
    )
}

@Composable
private fun ContactScreen(
    state: ContactState,
    onCloseClick: () -> Unit,
    onContactClick: (Contact) -> Unit,
) {
    var isMatchingAnimationEnd by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(3000L)
        isMatchingAnimationEnd = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundImage()

        PieceSubCloseTopBar(
            title = "",
            onCloseClick = onCloseClick,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        )

        ContactBody(
            nickName = state.nickName,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(top = 80.dp)
                .padding(horizontal = 20.dp),
        )


        Box(
            contentAlignment = Center,
            modifier = Modifier
                .align(Center)
                .size(500.dp)
        ) {
            AnimatedVisibility(
                visible = isMatchingAnimationEnd,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                AsyncImage(
                    model = state.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .size(220.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(PieceTheme.colors.black),
                )
            }

            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.anim_matching_success))
            LottieAnimation(
                composition = composition,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.size(500.dp),
            )
        }

        state.selectedContact?.let { selectedContact ->
            ContactInfo(
                isMatchingAnimationEnd = isMatchingAnimationEnd,
                selectedContact = selectedContact,
                contacts = state.contacts,
                onContactClick = onContactClick,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
private fun ContactBody(
    nickName: String,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = PieceTheme.colors.primaryDefault)) {
                    append(nickName)
                }
                append(stringResource(R.string.maching_contact_header))
            },
            textAlign = TextAlign.Center,
            style = PieceTheme.typography.headingLSB,
        )

        Text(
            text = stringResource(R.string.maching_contact_sub_header),
            textAlign = TextAlign.Center,
            style = PieceTheme.typography.bodyMR,
            color = PieceTheme.colors.dark1,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
private fun ContactInfo(
    isMatchingAnimationEnd: Boolean,
    selectedContact: Contact,
    contacts: List<Contact>,
    onContactClick: (Contact) -> Unit,
    modifier: Modifier = Modifier,
) {
    val contactIconId: Int? = selectedContact.type.getContactIconId()
    val contactNameId: Int? = selectedContact.type.getContactNameId()
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    AnimatedVisibility(
        visible = isMatchingAnimationEnd,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .padding(horizontal = 20.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    alignment = Alignment.CenterHorizontally,
                    space = 12.dp,
                ),
                modifier = Modifier.padding(bottom = 22.dp),
            ) {
                contacts.forEach { contact ->
                    val isSelected = selectedContact == contact
                    val contactOnOffIconId =
                        if (isSelected) {
                            contact.type.getSelectedContactIconId()
                        } else {
                            contact.type.getUnSelectedContactIconId()
                        }

                    if (contactOnOffIconId != null) {
                        AnimatedContent(
                            targetState = isSelected,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                        ) { _ ->
                            Image(
                                painter = painterResource(contactOnOffIconId),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(52.dp)
                                    .clickable { onContactClick(contact) },
                            )
                        }
                    }
                }
            }

            AnimatedContent(
                targetState = selectedContact,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
            ) { _ ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(color = PieceTheme.colors.white)
                        .padding(vertical = 24.dp, horizontal = 32.dp),
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
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 8.dp),
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
                                .padding(start = 8.dp)
                                .size(20.dp)
                                .clickable {
                                    clipboardManager.setText(
                                        AnnotatedString(
                                            selectedContact.content
                                        )
                                    )
                                },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BackgroundImage() {
    Image(
        painter = painterResource(id = R.drawable.matching_contact_bg),
        contentDescription = "basic info 배경화면",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize(),
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
        )
    }
}
