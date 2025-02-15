package com.puzzle.domain.model.profile

data class MyProfileBasic(
    val description: String,
    val nickname: String,
    val age: Int,
    val birthdate: String,
    val height: Int,
    val weight: Int,
    val location: String,
    val job: String,
    val smokingStatus: String,
    val snsActivityLevel: String,
    val imageUrl: String,
    val contacts: List<Contact>,
) {
    fun isSmoke(): Boolean = if (smokingStatus == "흡연") true else false
    fun isSnsActive(): Boolean = if (snsActivityLevel == "활동") true else false
}

data class MyValuePick(
    val id: Int,
    val category: String,
    val question: String,
    val answerOptions: List<AnswerOption>,
    val selectedAnswer: Int,
)

data class MyValueTalk(
    val id: Int,
    val category: String,
    val title: String,
    val answer: String,
    val summary: String,
    val guides: List<String>,
)
