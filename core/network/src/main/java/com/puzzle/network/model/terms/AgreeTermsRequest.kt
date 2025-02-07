package com.puzzle.network.model.terms

import kotlinx.serialization.Serializable

@Serializable
data class AgreeTermsRequest(
    val agreedTermsId: List<Int>,
)
