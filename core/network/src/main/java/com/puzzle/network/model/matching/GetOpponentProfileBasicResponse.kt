package com.puzzle.network.model.matching

import com.puzzle.domain.model.profile.OpponentProfileBasic
import com.puzzle.network.model.UNKNOWN_INT
import com.puzzle.network.model.UNKNOWN_STRING
import kotlinx.serialization.Serializable

@Serializable
data class GetOpponentProfileBasicResponse(
    val matchId: Int?,
    val description: String?,
    val nickname: String?,
    val age: Int?,
    val birthYear: String?,
    val height: Int?,
    val weight: Int?,
    val location: String?,
    val job: String?,
    val smokingStatus: String?,
) {
    fun toDomain() = OpponentProfileBasic(
        description = description ?: UNKNOWN_STRING,
        nickname = nickname ?: UNKNOWN_STRING,
        age = age ?: UNKNOWN_INT,
        birthYear = birthYear ?: UNKNOWN_STRING,
        height = height ?: UNKNOWN_INT,
        weight = weight ?: UNKNOWN_INT,
        location = location ?: UNKNOWN_STRING,
        job = job ?: UNKNOWN_STRING,
        smokingStatus = smokingStatus ?: UNKNOWN_STRING,
    )
}
