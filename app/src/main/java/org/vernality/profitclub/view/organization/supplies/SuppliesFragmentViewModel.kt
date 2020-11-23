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
import org.vernality.profitclub.view_model.BaseViewModel

class SuppliesFragmentViewModel(appContext: Application) : BaseViewModel<AppState>(appContext) {


    fun getLiveDataAndStartGetResult(): LiveData<AppState> {

        getResult()

        return liveDataForViewToObserve
    }


    private fun getResult(){
        compositeDisposable.add(
            interactor.getCommercialOffer()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { liveDataForViewToObserve.value = AppState.Loading(null) }
                .subscribeWith(getObserver())
        )
    }

    private fun getObserver(): DisposableSingleObserver<AppState> {
        return object : DisposableSingleObserver<AppState>() {

            override fun onSuccess(state: AppState) {
                liveDataForViewToObserve.value = state
            }

            override fun onError(e: Throwable) {
                liveDataForViewToObserve.value = AppState.Error(e)
            }

        }
    }
}