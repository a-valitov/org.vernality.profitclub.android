package org.vernality.profitclub.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Role(
    var id: String = "",
    var state: Int = 0,
    var name: String = "",
    var INN: String = "",
    var contactName: String = "",
    var phone: String = "",
    var role: String = ""
) : Parcelable