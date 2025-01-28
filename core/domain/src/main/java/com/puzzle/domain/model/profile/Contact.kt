package com.puzzle.domain.model.profile

data class Contact(
    val snsPlatform: SnsPlatform,
    val content: String,
)

enum class SnsPlatform(val displayName: String) {
    KAKAO("카카오톡 아이디"),
    OPENKAKAO("카카오톡 오픈 채팅방"),
    INSTA("인스타 아이디"),
    PHONE("전화번호"),
    UNKNOWN("");

    companion object {
        fun create(value: String): SnsPlatform {
            return SnsPlatform.entries.firstOrNull { it.name == value } ?: UNKNOWN
        }
    }
}
