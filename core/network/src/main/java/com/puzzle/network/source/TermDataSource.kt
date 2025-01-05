package com.puzzle.network.source

import com.puzzle.network.api.PieceApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TermDataSource @Inject constructor(
    private val pieceApi: PieceApi,
) {
    suspend fun loadTerms() = pieceApi.loadTerms()
}