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
import com.puzzle.profile.graph.register.contract.Job

@Composable
internal fun JobBottomSheet(
    selectedJob: String,
    updateSelectJob: (String) -> Unit,
) {
    val scrollState = rememberScrollState()
    var tempSelectedJob by remember(selectedJob) { mutableStateOf(selectedJob) }
    var tempOtherJob by remember(selectedJob) {
        mutableStateOf(
            if (selectedJob in Job.entries
                    .map { it.displayName }
                    .toSet()
            ) {
                ""
            } else {
                selectedJob
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {}
            .padding(horizontal = 20.dp),
    ) {
        PieceBottomSheetHeader(title = "직업")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 400.dp)
                .padding(top = 12.dp)
                .verticalScroll(scrollState),
        ) {
            Job.entries.forEach { job ->
                if (job == Job.OTHER) {
                    PieceBottomSheetListItemExpandable(
                        label = job.displayName,
                        checked = job.displayName == tempSelectedJob,
                        onChecked = { tempSelectedJob = job.displayName },
                        expandableContent = {
                            PieceTextInputDefault(
                                value = tempOtherJob,
                                hint = "자유롭게 작성해주세요",
                                onValueChange = { tempOtherJob = it },
                            )
                        },
                    )
                } else {
                    PieceBottomSheetListItemDefault(
                        label = job.displayName,
                        checked = job.displayName == tempSelectedJob,
                        onChecked = { tempSelectedJob = job.displayName },
                    )
                }
            }
        }

        PieceSolidButton(
            label = "적용하기",
            onClick = {
                updateSelectJob(
                    if (tempSelectedJob in Job.entries
                            .map { it.displayName }
                            .toSet()
                    ) {
                        tempSelectedJob
                    } else {
                        tempOtherJob
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 10.dp),
        )
    }
}
