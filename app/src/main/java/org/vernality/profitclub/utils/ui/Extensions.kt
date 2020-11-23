package org.vernality.profitclub.utils.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView

fun ImageView.setImageToImageView(array: ByteArray): View {
    val bmp = BitmapFactory.decodeByteArray(array, 0, array.size)

    setImageBitmap(
        Bitmap.createBitmap(
            bmp
        )
    )

    return this
}