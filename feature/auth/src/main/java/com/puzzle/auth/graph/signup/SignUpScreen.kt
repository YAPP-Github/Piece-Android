package com.puzzle.auth.graph.signup

import android.content.Context
import android.provider.ContactsContract
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.auth.graph.signup.contract.SignUpIntent
import com.puzzle.auth.graph.signup.contract.SignUpState
import com.puzzle.auth.graph.signup.page.AccessRightsPage
import com.puzzle.auth.graph.signup.page.AvoidAcquaintancesPage
import com.puzzle.auth.graph.signup.page.SignUpCompletedPage
import com.puzzle.auth.graph.signup.page.TermDetailPage
import com.puzzle.auth.graph.signup.page.TermPage
import com.puzzle.common.ui.ANIMATION_DURATION

@Composable
internal fun SignUpRoute(
    viewModel: SignUpViewModel = mavericksViewModel()
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current

    SignUpScreen(
        state = state,
        checkAllTerms = { viewModel.onIntent(SignUpIntent.CheckAllTerms) },
        checkTerm = { viewModel.onIntent(SignUpIntent.CheckTerm(it)) },
        agreeTerm = { viewModel.onIntent(SignUpIntent.AgreeTerm(it)) },
        onTermDetailClick = { viewModel.onIntent(SignUpIntent.OnTermDetailClick) },
        onBackClick = { viewModel.onIntent(SignUpIntent.OnBackClick) },
        onNextClick = { viewModel.onIntent(SignUpIntent.OnNextClick) },
        onDisEnabledButtonClick = { viewModel.onIntent(SignUpIntent.OnDisEnabledButtonClick) },
        onAvoidAcquaintancesClick = {
            val phoneNumbers = readContactPhoneNumbers(context)
            viewModel.onIntent(SignUpIntent.OnAvoidAcquaintancesClick(phoneNumbers))
        },
        onGenerateProfileClick = { viewModel.onIntent(SignUpIntent.OnGenerateProfileClick) }
    )
}

@Composable
private fun SignUpScreen(
    state: SignUpState,
    checkAllTerms: () -> Unit,
    checkTerm: (Int) -> Unit,
    agreeTerm: (Int) -> Unit,
    onTermDetailClick: () -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    onDisEnabledButtonClick: () -> Unit,
    onAvoidAcquaintancesClick: () -> Unit,
    onGenerateProfileClick: () -> Unit,
) {
    val (selectedTermIdx, setSelectedTermIdx) = rememberSaveable { mutableStateOf<Int?>(null) }

    AnimatedContent(
        targetState = state.signUpPage,
        transitionSpec = {
            fadeIn(tween(ANIMATION_DURATION)) togetherWith fadeOut(tween(ANIMATION_DURATION))
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            when (it) {
                SignUpState.SignUpPage.TermPage -> TermPage(
                    terms = state.terms,
                    termsCheckedInfo = state.termsCheckedInfo,
                    allTermsAgreed = state.areAllRequiredTermsAgreed,
                    checkAllTerms = checkAllTerms,
                    checkTerm = checkTerm,
                    showTermDetail = {
                        setSelectedTermIdx(it)
                        onTermDetailClick()
                    },
                    onBackClick = onBackClick,
                    onNextClick = onNextClick,
                )

                SignUpState.SignUpPage.TermDetailPage -> TermDetailPage(
                    term = state.terms[selectedTermIdx!!],
                    onBackClick = onBackClick,
                    onAgreeClick = agreeTerm,
                )

                SignUpState.SignUpPage.AccessRightsPage -> AccessRightsPage(
                    onBackClick = onBackClick,
                    onNextClick = onNextClick,
                    onDisEnabledButtonClick = onDisEnabledButtonClick,
                )

                SignUpState.SignUpPage.AvoidAcquaintancesPage -> AvoidAcquaintancesPage(
                    isBlockContactsDone = state.isBlockContactsDone,
                    onBackClick = onBackClick,
                    goNextStep = onNextClick,
                    onAvoidAcquaintancesClick = onAvoidAcquaintancesClick,
                )

                SignUpState.SignUpPage.SignUpCompleted -> SignUpCompletedPage(
                    onGenerateProfileClick = onGenerateProfileClick,
                )
            }
        }
    }
}

private fun readContactPhoneNumbers(context: Context): List<String> {
    val phoneNumbers = mutableListOf<String>()

    val contentResolver = context.contentResolver
    val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
    val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)

    contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
        val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
        while (cursor.moveToNext()) {
            val phoneNumber = cursor.getString(numberIndex)
                .replace(Regex("[^0-9]"), "")
            if (phoneNumber.isNotBlank()) {
                phoneNumbers.add(phoneNumber)
            }
        }
    }

    return phoneNumbers.distinct()
}
