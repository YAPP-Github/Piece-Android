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

private val DecimalBook = FontFamily(
    Font(
        resId = R.font.decimal_book,
        weight = FontWeight.Normal,
    )
)

@Immutable
data class PieceTypography(
    val heading1: TextStyle = TextStyle(
        fontFamily = PretendardSemiBold,
        fontSize = 28.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.28).sp, // -1% of 28sp
    ),
    val heading2: TextStyle = TextStyle(
        fontFamily = PretendardSemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.24).sp, // -1% of 24sp
    ),
    val heading3: TextStyle = TextStyle(
        fontFamily = PretendardSemiBold,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.2).sp, // -1% of 20sp
    ),
    val heading4: TextStyle = TextStyle(
        fontFamily = PretendardSemiBold,
        fontSize = 18.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.18).sp, // -1% of 18sp
    ),
    val heading5: TextStyle = TextStyle(
        fontFamily = PretendardMedium,
        fontSize = 18.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.18).sp,
    ),
    val body1: TextStyle = TextStyle(
        fontFamily = PretendardSemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.16).sp,
    ),
    val body2: TextStyle = TextStyle(
        fontFamily = PretendardMedium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.16).sp,
    ),
    val body3: TextStyle = TextStyle(
        fontFamily = PretendardMedium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.16).sp,
    ),
    val body4: TextStyle = TextStyle(
        fontFamily = PretendardSemiBold,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.14).sp,
    ),
    val body5: TextStyle = TextStyle(
        fontFamily = PretendardMedium,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.14).sp,
    ),
    val caption1: TextStyle = TextStyle(
        fontFamily = PretendardMedium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = (-0.12).sp,
    ),
    val decimalBook: TextStyle = TextStyle(
        fontFamily = DecimalBook,
        fontSize = 16.sp,
        lineHeight = (19.2).sp,
    )
)
