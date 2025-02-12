package com.puzzle.domain.usecase.profile

import com.puzzle.domain.model.profile.AnswerOption
import com.puzzle.domain.model.profile.MyValuePick
import com.puzzle.domain.spy.repository.SpyProfileRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetMyValuePicksUseCaseTest {
    private lateinit var spyProfileRepository: SpyProfileRepository
    private lateinit var getMyValuePicksUseCase: GetMyValuePicksUseCase

    @BeforeEach
    fun setup() {
        spyProfileRepository = SpyProfileRepository()
        getMyValuePicksUseCase = GetMyValuePicksUseCase(spyProfileRepository)
    }

    @Test
    fun `로컬에서 데이터를 불러오다가 실패했을 경우 서버 데이터를 불러온다`() = runTest {
        // Given
        val remoteValuePicks =
            dummyValuePicks.map { it.copy(selectedAnswer = it.selectedAnswer + 1) }

        spyProfileRepository.setRemoteMyValuePicks(remoteValuePicks)

        // When
        val result = getMyValuePicksUseCase()

        // Then
        assertEquals(remoteValuePicks, result.getOrNull())
        assertEquals(1, spyProfileRepository.loadMyValuePicksCallCount)
    }

    @Test
    fun `로컬에서 데이터를 성공적으로 불러올 경우 서버 요청을 하지 않는다`() = runTest {
        // Given
        val localValuePicks = dummyValuePicks
        spyProfileRepository.setLocalMyValuePicks(localValuePicks)

        // When
        val result = getMyValuePicksUseCase()

        // Then
        assertEquals(localValuePicks, result.getOrNull())
        assertEquals(0, spyProfileRepository.loadMyValuePicksCallCount)
    }

    private val dummyValuePicks = listOf(
        MyValuePick(
            id = 1,
            category = "인생의 가치",
            question = "당신에게 가장 중요한 것은 무엇인가요?",
            answerOptions = listOf(
                AnswerOption(1, "성장과 배움"),
                AnswerOption(2, "가족과 관계"),
                AnswerOption(3, "자아실현"),
                AnswerOption(4, "안정과 평화")
            ),
            selectedAnswer = 1
        ),
        MyValuePick(
            id = 2,
            category = "연애관",
            question = "좋은 파트너에게 가장 중요하게 생각하는 것은?",
            answerOptions = listOf(
                AnswerOption(1, "진실성"),
                AnswerOption(2, "공감능력"),
                AnswerOption(3, "지적 호기심"),
                AnswerOption(4, "유머감각")
            ),
            selectedAnswer = 2
        )
    )
}
