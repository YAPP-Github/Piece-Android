package com.puzzle.domain.model.profile

data class OpponentProfile(
    val description: String,
    val nickname: String,
    val age: Int,
    val birthYear: String,
    val height: Int,
    val weight: Int,
    val location: String,
    val job: String,
    val smokingStatus: String,
    val valuePicks: List<OpponentValuePick>,
    val valueTalks: List<OpponentValueTalk>,
    val imageUrl: String,
)

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

data class OpponentValuePick(
    val category: String,
    val question: String,
    val answerOptions: List<AnswerOption>,
    val selectedAnswer: Int,
    val isSameWithMe: Boolean,
)

data class OpponentValueTalk(
    val category: String,
    val summary: String,
    val answer: String,
)
