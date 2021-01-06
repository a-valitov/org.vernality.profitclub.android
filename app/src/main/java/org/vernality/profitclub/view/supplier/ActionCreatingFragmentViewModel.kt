package org.vernality.profitclub.view.supplier

import android.app.Application
import androidx.lifecycle.LiveData
import io.reactivex.observers.DisposableCompletableObserver
import org.vernality.profitclub.model.data.Action
import org.vernality.profitclub.model.data.AppState
import org.vernality.profitclub.utils.DataSaver
import org.vernality.profitclub.utils.ui.UIUtils
import org.vernality.profitclub.utils.ui.addStreamsIO_UI
import org.vernality.profitclub.view_model.BaseViewModel
import timber.log.Timber
import java.util.*

enum class Field{Login, Gmail, Password, Password2}

enum class ResultOfCreatingAction{
    SuccessCheckFields, SuccessCreating;
    var text:String? = null

}

const val message = "некорректное заполнение поля "

class ActionCreatingFragmentViewModel(appContext: Application) : BaseViewModel<AppState>(appContext) {

    private val action = Action()

    fun getLiveDataAndStartGetResult(): LiveData<AppState> {

        if(checkFields()) getCheckResult()

        return liveDataForViewToObserve
    }

    private fun getCheckResult() {
        liveDataForViewToObserve.value =
            AppState.Success<ResultOfCreatingAction>(ResultOfCreatingAction.SuccessCheckFields)
    }

    fun getResult(){
        compositeDisposable.add(
            interactor.createAction(action)
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
                Timber.d("onComplete()")
                liveDataForViewToObserve.value =
                    AppState.Success<ResultOfCreatingAction>(ResultOfCreatingAction.SuccessCreating)
            }

        }
    }

    fun setImageFile(byteArray: ByteArray){
        action.image = byteArray
    }

    fun setMessage(message: String){
        action.message = message
    }

    fun setDescription(description: String){
        action.descriptionOf = description
    }

    fun setLink(link: String){
        action.link = link
    }

    fun setStartDate(date: Date){
        action.start = date
    }

    fun setEndDate(date: Date){
        action.end = date
    }

    fun getMessage() = action.message

    fun getDescription() = action.descriptionOf

    fun getLink() = action.link



    private fun checkFields():Boolean =
        checkMessage(action.message)
        && checkDescription(action.descriptionOf)
        && checkLink(action.link)


    private fun checkMessage(message: String?):Boolean{
        if(!message.isNullOrEmpty() && !message.isNullOrBlank()){
            return true
        } else {
            liveDataForViewToObserve.value = AppState.Error(Throwable("Необходимо ввести сообщение акции"))
            return false
        }
    }

    private fun checkDescription(description: String?):Boolean{
        if(!description.isNullOrEmpty() && !description.isNullOrBlank()){
            return true
        } else {
            liveDataForViewToObserve.value = AppState.Error(Throwable("Необходимо ввести описание акции"))
            return false
        }
    }

    private fun checkLink(link: String?):Boolean{
        if(!link.isNullOrEmpty() && !link.isNullOrBlank()){
            return true
        } else {
            liveDataForViewToObserve.value = AppState.Error(Throwable("Необходимо вставить ссылку на акцию"))
            return false
        }

    }


    fun enterInAccount(){

    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}