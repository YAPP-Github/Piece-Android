package com.puzzle.profile.graph.register.bottomsheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.puzzle.common.ui.clickable
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceBottomSheetHeader
import com.puzzle.designsystem.component.PieceBottomSheetListItemDefault
import com.puzzle.designsystem.component.PieceBottomSheetListItemExpandable
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.component.PieceTextInputDefault
import com.puzzle.profile.graph.register.contract.RegisterProfileState.Location
import kotlinx.coroutines.delay

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

    LaunchedEffect(tempSelectedLocation) {
        if (tempSelectedLocation == Location.OTHER.displayName) {
            delay(200L)
            scrollState.animateScrollTo(scrollState.maxValue)
        }
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
                                modifier = Modifier.padding(bottom = 16.dp),
                            ) {
                                if (tempOtherLocation.isNotEmpty()) {
                                    Image(
                                        painter = painterResource(R.drawable.ic_delete_circle),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(start = 8.dp)
                                            .size(20.dp)
                                            .clickable { tempOtherLocation = "" },
                                    )
                                }
                            }
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
                    if (tempSelectedLocation == Location.OTHER.displayName) tempOtherLocation
                    else tempSelectedLocation
                )
            },
            enabled = (tempSelectedLocation.isNotEmpty() && tempSelectedLocation != Location.OTHER.displayName) ||
                    (tempSelectedLocation == Location.OTHER.displayName && tempOtherLocation.isNotEmpty()),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 10.dp),
        )
    }
}
