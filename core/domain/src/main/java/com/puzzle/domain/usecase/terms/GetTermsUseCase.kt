package com.puzzle.domain.usecase.terms

import com.puzzle.domain.model.terms.Term
import com.puzzle.domain.repository.TermsRepository
import javax.inject.Inject

class GetTermsUseCase @Inject constructor(
    private val termsRepository: TermsRepository,
) {
    suspend operator fun invoke(): Result<List<Term>> = termsRepository.getTerms()
}
