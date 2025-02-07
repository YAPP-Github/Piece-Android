package com.puzzle.data.image

import java.io.InputStream

interface ImageResizer {
    fun resizeImage(
        uri: String,
        reqWidth: Int = 1024,
        reqHeight: Int = 1024,
    ): InputStream
}
