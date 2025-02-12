package com.puzzle.network.model.profile

import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.ContactType
import com.puzzle.domain.model.profile.MyProfileBasic
import com.puzzle.network.model.UNKNOWN_INT
import com.puzzle.network.model.UNKNOWN_STRING
import kotlinx.serialization.Serializable

@Serializable
data class GetMyProfileBasicResponse(
    val description: String?,
    val nickname: String?,
    val age: Int?,
    val birthDate: String?,
    val height: Int?,
    val weight: Int?,
    val location: String?,
    val job: String?,
    val smokingStatus: String?,
    val snsActivityLevel: String?,
    val imageUrl: String?,
    val contacts: List<ContactResponse>?,
) {
    fun toDomain() = MyProfileBasic(
        description = description ?: UNKNOWN_STRING,
        nickname = nickname ?: UNKNOWN_STRING,
        age = age ?: UNKNOWN_INT,
        birthDate = birthDate ?: UNKNOWN_STRING,
        height = height ?: UNKNOWN_INT,
        weight = weight ?: UNKNOWN_INT,
        location = location ?: UNKNOWN_STRING,
        job = job ?: UNKNOWN_STRING,
        smokingStatus = smokingStatus ?: UNKNOWN_STRING,
        imageUrl = imageUrl ?: UNKNOWN_STRING,
        contacts = contacts?.map(ContactResponse::toDomain) ?: emptyList()
    )
}

@Serializable
data class ContactResponse(
    val type: String?,
    val value: String?,
) {
    fun toDomain() = Contact(
        type = ContactType.create(type),
        content = value ?: UNKNOWN_STRING,
    )
}
