package com.puzzle.network.source

import com.puzzle.network.api.PieceApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataSource @Inject constructor(
    private val pieceApi: PieceApi,
) {}
