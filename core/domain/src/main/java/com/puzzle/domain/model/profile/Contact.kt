package com.puzzle.domain.model.profile

data class Contact(
    val snsPlatForm: SnsPlatform,
    val content: String,
)

enum class SnsPlatform {
    KAKAO,
    OPENKAKAO,
    INSTA,
    PHONE,
    UNKNOWN;

    companion object {
        fun create(value: String): SnsPlatform {
            return SnsPlatform.entries.firstOrNull { it.name == value } ?: UNKNOWN
        }
    }
}
