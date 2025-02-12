package com.puzzle.domain.model.profile

data class MyProfileBasic(
    val description: String,
    val nickname: String,
    val age: Int,
    val birthDate: String,
    val height: Int,
    val weight: Int,
    val location: String,
    val job: String,
    val smokingStatus: String,
    val imageUrl: String,
    val contacts: List<Contact>,
)
