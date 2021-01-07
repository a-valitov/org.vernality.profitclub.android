package org.vernality.profitclub.utils.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import org.koin.core.KoinComponent
import org.koin.core.get
import org.vernality.profitclub.view.supplier.OfferCreatingFragment
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

        fun getBitmapFromByteArray(byte: ByteArray): Bitmap{
            val context: Context = get()
            val imageStream: InputStream? = byte.inputStream()
            val selectedImage: Bitmap = BitmapFactory.decodeStream(imageStream)
            return selectedImage
        }

        fun getByteArrayFromImagesUri(uri: Uri): ByteArray?{
            val context: Context = get()
            val imageStream: InputStream? =
                context.contentResolver.openInputStream(uri)
            return imageStream?.readBytes()
        }


    }
}