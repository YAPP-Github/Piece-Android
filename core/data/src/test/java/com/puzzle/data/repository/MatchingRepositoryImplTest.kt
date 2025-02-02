package com.puzzle.data.repository

import com.puzzle.database.source.LocalMatchingDataSource
import com.puzzle.network.model.matching.LoadValuePicksResponse
import com.puzzle.network.model.matching.LoadValueTalksResponse
import com.puzzle.network.model.matching.ValuePickAnswerResponse
import com.puzzle.network.model.matching.ValuePickResponse
import com.puzzle.network.model.matching.ValueTalkResponse
import com.puzzle.network.source.MatchingDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MatchingRepositoryImplTest {

    private lateinit var matchingDataSource: MatchingDataSource
    private lateinit var localMatchingDataSource: LocalMatchingDataSource
    private lateinit var matchingRepository: MatchingRepositoryImpl

    @BeforeEach
    fun setUp() {
        matchingDataSource = mockk()
        localMatchingDataSource = mockk()
        matchingRepository = MatchingRepositoryImpl(matchingDataSource, localMatchingDataSource)
    }

    @Test
    fun `가치관Pick을 새로 갱신할 경우 id값이 올바르게 내려오지 않은 가치관Pick은 무시한다`() = runTest {
        // given
        val invalidValuePick = ValuePickResponse(
            id = null,
            category = null,
            question = null,
            answers = null
        )
        val validValuePick = ValuePickResponse(
            id = 1,
            category = "음주",
            question = "술을 마시는 것에 대해 어떻게 생각하나요?",
            answers = listOf(
                ValuePickAnswerResponse(1, "함께 즐기고 싶어요"),
                ValuePickAnswerResponse(2, "같이 즐길 수 없어도 괜찮아요")
            )
        )

        coEvery { matchingDataSource.loadValuePicks() } returns
                Result.success(LoadValuePicksResponse(listOf(invalidValuePick, validValuePick)))
        coEvery { localMatchingDataSource.replaceValuePicks(any()) } returns Result.success(Unit)

        // when
        val result = matchingRepository.loadValuePicks()

        // then
        assertTrue(result.isSuccess)
        coVerify(exactly = 1) {
            localMatchingDataSource.replaceValuePicks(
                match {
                    it.size == 1 && it.first().valuePickQuestion.id == validValuePick.id
                }
            )
        }
    }

    @Test
    fun `가치관Talk을 새로 갱신할 경우 id값이 올바르게 내려오지 않은 가치관Talk은 무시한다`() = runTest {
        // given
        val invalidValueTalk = ValueTalkResponse(
            id = null,
            category = null,
            title = null,
            guide = null
        )
        val validValueTalk = ValueTalkResponse(
            id = 1,
            category = "음주",
            title = "술자리에 대한 대화",
            guide = null
        )

        coEvery { matchingDataSource.loadValueTalks() } returns
                Result.success(LoadValueTalksResponse(listOf(invalidValueTalk, validValueTalk)))
        coEvery { localMatchingDataSource.replaceValueTalks(any()) } returns Result.success(Unit)

        // when
        val result = matchingRepository.loadValueTalks()

        // then
        assertTrue(result.isSuccess)
        coVerify(exactly = 1) {
            localMatchingDataSource.replaceValueTalks(
                match {
                    it.size == 1 && it.first().id == validValueTalk.id
                }
            )
        }
    }

    @Test
    fun `갱신한 가치관Pick 데이터는 로컬 데이터베이스에 저장한다`() = runTest {
        // given
        val validValuePicks = listOf(
            ValuePickResponse(
                id = 1,
                category = "음주",
                question = "술을 마시는 것에 대해 어떻게 생각하나요?",
                answers = listOf(
                    ValuePickAnswerResponse(1, "함께 즐기고 싶어요"),
                    ValuePickAnswerResponse(2, "같이 즐길 수 없어도 괜찮아요")
                )
            ),
            ValuePickResponse(
                id = 2,
                category = "취미",
                question = "좋아하는 취미가 있나요?",
                answers = listOf(
                    ValuePickAnswerResponse(1, "여행을 좋아해요"),
                    ValuePickAnswerResponse(2, "영화 감상을 좋아해요")
                )
            )
        )

        coEvery { matchingDataSource.loadValuePicks() } returns Result.success(
            LoadValuePicksResponse(validValuePicks)
        )
        coEvery { localMatchingDataSource.replaceValuePicks(any()) } returns Result.success(Unit)

        // when
        val result = matchingRepository.loadValuePicks()

        // then
        assertTrue(result.isSuccess)
        coVerify(exactly = 1) {
            localMatchingDataSource.replaceValuePicks(
                match {
                    it.size == validValuePicks.size && it.all { entity ->
                        validValuePicks.any { pick ->
                            pick.id == entity.valuePickQuestion.id &&
                                    pick.category == entity.valuePickQuestion.category
                        }
                    }
                }
            )
        }
    }

    @Test
    fun `갱신한 가치관Talk 데이터는 로컬 데이터베이스에 저장한다`() = runTest {
        // given
        val validValueTalks = listOf(
            ValueTalkResponse(
                id = 1,
                title = "술자리에 대한 대화",
                category = "음주",
                guide = null
            ),
            ValueTalkResponse(
                id = 2,
                title = "취미 공유하기",
                category = "취미",
                guide = null
            )
        )

        coEvery { matchingDataSource.loadValueTalks() } returns Result.success(
            LoadValueTalksResponse(validValueTalks)
        )
        coEvery { localMatchingDataSource.replaceValueTalks(any()) } returns Result.success(Unit)

        // when
        val result = matchingRepository.loadValueTalks()

        // then
        assertTrue(result.isSuccess)
        coVerify(exactly = 1) {
            localMatchingDataSource.replaceValueTalks(
                match {
                    it.size == validValueTalks.size && it.all { entity ->
                        validValueTalks.any { talk ->
                            talk.id == entity.id && talk.title == entity.title
                        }
                    }
                }
            )
        }
    }
}
