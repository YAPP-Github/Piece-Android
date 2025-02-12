package com.puzzle.profile.graph.register.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceBottomSheetHeader
import com.puzzle.designsystem.component.PieceBottomSheetListItemDefault
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.domain.model.profile.ContactType

@Composable
internal fun ContactBottomSheet(
    usingContactType: Set<ContactType>,
    isEdit: Boolean,
    onButtonClicked: (ContactType) -> Unit,
    nowContactType: ContactType? = null,
) {
    val scrollState = rememberScrollState()
    var tempContactType by remember { mutableStateOf<ContactType?>(nowContactType) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {}
            .padding(horizontal = 20.dp),
    ) {
        PieceBottomSheetHeader(title = "연락처")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 400.dp)
                .padding(top = 12.dp)
                .verticalScroll(scrollState),
        ) {
            ContactType.entries.forEach { sns ->
                if (sns == ContactType.UNKNOWN) return@forEach

                val image = when (sns) {
                    ContactType.KAKAO_TALK_ID -> R.drawable.ic_sns_kakao
                    ContactType.OPEN_CHAT_URL -> R.drawable.ic_sns_openchatting
                    ContactType.INSTAGRAM_ID -> R.drawable.ic_sns_instagram
                    ContactType.PHONE_NUMBER -> R.drawable.ic_sns_call
                    else -> R.drawable.ic_delete_circle
                }

                PieceBottomSheetListItemDefault(
                    label = sns.displayName,
                    image = image,
                    checked = if (isEdit) sns == tempContactType
                    else sns in usingContactType || sns == tempContactType,
                    enabled = sns !in usingContactType,
                    onChecked = { tempContactType = sns },
                )
            }
        }

        PieceSolidButton(
            label = "적용하기",
            onClick = { onButtonClicked(tempContactType!!) },
            enabled = tempContactType != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 10.dp),
        )
    }
}
