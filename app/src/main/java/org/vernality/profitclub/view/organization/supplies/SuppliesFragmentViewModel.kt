package org.vernality.profitclub.view.organization.supplies

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import org.vernality.profitclub.model.data.Action
import org.vernality.profitclub.model.data.AppState
import org.vernality.profitclub.model.data.CommercialOffer
import org.vernality.profitclub.utils.ui.addStreamsIO_UI
import org.vernality.profitclub.view_model.BaseViewModel
import timber.log.Timber

class SuppliesFragmentViewModel(appContext: Application) : BaseViewModel<AppState>(appContext) {

    private val liveDataForOfferResult: MutableLiveData<AppState> = MutableLiveData()

    fun getLiveDataAndStartGetResult(): LiveData<AppState> {

        getResult()

        return liveDataForViewToObserve
    }


    private fun getResult(){
        println("getResult() into SuppliesFragmentViewModel")
        compositeDisposable.add(
            interactor.getCommercialOffer()
                .addStreamsIO_UI()
                .doOnSubscribe { liveDataForViewToObserve.value = AppState.Loading(null) }
                .subscribeWith(getObserver())
        )
    }

    private fun getObserver(): DisposableSingleObserver<AppState> {
        return object : DisposableSingleObserver<AppState>() {

            override fun onStart() {
                super.onStart()
                println("onStart into SuppliesFragmentViewModel")
            }

            override fun onSuccess(state: AppState) {
                liveDataForViewToObserve.value = state
                println("onSuccess into SuppliesFragmentViewModel")
            }

            override fun onError(e: Throwable) {
                println("onError into SuppliesFragmentViewModel")
                liveDataForViewToObserve.value = AppState.Error(e)
            }

        }
    }

//   liveData для результата одобрения/ неодобрения КП

    fun getLiveDataForOffer(btn: Int, offer: CommercialOffer): LiveData<AppState>{
          when(btn)
          {
              0 -> getResultForAcceptBtn(offer)
              1 -> getResultForRejectBtn(offer)
          }

        return liveDataForOfferResult
    }

    private fun getResultForRejectBtn(offer: CommercialOffer) {
        Timber.d("getResultForRejectBtn(offer: CommercialOffer)")
        compositeDisposable.add(
            interactor.getResultForRejectOffer(offer)
                .addStreamsIO_UI()
                .doOnSubscribe { liveDataForOfferResult.value = AppState.Loading(null) }
                .subscribeWith(getCompletableObserver())
        )
    }

    private fun getResultForAcceptBtn(offer: CommercialOffer) {
        Timber.d("getResultForAcceptBtn(offer: CommercialOffer)")
        compositeDisposable.add(
            interactor.getResultForApproveOffer(offer)
                .addStreamsIO_UI()
                .doOnSubscribe { liveDataForOfferResult.value = AppState.Loading(null) }
                .subscribeWith(getCompletableObserver())
        )
    }



    private fun getCompletableObserver(): DisposableCompletableObserver {
        return object : DisposableCompletableObserver() {

            override fun onError(e: Throwable) {
                liveDataForOfferResult.value = AppState.Error(e)
            }

            override fun onComplete() {
                liveDataForOfferResult.value = AppState.Success<Unit>(Unit)
            }

        }
    }
}