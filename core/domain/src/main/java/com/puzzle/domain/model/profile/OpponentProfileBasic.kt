package com.puzzle.domain.model.profile

data class OpponentProfileBasic(
    val description: String,
    val nickname: String,
    val age: Int,
    val birthYear: String,
    val height: Int,
    val weight: Int,
    val location: String,
    val job: String,
    val smokingStatus: String,
)
