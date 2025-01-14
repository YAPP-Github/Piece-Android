package com.puzzle.common.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

class CollapsingHeaderNestedScrollConnection(
    private val headerHeight: Int
) : NestedScrollConnection {

    // 헤더 offset(픽셀 단위), 0이면 펼침, -headerHeight이면 완전 접힘
    var headerOffset: Int by mutableIntStateOf(0)
        private set

    // 스크롤 이벤트가 오기 전, 먼저 얼마나 소모할지 계산
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        // y축 델타(수직 스크롤 양)
        val delta = available.y.toInt()

        // 새 offset = 기존 offset + 스크롤 델타
        val newOffset = headerOffset + delta
        val previousOffset = headerOffset

        // -headerHeight ~ 0 사이로 제한
        //   -> 최소 -105: 완전히 접힘, 최대 0: 완전히 펼침
        headerOffset = newOffset.coerceIn(-headerHeight, 0)

        // 소비(consumed)된 스크롤 양 = (바뀐 offset - 기존 offset)
        val consumed = headerOffset - previousOffset

        // x축은 소비 안 함(0f), y축은 consumed만큼 소비했다고 반환
        return Offset(0f, consumed.toFloat())
    }
}
