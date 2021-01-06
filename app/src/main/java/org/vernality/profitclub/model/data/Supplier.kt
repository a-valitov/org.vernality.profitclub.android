package org.vernality.profitclub.model.data

import android.os.Parcelable
import com.parse.ParseClassName
import com.parse.ParseObject
import kotlinx.android.parcel.Parcelize

@ParseClassName("Supplier")
class Supplier: ParseObject(), BusinessRole {

    val id: String?
        get() = getString("objectId")

    var name: String?
        get() = getString("name")
        set(value){ if(value != null) put("name", value)}

    var contactName: String?
        get() = getString("contact")
        set(value){ if(value != null) put("contact", value)}

    var phone: String?
        get() = getString("contact")
        set(value){ if(value != null) put("contact", value)}

    var inn: String?
        get() = getString("inn")
        set(value){ if(value != null) put("inn", value)}

    var statusString: String?
        get() = getString("statusString")
        set(value){ if(value != null) put("statusString", value)}


    fun setFields(baseCompany: BaseCompany){
        name = baseCompany.name
        contactName = baseCompany.contactName
        phone = baseCompany.phone
        inn = baseCompany.inn
        statusString = baseCompany.statusString
    }
}