package com.puzzle.data.repository

import com.puzzle.database.source.term.LocalTermDataSource
import com.puzzle.network.model.UNKNOWN_INT
import com.puzzle.network.model.terms.LoadTermsResponse
import com.puzzle.network.model.terms.TermResponse
import com.puzzle.network.source.TermDataSource
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TermsRepositoryImplTest {

    private lateinit var termDataSource: TermDataSource
    private lateinit var localTermDataSource: LocalTermDataSource
    private lateinit var termsRepository: TermsRepositoryImpl

    @BeforeEach
    fun setUp() {
        termDataSource = mockk()
        localTermDataSource = mockk()
        termsRepository = TermsRepositoryImpl(termDataSource, localTermDataSource)
    }

    @Test
    fun `약관을 새로 갱신할 경우 id값이 올바르게 내려오지 않은 약관은 무시한다`() = runTest {
        // given
        val invalidTerm = TermResponse(
            termId = UNKNOWN_INT,
            title = "Invalid",
            content = "Invalid Content",
            required = false,
            startDate = "2024-06-01T00:00:00",
        )
        val validTerm = TermResponse(
            termId = 1,
            title = "Valid",
            content = "Valid Content",
            required = true,
            startDate = "2024-06-01T00:00:00",
        )

        coEvery { termDataSource.loadTerms() } returns
                Result.success(LoadTermsResponse(listOf(invalidTerm, validTerm)))
        coEvery { localTermDataSource.replaceTerms(any()) } just Runs

        // when
        val result = termsRepository.loadTerms()

        // then
        assertTrue(result.isSuccess)
        coVerify(exactly = 1) {
            localTermDataSource.replaceTerms(
                match {
                    it.size == 1 && it.first().id == validTerm.termId
                }
            )
        }
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

        coEvery { termDataSource.loadTerms() } returns Result.success(LoadTermsResponse(validTerms))
        coEvery { localTermDataSource.replaceTerms(any()) } just Runs

        // when
        termsRepository.loadTerms()

        // then
        coVerify(exactly = 1) {
            localTermDataSource.replaceTerms(
                match {
                    it.size == validTerms.size && it.all { entity ->
                        validTerms.any { term ->
                            term.termId == entity.id && term.title == entity.title
                        }
                    }
                }
            )
        }
    }
}
