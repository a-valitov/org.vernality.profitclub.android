package org.vernality.profitclub.model.data

import android.os.Parcelable
import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import kotlinx.android.parcel.Parcelize
import java.util.*

@ParseClassName("Action")
class Action: ParseObject(){
    val id: String?
        get() = getString("objectId")

    var message: String?
        get() = getString("message")
        set(value){ if(value != null) put("message", value)}

    var startDate: Date?
        get() = getDate("startDate")
        set(value){ if(value != null) put("startDate", value)}

    var endDate: Date?
        get() = getDate("endDate")
        set(value){ if(value != null) put("endDate", value)}

    var descriptionOf: String?
        get() = getString("descriptionOf")
        set(value){ if(value != null) put("descriptionOf", value)}

    var link: String?
        get() = getString("link")
        set(value){ if(value != null) put("link", value)}

    var statusString: String?
        get() = getString("statusString")
        set(value){ if(value != null) put("statusString", value)}

    var imageFile: ParseFile?
    get() = getParseFile("imageFile")
    set(value){ if(value != null) put("imageFile", value)}

    var supplier: ParseObject?
        get() = getParseObject("supplier")
        set(value){ if(value != null) put("supplier", value)}

}