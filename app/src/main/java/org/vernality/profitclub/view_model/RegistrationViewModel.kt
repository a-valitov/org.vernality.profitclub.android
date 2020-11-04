package org.vernality.profitclub.view_model

import android.app.Application
import androidx.lifecycle.MutableLiveData
import org.koin.core.inject
import org.vernality.profitclub.model.repository.RepositoryImplementation
import org.vernality.profitclub.utils.ui.RegistrationStatus
import org.vernality.profitclub.utils.ui.UIUtils

enum class Field{Login, Gmail, Password, Password2}

enum class Result{
    Success, Error;
    var text:String? = null

}

const val message = "некорректное зхаполнение поля "

class RegistrationViewModel(appContext: Application) : BaseViewModel(appContext) {

    private val context = appContext

    private val repository: RepositoryImplementation by inject()

    private val registrationFields: MutableMap<Field, String?> =
        mutableMapOf(Field.Login to null, Field.Gmail to null, Field.Password to null, Field.Password2 to null)


    val resultLiveData: MutableLiveData<Result> by lazy {
        MutableLiveData<Result>()
    }

    val messageLiveData: MutableLiveData<String?> by lazy {
        MutableLiveData<String?>()
    }


    fun setLogin(login: String){
        registrationFields[Field.Login] = login
    }

    fun setGmail(gmail: String){
        registrationFields[Field.Gmail] = gmail
    }

    fun setPassword(password1: String){
        registrationFields[Field.Password]=password1
    }

    fun setPassword2(password2: String){
        registrationFields[Field.Password2]=password2
        checkPassword(password2)
    }

    fun checkPassword(password2: String):Boolean{

        if(registrationFields.get(Field.Password) == null) {
            messageLiveData.value = "fill in the previous password field"
            return false
        } else {
            if(!registrationFields.get(Field.Password)?.contains(password2)!!) {
                messageLiveData.value = "the passwords you entered don't match"
                return false
            } else return true
        }
    }

    fun registration(){

        if (checkFields() == Result.Success)

            repository.registration(
                registrationFields[Field.Login]!!,
                registrationFields[Field.Password]!!,
                registrationFields[Field.Gmail]!!
            ).subscribe(
                {
                    UIUtils.setRegistrationStatus(RegistrationStatus.Registered)
                    resultLiveData.value = Result.Success
                    println("-----registration is Succes-----")
                },
                {e ->
                    println(e)
                    resultLiveData.value = Result.Error
                    messageLiveData.value = e.toString()})

        else {

        }

    }


    fun deleteShowedMessage(){
        messageLiveData.value = null
    }

    private fun checkFields():Result {

        if(
            checkLogin(registrationFields[Field.Login])
            && checkGmail(registrationFields[Field.Gmail])
            && checkPassword(registrationFields[Field.Password2]!!)
            && checkFinishPassword(registrationFields[Field.Password])
          ) {
            println("---check is Succes---")
            return Result.Success}
        else return Result.Error
    }

    private fun checkLogin(login: String?):Boolean{
        if(!login.isNullOrEmpty() && !login.isNullOrBlank()){
            return true
        } else {
            messageLiveData.value = message + Field.Login.name
            return false
        }

    }

    private fun checkGmail(gmail: String?):Boolean{
        if(!gmail.isNullOrBlank() && !gmail.isNullOrEmpty() && UIUtils.checkGmail(gmail)){
            return true
        } else {
            messageLiveData.value = message + Field.Gmail.name
            return false
        }
    }

    private fun checkFinishPassword(password: String?):Boolean{
        val PASSWORD_REGEX = """^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#${'$'}%!\-_?&])(?=\S+${'$'}).{6,40}""".toRegex()
        if(!password.isNullOrBlank() && !password.isNullOrEmpty() && PASSWORD_REGEX.matches(password)){
            return true
        } else {
            messageLiveData.value = message + Field.Password.name
            return false
        }
    }


    fun enterInAccount(){

    }

    fun clear(){
        for (field in registrationFields){
           field.setValue(null)
        }
    }

}