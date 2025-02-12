package com.puzzle.domain.model.profile

data class Contact(
    val type: ContactType,
    val content: String,
)

enum class ContactType(val displayName: String) {
    KAKAO_TALK_ID("카카오톡 아이디"),
    OPEN_CHAT_URL("카카오톡 오픈 채팅방"),
    INSTAGRAM_ID("인스타 아이디"),
    PHONE_NUMBER("전화번호"),
    UNKNOWN("");

    companion object {
        fun create(value: String?): ContactType {
            return ContactType.entries.firstOrNull { it.name == value } ?: UNKNOWN
        }
    }
}
