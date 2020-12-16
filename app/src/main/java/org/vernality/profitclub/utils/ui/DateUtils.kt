package org.vernality.profitclub.utils.ui

import android.app.DatePickerDialog
import android.content.Context
import com.google.android.material.textview.MaterialTextView
import org.vernality.profitclub.R
import java.util.*

fun selectDate(context: Context?, theme: Int, dateEditText: MaterialTextView, calendar: Calendar? = null) {
    val calender = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(context!!,
        R.style.DatePickerDialogTheme,
        DatePickerDialog.OnDateSetListener { view, year, month, day ->
            val dateCalender = Calendar.getInstance()
            dateCalender[Calendar.YEAR] = year
            dateCalender[Calendar.MONTH] = month
            dateCalender[Calendar.DAY_OF_MONTH] = day
            val dateFormatted =
                dateFormatter(
                    year,
                    month,
                    day
                )
            dateEditText.setText(dateFormatted)
            calendar?.set(year, month, day)
        }, calender[Calendar.YEAR], calender[Calendar.MONTH], calender[Calendar.DAY_OF_MONTH])
    datePickerDialog.show()
}

fun dateFormatter(year: Int, month: Int, day: Int): StringBuilder {
    var monthString: String
    var dayString: String
    monthString = (month + 1).toString()
    dayString = day.toString()
    if (month < 9) monthString = "0$monthString"
    if (day < 10) dayString = "0$day"
    val dateFormatted: StringBuilder
    dateFormatted = StringBuilder().append(dayString).append(".").append(monthString).
    append(".").append(year)
    return dateFormatted
}
