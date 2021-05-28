package org.vernality.profitclub.model.data

import android.os.Parcelable
import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import kotlinx.android.parcel.Parcelize
import java.util.*

@ParseClassName("Member")
class Member: ParseObject(), BusinessRole{
    val id: String?
        get() = getString("objectId")

    var name: String?
        get() = getString("name")
        set(value){ if(value != null) put("name", value)}

    var firstName: String?
        get() = getString("firstName")
        set(value){ if(value != null) put("firstName", value)}

    var lastName: String?
        get() = getString("lastName")
        set(value){ if(value != null) put("lastName", value)}

    var statusString: String?
        get() = getString("statusString")
        set(value){ if(value != null) put("statusString", value)}

    var imageFile: ParseFile?
        get() = getParseFile("imageFile")
        set(value){ if(value != null) put("imageFile", value)}


    var image: ByteArray? = null
        get() = field
        set(value){ field = value }

    var listOrganizations = mutableListOf<Organization>()


}