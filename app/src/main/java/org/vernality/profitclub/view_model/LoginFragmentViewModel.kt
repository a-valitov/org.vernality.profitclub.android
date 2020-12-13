package org.vernality.profitclub.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import io.reactivex.observers.DisposableCompletableObserver
import org.vernality.profitclub.model.data.AppState
import org.vernality.profitclub.model.data.User
import org.vernality.profitclub.utils.ui.UIUtils
import org.vernality.profitclub.utils.ui.addStreamsIO_UI


class LoginFragmentViewModel(appContext: Application) : BaseViewModel<AppState>(appContext) {

    private val user = User()

    fun getLiveDataAndStartGetResult(): LiveData<AppState> {

        if(true) getResult()

        return liveDataForViewToObserve
    }

    private fun getResult(){
        compositeDisposable.add(
            interactor.signIn(user)
                .addStreamsIO_UI()
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

    fun setLogin(_login: String){
        user.login = _login
    }

    fun setPassword(password: String){
        user.password = password
    }




    private fun checkFields():Boolean =
        checkLogin(user.login)
        && checkFinishPassword(user.password)


    private fun checkLogin(login: String?):Boolean{
        if(!login.isNullOrEmpty() && !login.isNullOrBlank()){
            return true
        } else {
            liveDataForViewToObserve.value = AppState.Error(Throwable("Необходимо заполнить поле login"))
            return false
        }

    }


    private fun checkFinishPassword(password: String?):Boolean{
        val PASSWORD_REGEX = """^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#${'$'}%!\-_?&])(?=\S+${'$'}).{6,40}""".toRegex()
        if(!password.isNullOrBlank() && !password.isNullOrEmpty() && PASSWORD_REGEX.matches(password)){
            return true
        } else {
            liveDataForViewToObserve.value = AppState.Error(Throwable("Некорректный пароль"))
            return false
        }
    }


    fun enterInAccount(){

    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}