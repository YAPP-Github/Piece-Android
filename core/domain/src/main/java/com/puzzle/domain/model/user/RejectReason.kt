package com.puzzle.domain.model.user

data class RejectReason(
    val profileStatus: ProfileStatus,
    val reasonImage: Boolean,
    val reasonValues: Boolean,
)
