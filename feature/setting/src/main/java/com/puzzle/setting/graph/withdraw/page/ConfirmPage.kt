package com.puzzle.setting.graph.withdraw.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.foundation.PieceTheme

@Composable
internal fun ColumnScope.ConfirmPage(
    onWithdrawClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "정말 탈퇴하시겠어요?",
        style = PieceTheme.typography.headingLSB,
        color = PieceTheme.colors.black,
        modifier = modifier.padding(top = 20.dp),
    )

    Text(
        text = "탈퇴하면 계정과 관련된 모든 정보가 삭제되며\n" +
                "복구할 수 없습니다. 탈퇴하시겠습니까?",
        style = PieceTheme.typography.bodySM,
        color = PieceTheme.colors.dark3,
        modifier = modifier.padding(top = 12.dp, bottom = 60.dp),
    )

    Image(
        painter = painterResource(id = R.drawable.ic_image_default),
        contentDescription = "일러스트",
        contentScale = ContentScale.Crop,
        modifier = modifier.fillMaxWidth(),
    )

    Spacer(modifier = modifier.weight(1f))

    PieceSolidButton(
        label = "탈퇴할래요",
        onClick = onWithdrawClick,
        enabled = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
    )
}

@Preview
@Composable
private fun PreviewConfirmPage() {
    PieceTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PieceTheme.colors.white)
        ) {
            ConfirmPage(
                {},
            )
        }
    }
}
