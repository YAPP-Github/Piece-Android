package com.puzzle.data.image

import java.io.InputStream

interface ImageResizer {
    fun resizeImage(
        uri: String,
        reqWidth: Int = 512,
        reqHeight: Int = 512,
    ): InputStream
}
