package org.vernality.profitclub.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Organization(
    var id: String = "",
    var state: Int = 0,
    var name: String = "",
    var INN: String = "",
    var FCS: String = "",
    var phone: String = "",
    var role: String = ""
) : Parcelable