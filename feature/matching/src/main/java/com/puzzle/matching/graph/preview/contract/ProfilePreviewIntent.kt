package com.puzzle.matching.graph.preview.contract

sealed class ProfilePreviewIntent {
    data object OnCloseClick : ProfilePreviewIntent()
}