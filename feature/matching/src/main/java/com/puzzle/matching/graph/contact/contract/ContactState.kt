package com.puzzle.matching.graph.contact.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.designsystem.R
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.ContactType

data class ContactState(
    val isLoading: Boolean = false,
    val nickName: String = "",
    val contacts: List<Contact> = emptyList(),
    val selectedContact: Contact? = null,
) : MavericksState

internal fun ContactType.getContactIconId(): Int? = when (this) {
    ContactType.KAKAO_TALK_ID -> R.drawable.ic_sns_kakao
    ContactType.OPEN_CHAT_URL -> R.drawable.ic_sns_openchatting
    ContactType.INSTAGRAM_ID -> R.drawable.ic_sns_instagram
    ContactType.PHONE_NUMBER -> R.drawable.ic_sns_call
    ContactType.UNKNOWN -> null
}

internal fun ContactType.getContactNameId(): Int? = when (this) {
    ContactType.KAKAO_TALK_ID -> R.string.maching_contact_kakao_id
    ContactType.OPEN_CHAT_URL -> R.string.maching_contact_open_chat_id
    ContactType.INSTAGRAM_ID -> R.string.maching_contact_insta_id
    ContactType.PHONE_NUMBER -> R.string.maching_contact_phone_number
    ContactType.UNKNOWN -> null
}

internal fun ContactType.getSelectedContactIconId(): Int? = when (this) {
    ContactType.KAKAO_TALK_ID -> R.drawable.ic_kakao_on
    ContactType.OPEN_CHAT_URL -> R.drawable.ic_open_chat_on
    ContactType.INSTAGRAM_ID -> R.drawable.ic_insta_on
    ContactType.PHONE_NUMBER -> R.drawable.ic_phone_on
    ContactType.UNKNOWN -> null
}

internal fun ContactType.getUnSelectedContactIconId(): Int? = when (this) {
    ContactType.KAKAO_TALK_ID -> R.drawable.ic_kakao_off
    ContactType.OPEN_CHAT_URL -> R.drawable.ic_open_chat_off
    ContactType.INSTAGRAM_ID -> R.drawable.ic_insta_off
    ContactType.PHONE_NUMBER -> R.drawable.ic_phone_off
    ContactType.UNKNOWN -> null
}
