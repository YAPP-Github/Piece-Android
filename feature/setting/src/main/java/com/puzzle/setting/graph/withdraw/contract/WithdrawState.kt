package com.puzzle.setting.graph.withdraw.contract

import com.airbnb.mvrx.MavericksState

data class WithdrawState(
    val currentPage: WithdrawPage = WithdrawPage.REASON,
    val isLoading: Boolean = false,
    val selectedReason: WithdrawReason? = null
) : MavericksState {

    enum class WithdrawReason(val label: String) {
        FoundPartner("애인이 생겼어요"),
        NeedRest("잠깐 쉬고 싶어요"),
        NoMatch("매칭이 안 이루어져요"),
        PoorUsability("어플 사용성이 별로에요"),
        Other("기타"),
    }

    enum class WithdrawPage {
        REASON,
        CONFIRM;

        companion object {
            fun getNextPage(currentPage: WithdrawPage): WithdrawPage {
                return when (currentPage) {
                    REASON -> CONFIRM
                    CONFIRM -> CONFIRM
                }
            }

            fun getPreviousPage(currentPage: WithdrawPage): WithdrawPage {
                return when (currentPage) {
                    CONFIRM -> REASON
                    REASON -> REASON
                }
            }
        }
    }
}
