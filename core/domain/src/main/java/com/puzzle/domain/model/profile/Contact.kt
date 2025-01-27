package com.puzzle.domain.model.profile

data class Contact(
    val snsPlatForm: SnsPlatform,
    val content: String,
)

enum class SnsPlatform {
    KAKAO_TALK,
    KAKAO_OPENCCHATTING,
    INSTAGRAM,
    PHONENUMBER,
}
