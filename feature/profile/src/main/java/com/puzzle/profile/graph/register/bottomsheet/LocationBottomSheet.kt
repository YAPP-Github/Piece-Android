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
import com.puzzle.designsystem.component.PieceBottomSheetHeader
import com.puzzle.designsystem.component.PieceBottomSheetListItemDefault
import com.puzzle.designsystem.component.PieceBottomSheetListItemExpandable
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.component.PieceTextInputDefault
import com.puzzle.profile.graph.register.contract.Location

@Composable
internal fun LocationBottomSheet(
    selectedLocation: String,
    updateSelectLocation: (String) -> Unit,
) {
    val scrollState = rememberScrollState()
    var tempSelectedLocation by remember(selectedLocation) { mutableStateOf(selectedLocation) }
    var tempOtherLocation by remember(selectedLocation) {
        mutableStateOf(
            if (selectedLocation in Location.entries
                    .map { it.displayName }
                    .toSet()
            ) {
                ""
            } else {
                selectedLocation
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {}
            .padding(horizontal = 20.dp),
    ) {
        PieceBottomSheetHeader(
            title = "활동 지역",
            subTitle = "주로 활동하는 지역을 선택해주세요."
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 400.dp)
                .padding(top = 12.dp)
                .verticalScroll(scrollState),
        ) {
            Location.entries.forEach { location ->
                if (location == Location.OTHER) {
                    PieceBottomSheetListItemExpandable(
                        label = location.displayName,
                        checked = location.displayName == tempSelectedLocation,
                        onChecked = { tempSelectedLocation = location.displayName },
                        expandableContent = {
                            PieceTextInputDefault(
                                value = tempOtherLocation,
                                hint = "자유롭게 작성해주세요",
                                onValueChange = { tempOtherLocation = it },
                            )
                        },
                    )
                } else {
                    PieceBottomSheetListItemDefault(
                        label = location.displayName,
                        checked = location.displayName == tempSelectedLocation,
                        onChecked = { tempSelectedLocation = location.displayName },
                    )
                }
            }
        }

        PieceSolidButton(
            label = "적용하기",
            onClick = {
                updateSelectLocation(
                    if (tempSelectedLocation in Location.entries
                            .map { it.displayName }
                            .toSet()
                    ) {
                        tempSelectedLocation
                    } else {
                        tempOtherLocation
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 10.dp),
        )
    }
}
