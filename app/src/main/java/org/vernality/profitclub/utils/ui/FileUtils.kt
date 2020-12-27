package org.vernality.profitclub.utils.ui

import android.content.Context
import android.net.Uri
import org.koin.core.KoinComponent
import org.koin.core.get
import java.io.InputStream

class FileUtils {

    companion object: KoinComponent {



        fun getByteFromDocUri(uri: Uri): ByteArray? {
            val context: Context = get()
            val docStream: InputStream? =
                context.contentResolver.openInputStream(uri)
            return docStream?.readBytes()
        }



    }

}