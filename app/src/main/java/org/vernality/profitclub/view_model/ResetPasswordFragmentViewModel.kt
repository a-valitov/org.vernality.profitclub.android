package org.vernality.profitclub.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import io.reactivex.observers.DisposableCompletableObserver
import org.vernality.profitclub.model.data.AppState
import org.vernality.profitclub.model.data.User
import org.vernality.profitclub.utils.ui.UIUtils
import org.vernality.profitclub.utils.ui.addStreamsIO_UI


class ResetPasswordFragmentViewModel(appContext: Application) : BaseViewModel<AppState>(appContext) {

    private val user = User()

    private var gmail: String? = null

    fun getLiveDataAndStartGetResult(): LiveData<AppState> {

        if(checkFields()) getResult()

        return liveDataForViewToObserve
    }

    private fun getResult(){
        compositeDisposable.add(
            interactor.resetPassword(gmail!!)
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


    fun setGmail(_gmail: String){
        gmail = _gmail
    }




    private fun checkFields():Boolean =
        checkGmail(gmail)


    private fun checkGmail(login: String?):Boolean{
        if(!login.isNullOrEmpty() && !login.isNullOrBlank()){
            return true
        } else {
            liveDataForViewToObserve.value = AppState.Error(Throwable("Необходимо заполнить поле login"))
            return false
        }

    }


    fun enterInAccount(){

    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}