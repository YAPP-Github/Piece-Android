package com.puzzle.network.source

import com.puzzle.network.api.PieceApi
import com.puzzle.network.model.terms.LoadTermsResponse
import com.puzzle.network.model.unwrapData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TermDataSource @Inject constructor(
    private val pieceApi: PieceApi,
) {
    suspend fun loadTerms(): Result<LoadTermsResponse> = pieceApi.loadTerms().unwrapData()
}
