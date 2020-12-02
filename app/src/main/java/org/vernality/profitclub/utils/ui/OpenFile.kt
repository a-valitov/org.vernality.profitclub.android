package org.vernality.profitclub.utils.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.FileProvider
import timber.log.Timber
import java.io.File
import java.util.*


fun openFileInExternalApp(file: File?, format: String, context: Context){
    Timber.d("формат файла = "+format)
    if(file != null)
    {
        openPdfFileInExternalApp(file, context, getTypeOfMIME(format))

    } else Toast.makeText(context,"файл не найден", Toast.LENGTH_SHORT).show()

}

private fun openPdfFileInExternalApp(file: File, context: Context, dataAndType: String){
    val filesUri = FileProvider.getUriForFile(
        context,
        context.getPackageName() + ".provider",
        file
    )

    val intent = Intent(Intent.ACTION_VIEW)
    intent.flags =
        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_CLEAR_TOP
    intent.setDataAndType(filesUri, dataAndType)
    verifyIntent(intent, context)

}

private fun verifyIntent(intent: Intent, context: Context){
    val chooser = Intent.createChooser(intent, "Выберите приложение для открытия файла");
    val packageManager = context.packageManager
    val activities =
        packageManager.queryIntentActivities(intent, 0)
    val isIntentSafe = activities.size > 0

    if(isIntentSafe){
        Timber.d("есть активити способное открыть интент")
        try {
            context.startActivity(chooser)
        } catch (e: ActivityNotFoundException) {
            Timber.d("не найдено активити, способное открыть интент")
            Toast.makeText(context, "на устройстве не найдено приложение, способное открыть данный файл", Toast.LENGTH_LONG).show()
        }

    }
    else {
        Timber.d("нет активити способного открыть интент")
        Toast.makeText(context, "на устройстве не найдено приложение, способное открыть данный файл", Toast.LENGTH_LONG).show()
    }
}

private fun getTypeOfMIME(_extension: String): String{
    val extension = _extension.toLowerCase(Locale.ROOT)

    return when(extension){
        "pdf" -> "application/pdf"
        "doc","word", "w6w", "dot" -> "application/msword"
        "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        "dotx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.template"
        "docm" -> "application/vnd.ms-word.document.macroEnabled.12"
        "dotm" -> "application/vnd.ms-word.template.macroEnabled.12"
        "xls", "xla", "xlt", "xlw" -> "application/msexcel"
        "ppt", "pot", "pps", "ppa" -> "tapplication/mspowerpoint"
        "djvu" -> "image/vnd.djvu"
        "text" -> "text/plain"
        else -> "unknown file format"
    }

}

