package com.puzzle.data.repository

import com.puzzle.data.fake.source.matching.FakeLocalMatchingDataSource
import com.puzzle.data.fake.source.matching.FakeMatchingDataSource
import com.puzzle.domain.model.profile.OpponentProfile
import com.puzzle.domain.model.profile.OpponentValuePick
import com.puzzle.domain.model.profile.OpponentValueTalk
import com.puzzle.network.model.matching.GetOpponentProfileBasicResponse
import com.puzzle.network.model.matching.GetOpponentProfileImageResponse
import com.puzzle.network.model.matching.GetOpponentValuePicksResponse
import com.puzzle.network.model.matching.GetOpponentValueTalksResponse
import com.puzzle.network.model.matching.OpponentValuePickResponse
import com.puzzle.network.model.matching.OpponentValueTalkResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MatchingRepositoryImplTest {

    private lateinit var matchingRepository: MatchingRepositoryImpl
    private lateinit var fakeMatchingDataSource: FakeMatchingDataSource
    private lateinit var fakeLocalMatchingDataSource: FakeLocalMatchingDataSource

    @BeforeEach
    fun setUp() {
        fakeMatchingDataSource = FakeMatchingDataSource()
        fakeLocalMatchingDataSource = FakeLocalMatchingDataSource()
        matchingRepository = MatchingRepositoryImpl(
            localMatchingDataSource = fakeLocalMatchingDataSource,
            matchingDataSource = fakeMatchingDataSource
        )
    }

    @Test
    fun `매칭된 상대방 정보는 로컬에 저장한다`() = runTest {
        // Given
        val expectedOpponentProfile = OpponentProfile(
            description = "안녕하세요",
            nickname = "테스트",
            age = 25,
            birthYear = "1998",
            height = 170,
            weight = 60,
            location = "서울",
            job = "개발자",
            smokingStatus = "비흡연",
            valuePicks = listOf(
                OpponentValuePick(
                    category = "취미",
                    question = "당신의 취미는 무엇인가요?",
                    isSameWithMe = true,
                    answerOptions = emptyList(),
                    selectedAnswer = 1
                )
            ),
            valueTalks = listOf(
                OpponentValueTalk(
                    category = "음악",
                    summary = "좋아하는 음악 장르",
                    answer = "저는 클래식 음악을 좋아합니다."
                )
            ),
            imageUrl = "https://example.com/image.jpg"
        )

        fakeMatchingDataSource.setOpponentProfileData(
            GetOpponentProfileBasicResponse(
                matchId = 1,
                description = expectedOpponentProfile.description,
                nickname = expectedOpponentProfile.nickname,
                age = expectedOpponentProfile.age,
                birthYear = expectedOpponentProfile.birthYear,
                height = expectedOpponentProfile.height,
                weight = expectedOpponentProfile.weight,
                location = expectedOpponentProfile.location,
                job = expectedOpponentProfile.job,
                smokingStatus = expectedOpponentProfile.smokingStatus
            ),
            GetOpponentValuePicksResponse(
                matchId = 1,
                description = null,
                nickname = null,
                valuePicks = expectedOpponentProfile.valuePicks.map {
                    OpponentValuePickResponse(
                        category = it.category,
                        question = it.question,
                        isSameWithMe = it.isSameWithMe,
                        answerOptions = null,
                        selectedAnswer = it.selectedAnswer
                    )
                }
            ),
            GetOpponentValueTalksResponse(
                matchId = 1,
                description = null,
                nickname = null,
                valueTalks = expectedOpponentProfile.valueTalks.map {
                    OpponentValueTalkResponse(
                        category = it.category,
                        summary = it.summary,
                        answer = it.answer
                    )
                }
            ),
            GetOpponentProfileImageResponse(imageUrl = expectedOpponentProfile.imageUrl)
        )

        // When
        matchingRepository.loadOpponentProfile().getOrThrow()

        // Then
        val storedProfile = fakeLocalMatchingDataSource.opponentProfile.first()
        assertEquals(expectedOpponentProfile, storedProfile)
    }
}
