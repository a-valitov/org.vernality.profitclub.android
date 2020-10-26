package org.vernality.profitclub.view_model

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.get
import org.koin.core.inject
import org.vernality.profitclub.di.application
import java.util.*

enum class Field{Login, Gmail, Password1, Password2}

class RegistrationViewModel(appContext: Application) : BaseViewModel(appContext) {

    private val context = appContext

    private val registration: MutableMap<Field, String?> =
        mutableMapOf(Field.Login to null, Field.Gmail to null, Field.Password1 to null, Field.Password2 to null)


    fun setLogin(login: String){
        registration[Field.Login] = login
    }

    fun setGmail(gmail: String){
        registration[Field.Gmail] = gmail
    }

    fun setPassword(password1: String){
        registration[Field.Password1]=password1
    }

    fun checkPassword(password2: String){

        if(registration.get(Field.Password1) == null) {
            Toast.makeText(context, "fill in the previous password field", Toast.LENGTH_LONG).show()
        } else {
            if(!registration.get(Field.Password1)?.contains(password2)!!) {
                Toast.makeText(context, "the passwords you entered don't match", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun registration(){

        checkFields()
    }

    private fun checkFields() {
        TODO("Not yet implemented")
    }

    fun enterInAccount(){

    }

    fun clear(){
        for (field in registration){
           field.setValue(null)
        }
    }

}