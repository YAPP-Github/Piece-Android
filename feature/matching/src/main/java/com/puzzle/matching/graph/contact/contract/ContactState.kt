package com.puzzle.matching.graph.contact.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.designsystem.R
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.SnsPlatform

data class ContactState(
    val isLoading: Boolean = false,
    val nickName: String = "",
    val contacts: List<Contact> = emptyList(),
    val selectedContact: Contact? = contacts.firstOrNull()
) : MavericksState

internal fun SnsPlatform.getContactIconId(): Int? = when (this) {
    SnsPlatform.KAKAO_TALK_ID -> R.drawable.ic_sns_kakao
    SnsPlatform.OPEN_CHAT_URL -> R.drawable.ic_sns_openchatting
    SnsPlatform.INSTAGRAM_ID -> R.drawable.ic_sns_instagram
    SnsPlatform.PHONE_NUMBER -> R.drawable.ic_sns_call
    SnsPlatform.UNKNOWN -> null
}

internal fun SnsPlatform.getContactNameId(): Int? = when (this) {
    SnsPlatform.KAKAO_TALK_ID -> R.string.maching_contact_kakao_id
    SnsPlatform.OPEN_CHAT_URL -> R.string.maching_contact_open_chat_id
    SnsPlatform.INSTAGRAM_ID -> R.string.maching_contact_insta_id
    SnsPlatform.PHONE_NUMBER -> R.string.maching_contact_phone_number
    SnsPlatform.UNKNOWN -> null
}

internal fun SnsPlatform.getSelectedContactIconId(): Int? = when (this) {
    SnsPlatform.KAKAO_TALK_ID -> R.drawable.ic_kakao_on
    SnsPlatform.OPEN_CHAT_URL -> R.drawable.ic_open_chat_on
    SnsPlatform.INSTAGRAM_ID -> R.drawable.ic_insta_on
    SnsPlatform.PHONE_NUMBER -> R.drawable.ic_phone_on
    SnsPlatform.UNKNOWN -> null
}

internal fun SnsPlatform.getUnSelectedContactIconId(): Int? = when (this) {
    SnsPlatform.KAKAO_TALK_ID -> R.drawable.ic_kakao_off
    SnsPlatform.OPEN_CHAT_URL -> R.drawable.ic_open_chat_off
    SnsPlatform.INSTAGRAM_ID -> R.drawable.ic_insta_off
    SnsPlatform.PHONE_NUMBER -> R.drawable.ic_phone_off
    SnsPlatform.UNKNOWN -> null
}
