package com.puzzle.designsystem.foundation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.puzzle.designsystem.R

private val PretendardBold = FontFamily(
    Font(
        resId = R.font.pretendard_bold,
        weight = FontWeight.Bold,
    ),
)

private val PretendardSemiBold = FontFamily(
    Font(
        resId = R.font.pretendard_semi_bold,
        weight = FontWeight.SemiBold,
    ),
)

private val PretendardMedium = FontFamily(
    Font(
        resId = R.font.pretendard_medium,
        weight = FontWeight.Medium,
    ),
)

private val PretendardRegular = FontFamily(
    Font(
        resId = R.font.pretendard_medium,
        weight = FontWeight.Normal,
    ),
)

@Immutable
data class PieceTypography(
    val headingXLSB: TextStyle = TextStyle(
        fontFamily = PretendardSemiBold,
        fontSize = 28.sp,
        lineHeight = 40.sp,
    ),
    val headingLSB: TextStyle = TextStyle(
        fontFamily = PretendardSemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
    ),
    val headingMSB: TextStyle = TextStyle(
        fontFamily = PretendardSemiBold,
        fontSize = 20.sp,
        lineHeight = 24.sp,
    ),
    val headingSSB: TextStyle = TextStyle(
        fontFamily = PretendardSemiBold,
        fontSize = 18.sp,
        lineHeight = 22.sp,
    ),
    val headingSM: TextStyle = TextStyle(
        fontFamily = PretendardMedium,
        fontSize = 18.sp,
        lineHeight = 22.sp,
    ),
    val bodyMSB: TextStyle = TextStyle(
        fontFamily = PretendardSemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    val bodyMM: TextStyle = TextStyle(
        fontFamily = PretendardMedium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    val bodyMR: TextStyle = TextStyle(
        fontFamily = PretendardRegular,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    val bodySSB: TextStyle = TextStyle(
        fontFamily = PretendardSemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    val bodySM: TextStyle = TextStyle(
        fontFamily = PretendardMedium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    val captionM: TextStyle = TextStyle(
        fontFamily = PretendardMedium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
)
