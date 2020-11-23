package org.vernality.profitclub.utils

import android.annotation.SuppressLint
import java.util.*

public class Utils {

    companion object{
        @SuppressLint("SimpleDateFormat")
        fun getMyDateFormat()  = java.text.SimpleDateFormat("dd.MM.yyyy")

        fun getDataOfMyFormat(date: Date) = getMyDateFormat().format(date)


    }


}