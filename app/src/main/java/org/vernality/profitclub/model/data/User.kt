package org.vernality.profitclub.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var id: String = "",
    var login: String = ""
) : Parcelable