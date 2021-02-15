package org.vernality.profitclub.view.supplier

import android.annotation.SuppressLint
import android.app.Application
import android.net.ParseException
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import io.reactivex.observers.DisposableCompletableObserver
import org.vernality.profitclub.model.data.AppState
import org.vernality.profitclub.model.data.CommercialOffer
import org.vernality.profitclub.utils.ui.FileUtils
import org.vernality.profitclub.utils.ui.ImagesUtils
import org.vernality.profitclub.utils.ui.addStreamsIO_UI
import org.vernality.profitclub.view.organization.adapter.OffersDocListRVAdapter
import org.vernality.profitclub.view_model.BaseViewModel
import timber.log.Timber
import java.io.File
import org.vernality.profitclub.view.organization.adapter.*


class OfferCreatingFragmentViewModel(appContext: Application) : BaseViewModel<AppState>(appContext) {

    private val offer = CommercialOffer()

    private var listStateDocs = mutableListOf<LoadingStateDoc>()

    var offersMessage: String? = null
    val liveDataForImage: MutableLiveData<ByteArray> = MutableLiveData()
    val liveDataForDocs: MutableLiveData<List<LoadingStateDoc>> = MutableLiveData()

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
            interactor.createOffer(offer)
                .addStreamsIO_UI()
                .doOnSubscribe { liveDataForViewToObserve.value = AppState.Loading(null) }
                .subscribeWith(getObserver())
        )
    }


    @SuppressLint("CheckResult")
    fun uploadFileToOffer(uri: Uri){

        val fileName = File(uri.path).name

        var loadingStateDoc = LoadingStateDoc(fileName, UploadState.Loading)

        listStateDocs.add(loadingStateDoc)


        Single.create<ByteArray> {
            val byteArray = FileUtils.getByteFromDocUri(uri)
            byteArray?.let { byte -> it.onSuccess(byte) }?:(it.onError(throw Throwable("ошибка чтения файла")))

        }.doOnSuccess { liveDataForDocs.value = listStateDocs }
            .subscribe({
                offer.listOfDocs.put(fileName, it)
                offer.listNamesOfDocs.add(fileName)
                loadingStateDoc.fileState = UploadState.Uploaded
                liveDataForDocs.value = listStateDocs
            }, {
                liveDataForViewToObserve.value = AppState.Error(it)
                listStateDocs.remove(loadingStateDoc)
                liveDataForDocs.value = listStateDocs
            })

    }

    @SuppressLint("CheckResult")
    fun uploadImageToOffer(uri: Uri): LiveData<ByteArray>{

        ImagesUtils.getBitmapFromImagesUri(uri)

        Single.create<ByteArray> {
            val byteArray = ImagesUtils.getByteArrayFromImagesUri(uri)
            byteArray?.let { byte -> it.onSuccess(byte) }?:(it.onError(throw Throwable("ошибка чтения файла")))

        }.subscribe({
            offer.image = it
            liveDataForImage.value = it
        }, {
            liveDataForViewToObserve.value = AppState.Error(it)
        })

        return liveDataForImage

    }



    fun setMessage(message: String){
        offersMessage = message
        offer.message = message
    }

    private fun getObserver(): DisposableCompletableObserver {
        return object : DisposableCompletableObserver() {

            override fun onError(e: Throwable) {
                println("---- error.code = ${e.message}")

                liveDataForViewToObserve.value = AppState.Error(e)
            }

            override fun onComplete() {
                Timber.d("onComplete()")
                liveDataForViewToObserve.value =
                    AppState.Success<ResultOfCreatingAction>(ResultOfCreatingAction.SuccessCreating)
            }

        }
    }


    fun getMessage() = offersMessage


    private fun checkFields():Boolean =
        checkMessage(message)


    fun getLiveForDocs(): LiveData<List<LoadingStateDoc>> = liveDataForDocs

    fun getLiveForImage(): LiveData<ByteArray> = liveDataForImage


    private fun checkMessage(message: String?):Boolean{
        if(!message.isNullOrEmpty() && !message.isNullOrBlank()){
            return true
        } else {
            liveDataForViewToObserve.value = AppState.Error(Throwable("Необходимо ввести сообщение акции"))
            return false
        }
    }


    override fun onCleared() {
        compositeDisposable.clear()
    }

}