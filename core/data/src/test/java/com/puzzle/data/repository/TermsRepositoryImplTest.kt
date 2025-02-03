package com.puzzle.data.repository

import com.puzzle.data.datasource.term.FakeLocalTermDataSource
import com.puzzle.data.datasource.term.FakeTermDataSource
import com.puzzle.network.model.UNKNOWN_INT
import com.puzzle.network.model.terms.TermResponse
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TermsRepositoryImplTest {
    private lateinit var termDataSource: FakeTermDataSource
    private lateinit var localTermDataSource: FakeLocalTermDataSource
    private lateinit var termsRepository: TermsRepositoryImpl

    @BeforeEach
    fun setUp() {
        termDataSource = FakeTermDataSource()
        localTermDataSource = FakeLocalTermDataSource()
        termsRepository = TermsRepositoryImpl(termDataSource, localTermDataSource)
    }

    @Test
    fun `약관을 새로 갱신할 경우 id값이 올바르게 내려오지 않은 약관은 무시한다`() = runTest {
        //given
        val invalidTerms = listOf(
            TermResponse(
                termId = null,
                title = null,
                content = null,
                required = null,
                startDate = null,
            ),
            TermResponse(
                termId = 2,
                title = "Valid2",
                content = "Content2",
                required = false,
                startDate = "2024-06-01T00:00:00"
            )
        )
        termDataSource.setTerms(invalidTerms)

        // when
        termsRepository.loadTerms()

        // then
        val storedTerms = localTermDataSource.retrieveTerms()
        assertTrue(storedTerms.all { it.id != UNKNOWN_INT })
        assertTrue(storedTerms.size == 1)  // 유효한 약관 하나만 저장되어야 함
    }

    @Test
    fun `갱신한 데이터는 로컬 데이터베이스에 저장한다`() = runTest {
        // given
        val validTerms = listOf(
            TermResponse(
                termId = 1,
                title = "Valid1",
                content = "Content1",
                required = true,
                startDate = "2024-06-01T00:00:00"
            ),
            TermResponse(
                termId = 2,
                title = "Valid2",
                content = "Content2",
                required = false,
                startDate = "2024-06-01T00:00:00"
            )
        )
        termDataSource.setTerms(validTerms)

        // when
        termsRepository.loadTerms()

        // then
        val storedTerms = localTermDataSource.retrieveTerms()
        assertTrue(storedTerms.size == validTerms.size)
        assertTrue(
            storedTerms.all { entity ->
                validTerms.any { term ->
                    term.termId == entity.id && term.title == entity.title
                }
            }
        )
    }
}
