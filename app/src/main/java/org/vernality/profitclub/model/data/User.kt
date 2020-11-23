package org.vernality.profitclub.model.data

import android.os.Parcelable
import com.parse.ParseClassName
import com.parse.ParseObject
import kotlinx.android.parcel.Parcelize

@ParseClassName("User")
class User: ParseObject(){
    val id: String?
        get() = getString("objectId")

    var username: String?
        get() = getString("username")
        set(value){ if(value != null) put("username", value)}

    var login: String?
        get() = getString("login")
        set(value){ if(value != null) put("login", value)}

    var password: String?
        get() = getString("password")
        set(value){ if(value != null) put("password", value)}

    var email: String?
        get() = getString("email")
        set(value){ if(value != null) put("email", value)}

    var emailVerified: Boolean?
        get() = getBoolean("emailVerified")
        set(value){ if(value != null) put("emailVerified", value)}

    var password2: String? = null


}