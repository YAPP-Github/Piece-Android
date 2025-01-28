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
import com.puzzle.domain.model.profile.SnsPlatform

@Composable
internal fun ContactBottomSheet(
    onButtonClicked: (SnsPlatform) -> Unit,
) {
    val scrollState = rememberScrollState()
    var tempSnsPlatform by remember { mutableStateOf<SnsPlatform?>(null) }

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
            SnsPlatform.entries.forEach { sns ->
                if (sns == SnsPlatform.UNKNOWN) return@forEach

                val image = when (sns) {
                    SnsPlatform.KAKAO -> R.drawable.ic_sns_kakao
                    SnsPlatform.OPENKAKAO -> R.drawable.ic_sns_openchatting
                    SnsPlatform.INSTA -> R.drawable.ic_sns_instagram
                    SnsPlatform.PHONE -> R.drawable.ic_sns_call
                    else -> R.drawable.ic_delete_circle
                }

                PieceBottomSheetListItemDefault(
                    label = sns.displayName,
                    image = image,
                    checked = tempSnsPlatform == sns,
                    onChecked = { tempSnsPlatform = sns },
                )
            }
        }

        PieceSolidButton(
            label = "적용하기",
            onClick = { onButtonClicked(tempSnsPlatform!!) },
            enabled = tempSnsPlatform != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 10.dp),
        )
    }
}
