@file:OptIn(ExperimentalMaterial3Api::class)

package com.puzzle.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.puzzle.designsystem.R
import com.puzzle.designsystem.foundation.PieceTheme

@Composable
fun PieceModalBottomSheet(
    sheetState: ModalBottomSheetState,
    modifier: Modifier = Modifier,
    sheetContent: @Composable (() -> Unit)?,
    content: @Composable () -> Unit,
) {
    ModalBottomSheetLayout(
        sheetGesturesEnabled = false,
        sheetContentColor = PieceTheme.colors.white,
        sheetBackgroundColor = PieceTheme.colors.white,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetState = sheetState,
        sheetContent = {
            Column {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(28.dp)
                        .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                )

                sheetContent?.invoke()
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(WindowInsets.navigationBars.asPaddingValues()),
    ) {
        content()
    }
}

@Composable
fun PieceBottomSheetHeader(
    title: String,
    subTitle: String? = null,
) {
    Text(
        text = title,
        style = PieceTheme.typography.headingLSB,
        color = PieceTheme.colors.black,
        modifier = Modifier.padding(top = 10.dp),
    )

    subTitle?.let {
        Text(
            text = subTitle,
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.dark2,
            modifier = Modifier.padding(vertical = 8.dp),
        )
    }
}

@Composable
fun PieceBottomSheetListItemDefault(
    label: String,
    checked: Boolean,
    onChecked: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    @DrawableRes image: Int? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(62.dp)
            .clickable(enabled = enabled) { onChecked() },
    ) {
        if (image != null) {
            Image(
                painter = painterResource(image),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    color = if (!enabled) PieceTheme.colors.dark3
                    else if (checked) PieceTheme.colors.primaryDefault
                    else PieceTheme.colors.dark1,
                ),
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 10.dp),
            )
        }

        Text(
            text = label,
            style = if (checked) PieceTheme.typography.bodyMSB else PieceTheme.typography.bodyMM,
            color = if (!enabled) PieceTheme.colors.dark3
            else if (checked) PieceTheme.colors.primaryDefault
            else PieceTheme.colors.black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )

        if (!enabled || checked) {
            Image(
                painter = painterResource(R.drawable.ic_textinput_check),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    color = if (!enabled) PieceTheme.colors.dark3
                    else PieceTheme.colors.primaryDefault,
                ),
                modifier = Modifier
                    .size(32.dp)
                    .padding(start = 36.dp),
            )
        }
    }
}

@Composable
fun PieceBottomSheetListItemExpandable(
    label: String,
    checked: Boolean,
    onChecked: () -> Unit,
    modifier: Modifier = Modifier,
    expandableContent: @Composable () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .height(62.dp)
                .clickable { onChecked() },
        ) {
            Text(
                text = label,
                style = if (checked) PieceTheme.typography.bodyMSB else PieceTheme.typography.bodyMM,
                color = if (checked) PieceTheme.colors.primaryDefault else PieceTheme.colors.black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )

            if (checked) {
                Image(
                    painter = painterResource(R.drawable.ic_textinput_check),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 36.dp)
                        .size(32.dp),
                )
            }
        }

        AnimatedVisibility(
            visible = checked,
            modifier = Modifier.fillMaxWidth(),
        ) {
            expandableContent()
        }
    }
}

@Preview
@Composable
private fun PreviewModalBottomSheet() {
    val mocklocationList = listOf(
        "서울특별시", "경기도", "부산광역시", "대구광역시", "인천광역시",
        "광주광역시", "대전광역시", "울산광역시", "세종특별자치시", "강원도",
        "충청북도", "충청남도", "전라북도", "전라남도", "경상북도",
        "경상남도", "제주특별자치도", "기타"
    )

    PieceTheme {
        PieceModalBottomSheet(
            sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Expanded),
            sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(Unit) {}
                        .padding(horizontal = 20.dp)
                ) {
                    PieceBottomSheetHeader(
                        title = "활동 지역",
                        subTitle = "주로 활동하는 지역을 선택해주세요."
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                            .padding(top = 12.dp),
                    ) {
                        items(mocklocationList) { location ->
                            PieceBottomSheetListItemDefault(
                                label = location,
                                checked = location == "서울특별시",
                                onChecked = {},
                            )
                        }
                    }

                    PieceSolidButton(
                        label = "적용하기",
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, bottom = 10.dp),
                    )
                }
            },
            content = {}
        )
    }
}
