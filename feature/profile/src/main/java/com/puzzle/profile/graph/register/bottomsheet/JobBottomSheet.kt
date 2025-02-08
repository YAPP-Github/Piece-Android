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
import com.puzzle.profile.graph.register.contract.Job
import kotlinx.coroutines.delay

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

    LaunchedEffect(tempSelectedJob) {
        if (tempSelectedJob == Job.OTHER.displayName) {
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
                                modifier = Modifier.padding(bottom = 16.dp),
                            ) {
                                if (tempOtherJob.isNotEmpty()) {
                                    Image(
                                        painter = painterResource(R.drawable.ic_delete_circle),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(start = 8.dp)
                                            .size(20.dp)
                                            .clickable { tempOtherJob = "" },
                                    )
                                }
                            }
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
                    if (tempSelectedJob == Job.OTHER.displayName) tempOtherJob
                    else tempSelectedJob
                )
            },
            enabled = (tempSelectedJob.isNotEmpty() && tempSelectedJob != Job.OTHER.displayName)
                    || (tempSelectedJob == Job.OTHER.displayName && tempOtherJob.isNotEmpty()),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 10.dp),
        )
    }
}
