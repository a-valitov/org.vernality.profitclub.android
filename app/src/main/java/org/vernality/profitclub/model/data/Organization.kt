package org.vernality.profitclub.model.data

import com.parse.ParseClassName
import com.parse.ParseObject


@ParseClassName("Organization")
class Organization: ParseObject() {

    enum class OrganizationStatus{
        onReview, approved, rejected, excluded
    }

    val id: String?
        get() = getString("objectId")

    var name: String?
        get() = getString("name")
        set(value){ if(value != null) put("name", value)}

    var contactName: String?
        get() = getString("contact")
        set(value){ if(value != null) put("contact", value)}

    var phone: String?
        get() = getString("phone")
        set(value){ if(value != null) put("phone", value)}

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