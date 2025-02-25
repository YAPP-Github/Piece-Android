package com.puzzle.domain.usecase.profile

import com.puzzle.domain.model.profile.MyValueTalk
import com.puzzle.domain.spy.repository.SpyProfileRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetMyValueTalksUseCaseTest {
    private lateinit var spyProfileRepository: SpyProfileRepository
    private lateinit var getMyValueTalksUseCase: GetMyValueTalksUseCase

    @BeforeEach
    fun setup() {
        spyProfileRepository = SpyProfileRepository()
        getMyValueTalksUseCase = GetMyValueTalksUseCase(spyProfileRepository)
    }

    @Test
    fun `로컬에서 데이터를 불러오다가 실패했을 경우 서버 데이터를 불러온다`() = runTest {
        // Given
        val remoteValueTalks = dummyValueTalks.map { it.copy(summary = "Updated " + it.summary) }
        spyProfileRepository.setRemoteMyValueTalks(remoteValueTalks)

        // When
        val result = getMyValueTalksUseCase()

        // Then
        assertEquals(remoteValueTalks, result.getOrNull())
        assertEquals(1, spyProfileRepository.loadMyValueTalksCallCount)
    }

    @Test
    fun `로컬에서 데이터를 성공적으로 불러올 경우 서버 요청을 하지 않는다`() = runTest {
        // Given
        val localValueTalks = dummyValueTalks
        spyProfileRepository.setLocalMyValueTalks(localValueTalks)

        // When
        val result = getMyValueTalksUseCase()

        // Then
        assertEquals(localValueTalks, result.getOrNull())
        assertEquals(0, spyProfileRepository.loadMyValueTalksCallCount)
    }

    private val dummyValueTalks = listOf(
        MyValueTalk(
            id = 1,
            category = "커리어",
            title = "나의 개발 여정",
            answer = "대학에서 컴퓨터공학을 전공하고 스타트업에서 junior 개발자로 시작했어요. 꾸준한 학습과 도전을 통해 성장하고 있습니다.",
            summary = "기술에 대한 열정과 성장 마인드셋",
            placeholder = "",
            guides = emptyList(),
        ),
        MyValueTalk(
            id = 2,
            category = "라이프스타일",
            title = "일과 삶의 균형",
            answer = "개발 공부와 개인적인 성장, 취미 활동 사이의 균형을 중요하게 생각해요. 주말에는 주로 카페에서 책을 읽거나 새로운 기술을 공부합니다.",
            summary = "균형 잡힌 삶을 추구하는 개발자",
            placeholder = "",
            guides = emptyList(),
        )
    )
}
