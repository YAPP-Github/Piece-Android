package com.puzzle.profile.graph.basic.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.SnsPlatform

data class BasicProfileState(
    val isEdited: Boolean = false,
    val nickName: String = "수줍은 수달",
    val isCheckingButtonAvailable: Boolean = false,
    val nickNameInputState: NickNameInputState = NickNameInputState.VALID_LENGTH,
    val description: String = "요리와 음악을 좋아하는",
    val birthdate: String = "19990909",
    val location: String = "서울특별시",
    val height: String = "180",
    val weight: String = "72",
    val job: String = "프리랜서",
    val isSmoke: Boolean = false,
    val isSnsActive: Boolean = false,
    val contacts: List<Contact> = listOf(
        Contact(
            snsPlatform = SnsPlatform.KAKAO,
            content = "puzzle1234",
        ),
        Contact(
            snsPlatform = SnsPlatform.INSTA,
            content = "puzzle1234",
        ),
        Contact(
            snsPlatform = SnsPlatform.PHONE,
            content = "010-0000-0000",
        ),
        Contact(
            snsPlatform = SnsPlatform.OPENKAKAO,
            content = "https://open.kakao.com/o/s5aqIX1g",
        ),
    ),
) : MavericksState {
    val usingSnsPlatforms = contacts.map { it.snsPlatform }
        .toSet()

    enum class NickNameInputState {
        EXCEEDS_MAX_LENGTH,   // 글자 수 7 이상
        EMPTY,                // 아무 글자도 없음 (0)
        VALID_LENGTH,         // 길이는 정상(1~6)
        ALREADY_IN_USE,       // API 결과: 중복
        AVAILABLE,            // API 결과: 사용 가능
        NEEDS_DUPLICATE_CHECK // 중복 검사 안하고 저장 버튼 눌렀을 때
    }
}