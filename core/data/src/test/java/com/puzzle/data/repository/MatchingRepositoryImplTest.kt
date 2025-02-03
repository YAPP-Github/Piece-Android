package com.puzzle.data.repository

import com.puzzle.data.fake.source.matching.FakeLocalMatchingDataSource
import com.puzzle.data.fake.source.matching.FakeMatchingDataSource
import com.puzzle.network.model.matching.ValueTalkResponse
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MatchingRepositoryImplTest {
    private lateinit var matchingDataSource: FakeMatchingDataSource
    private lateinit var localMatchingDataSource: FakeLocalMatchingDataSource
    private lateinit var matchingRepository: MatchingRepositoryImpl

    @BeforeEach
    fun setUp() {
        matchingDataSource = FakeMatchingDataSource()
        localMatchingDataSource = FakeLocalMatchingDataSource()
        matchingRepository = MatchingRepositoryImpl(matchingDataSource, localMatchingDataSource)
    }

    @Test
    fun `가치관Talk을 새로 갱신할 경우 id값이 올바르게 내려오지 않은 가치관Talk은 무시한다`() = runTest {
        // given
        matchingDataSource.setValueTalks(
            listOf(
                ValueTalkResponse(
                    id = null,
                    category = null,
                    title = null,
                    guide = null
                ),
                ValueTalkResponse(
                    id = 1,
                    category = "음주",
                    title = "술자리에 대한 대화",
                    guide = null
                )
            )
        )

        // when
        matchingRepository.loadValueTalks()

        // then
        val storedTalks = localMatchingDataSource.retrieveValueTalks()
        assertTrue(storedTalks.all { it.id != null })
        assertTrue(storedTalks.size == 1)
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
        matchingDataSource.setValueTalks(validValueTalks)

        // when
        matchingRepository.loadValueTalks()

        // then
        val storedTalks = localMatchingDataSource.retrieveValueTalks()
        assertTrue(storedTalks.size == validValueTalks.size)
        assertTrue(
            storedTalks.all { entity ->
                validValueTalks.any { talk -> talk.id == entity.id && talk.title == entity.title }
            }
        )
    }
}
