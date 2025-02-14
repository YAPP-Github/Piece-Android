package com.puzzle.data.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject

class ImageResizerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ImageResizer {
    override fun resizeImage(
        uri: String,
        reqWidth: Int,
        reqHeight: Int,
    ): InputStream {
        val originImageStream = context.contentResolver.openInputStream(uri.toUri())

        originImageStream?.use { inputStream ->
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeStream(inputStream, null, options)

            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
            options.inJustDecodeBounds = false

            context.contentResolver.openInputStream(uri.toUri())?.use { newInputStream ->
                val resizedBitmap = BitmapFactory.decodeStream(newInputStream, null, options)

                val byteArrayOutputStream = ByteArrayOutputStream()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    resizedBitmap?.compress(
                        Bitmap.CompressFormat.WEBP_LOSSY, 100, byteArrayOutputStream
                    )
                } else {
                    resizedBitmap?.compress(
                        Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream
                    )
                }
                val byteArray = byteArrayOutputStream.toByteArray()

                return ByteArrayInputStream(byteArray)
            }
        }

        throw IllegalArgumentException("Unable to open InputStream for the given URI")
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int,
    ): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }
}
