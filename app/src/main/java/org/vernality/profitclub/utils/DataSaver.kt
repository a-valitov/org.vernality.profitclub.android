package org.vernality.profitclub.utils

import org.vernality.profitclub.model.data.Member
import org.vernality.profitclub.model.data.Organization
import org.vernality.profitclub.model.data.Supplier
import org.vernality.profitclub.view_model.Role

object DataSaver {

    var organization: Organization? = null

    var supplier: Supplier? = null

    var member: Member? = null

    var role: Role? = null


    fun <T>setData(data: T){
        if(data is Role) role = data
        if(data is Organization) organization = data
        println("DataSaver save data type "+ data)
    }

    inline fun <reified T>getData(remove: Boolean = false):T?{

        when{
            role is T -> {
                if(!remove) {println("DataSaver get data type "+ role); return role as T}
                else {val _role = role; role = null; return _role as T}

            }
            organization is T -> {
                if(!remove) {println("DataSaver get data type "+ organization); return organization as T}
                else {val _organization = organization; organization = null; return _organization as T}

            }
            else -> return null

        }

    }
}
