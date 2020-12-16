package org.vernality.profitclub.utils.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import org.koin.core.KoinComponent
import org.koin.core.get
import java.io.ByteArrayOutputStream
import java.io.InputStream

class ImagesUtils {
    companion object: KoinComponent {

        fun convertBitmapToPNGByteArray(bitmap: Bitmap):ByteArray{

            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val bytes: ByteArray = byteArrayOutputStream.toByteArray()
            return bytes
        }


        fun getBitmapFromImagesUri(uri: Uri): Bitmap{
            val context: Context = get()
            val imageStream: InputStream? =
                context.contentResolver.openInputStream(uri)
            val selectedImage: Bitmap = BitmapFactory.decodeStream(imageStream)
            return selectedImage
        }





    }
}