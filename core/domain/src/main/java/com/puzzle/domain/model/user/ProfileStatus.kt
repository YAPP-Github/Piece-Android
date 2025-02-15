package com.puzzle.domain.model.user

enum class ProfileStatus {
    //프로필 생성후 초기 상태 (관리자가 심사를 하지도 않음)
    INCOMPLETE,

    // 프로필 반려됨 (관리자가 거절함)
    REJECTED,

    // 프로필 수정됨 (사용자가 거절된 것을 인지하고 프로필을 수정함 )
    REVISED,

    // 승인 완료 (관리자가 프로필을 승인함)
    APPROVED,

    // 에러
    UNKNOWN
    ;

    companion object {
        fun fromName(name: String): ProfileStatus = entries.find { it.name == name } ?: UNKNOWN
    }
}