package org.vernality.profitclub.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import io.reactivex.observers.DisposableCompletableObserver
import org.vernality.profitclub.model.data.AppState
import org.vernality.profitclub.model.data.User
import org.vernality.profitclub.utils.ui.UIUtils

enum class Field{Login, Gmail, Password, Password2}

enum class Result{
    Success, Error;
    var text:String? = null

}

const val message = "некорректное зхаполнение поля "

class RegistrationFragmentViewModel(appContext: Application) : BaseViewModel<AppState>(appContext) {

    private val user = User()

    fun getLiveDataAndStartGetResult(): LiveData<AppState> {

        if(checkFields()) getResult()

        return liveDataForViewToObserve
    }

    private fun getResult(){
        compositeDisposable.add(
            interactor.registration(user)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { liveDataForViewToObserve.value = AppState.Loading(null) }
                .subscribeWith(getObserver())
        )
    }

    private fun getObserver(): DisposableCompletableObserver {
        return object : DisposableCompletableObserver() {

            override fun onError(e: Throwable) {
                liveDataForViewToObserve.value = AppState.Error(e)
            }

            override fun onComplete() {
                liveDataForViewToObserve.value = AppState.Success<Unit>(Unit)
            }

        }
    }

    fun setLogin(login: String){
        user.login = login

    }

    fun setGmail(gmail: String){
        user.email = gmail
        user.username = gmail
    }

    fun setPassword(password1: String){
        user.password = password1
    }

    fun setPassword2(password2: String){
        user.password2 = password2
        checkPassword(password2)
    }

    private fun checkPassword(password2: String?):Boolean{

        if(password2 != null) {
            if (user.password == null) {
                liveDataForViewToObserve.value =
                    AppState.Error(Throwable("Заполните предыдущее поле ввода пароля"))
                return false
            } else {
                if (!user.password?.contains(password2)!!) {
                    liveDataForViewToObserve.value =
                        AppState.Error(Throwable("Введенные пароли не совпадают"))
                    return false
                } else return true
            }
        }else{
            liveDataForViewToObserve.value =
                AppState.Error(Throwable("Заполните поле ввода пароля"))
            return false
        }
    }



    private fun checkFields():Boolean =
        checkLogin(user.login)
        && checkGmail(user.email)
        && checkPassword(user.password)
        && checkFinishPassword(user.password2)


    private fun checkLogin(login: String?):Boolean{
        if(!login.isNullOrEmpty() && !login.isNullOrBlank()){
            return true
        } else {
            liveDataForViewToObserve.value = AppState.Error(Throwable("Необходимо ввести логин"))
            return false
        }

    }

    private fun checkGmail(gmail: String?):Boolean{
        if(!gmail.isNullOrBlank()){
            if(UIUtils.checkGmail(gmail)){
                return true
            }else{
                liveDataForViewToObserve.value = AppState.Error(Throwable("Некорректный адрес почты"))
                return false
            }
        } else {
            liveDataForViewToObserve.value = AppState.Error(Throwable("Необходимо ввести почту"))
            return false
        }
    }

    private fun checkFinishPassword(password: String?):Boolean{
        val PASSWORD_REGEX = """^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#${'$'}%!\-_?&])(?=\S+${'$'}).{6,40}""".toRegex()
        if(!password.isNullOrBlank() && !password.isNullOrEmpty()){
            if(PASSWORD_REGEX.matches(password)){
                return true
            }else{
                liveDataForViewToObserve.value = AppState.Error(Throwable("Некорректный пароль." +
                        " Длина пароля должна быть от 6 до 40 символов, пароль должен включать в себя цифры, заглавные буквы и специальные символы"))
                return false
            }
        } else {
            liveDataForViewToObserve.value = AppState.Error(Throwable("Необходимо ввести пароль"))
            return false
        }

    }


    fun enterInAccount(){

    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}