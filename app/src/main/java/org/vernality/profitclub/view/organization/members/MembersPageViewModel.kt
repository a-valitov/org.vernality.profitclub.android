package org.vernality.profitclub.view.organization.members

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import org.vernality.profitclub.model.data.AppState
import org.vernality.profitclub.view_model.BaseViewModel

class MembersPageViewModel(appContext: Application) : BaseViewModel<AppState>(appContext) {

    fun getLiveDataAndStartGetResult(page:Int): LiveData<AppState> {

        when(page){
            0 -> getResultForMembers()
            1 -> getResultForRequestMembers()
        }



        return liveDataForViewToObserve
    }

    private fun getResultForMembers(){
        compositeDisposable.add(
            interactor.getMembers()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { liveDataForViewToObserve.value = AppState.Loading(null) }
                .subscribeWith(getObserver())
        )
    }

    private fun getResultForRequestMembers(){
        compositeDisposable.add(
            interactor.getRequestMembers()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { liveDataForViewToObserve.value = AppState.Loading(null) }
                .subscribeWith(getObserver())
        )
    }

    private fun getObserver(): DisposableObserver<AppState> {
        return object : DisposableObserver<AppState>() {

            override fun onNext(state: AppState) {
                liveDataForViewToObserve.value = state
            }

            override fun onError(e: Throwable) {
                liveDataForViewToObserve.value = AppState.Error(e)
            }

            override fun onComplete() {
            }
        }
    }


    private val _index = MutableLiveData<Int>()
    val page: LiveData<Int> = Transformations.map(_index) {
        it
    }

    fun setIndex(index: Int) {
        _index.value = index
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}