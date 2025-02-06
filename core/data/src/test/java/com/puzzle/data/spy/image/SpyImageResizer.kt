package com.puzzle.data.spy.image

import com.puzzle.data.image.ImageResizer
import java.io.ByteArrayInputStream
import java.io.InputStream

class SpyImageResizer : ImageResizer {
    var resizeImageCallCount = 0
        private set

    override fun resizeImage(uri: String, reqWidth: Int, reqHeight: Int): InputStream {
        resizeImageCallCount++
        return ByteArrayInputStream(ByteArray(0))
    }
}
