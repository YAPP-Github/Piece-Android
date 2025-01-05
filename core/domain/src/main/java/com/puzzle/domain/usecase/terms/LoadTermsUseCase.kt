package com.puzzle.domain.usecase.terms

import com.puzzle.domain.repository.TermsRepository
import javax.inject.Inject

class LoadTermsUseCase @Inject constructor(
    private val termsRepository: TermsRepository,
) {
    suspend operator fun invoke(): Result<Unit> = termsRepository.loadTerms()
}