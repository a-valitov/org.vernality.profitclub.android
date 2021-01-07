package org.vernality.profitclub.model.data

import android.os.Parcelable
import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.util.*

@ParseClassName("CommercialOffer")
class CommercialOffer: ParseObject(){
    val id: String?
        get() = getString("objectId")

    var message: String?
        get() = getString("message")
        set(value){ if(value != null) put("message", value)}

    var imageFile: ParseFile?
        get() = getParseFile("imageFile")
        set(value){ if(value != null) put("imageFile", value)}

    val supplier: Supplier?
        get()
        {
            println("---get Supplier")
            return getParseObject("supplier") as Supplier
        }

    var image: ByteArray? = null
        get() = field
        set(value){ field = value }

    var listOfDocs: MutableMap<String, ByteArray> = mutableMapOf()
        get() = field
        set(value){ field = value }

    var listNamesOfDocs: MutableList<String> = mutableListOf()
        get() = field
        set(value){ field = value }

    @IgnoredOnParcel
    var contact: String? = null
        get() = field
        set(value){ if(value != null) field = value}

}