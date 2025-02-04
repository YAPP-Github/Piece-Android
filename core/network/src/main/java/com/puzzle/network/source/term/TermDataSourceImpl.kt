package com.puzzle.network.source.term

import com.puzzle.network.api.PieceApi
import com.puzzle.network.model.terms.AgreeTermsRequest
import com.puzzle.network.model.terms.LoadTermsResponse
import com.puzzle.network.model.unwrapData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TermDataSourceImpl @Inject constructor(
    private val pieceApi: PieceApi,
) : TermDataSource {
    override suspend fun loadTerms(): Result<LoadTermsResponse> = pieceApi.loadTerms().unwrapData()

    override suspend fun agreeTerms(agreeTermsId: List<Int>): Result<Unit> =
        pieceApi.agreeTerms(AgreeTermsRequest(agreeTermsId)).unwrapData()
}
