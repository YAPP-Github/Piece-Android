package com.puzzle.data.fake.source.image

import com.puzzle.data.image.ImageResizer
import java.io.ByteArrayInputStream
import java.io.InputStream

class FakeImageResizer : ImageResizer {
    override fun resizeImage(uri: String, reqWidth: Int, reqHeight: Int): InputStream {
        return ByteArrayInputStream(ByteArray(0))
    }
}
