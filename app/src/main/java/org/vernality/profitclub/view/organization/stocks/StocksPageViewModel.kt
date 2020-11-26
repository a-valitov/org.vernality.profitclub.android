package org.vernality.profitclub.view.organization.stocks

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import org.vernality.profitclub.model.data.Action
import org.vernality.profitclub.model.data.AppState
import org.vernality.profitclub.model.data.Organization
import org.vernality.profitclub.view.activities.DataRoleSaver
import org.vernality.profitclub.view_model.BaseViewModel

class StocksPageViewModel(appContext: Application) : BaseViewModel<AppState>(appContext) {

    private val liveDataForActionResult: MutableLiveData<AppState> = MutableLiveData()

    fun getLiveDataAndStartGetResult(page:Int): LiveData<AppState> {

        when(page){
            0 -> getResultForCurrentActions()
            1 -> getResultForPastActions()
        }

        return liveDataForViewToObserve
    }

    private fun getResultForCurrentActions(){
        compositeDisposable.add(
            interactor.getCurrentActions()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { liveDataForViewToObserve.value = AppState.Loading(null) }
                .subscribeWith(getObserver())
        )
    }

    private fun getResultForPastActions(){
        compositeDisposable.add(
            interactor.getPastActions()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { liveDataForViewToObserve.value = AppState.Loading(null) }
                .subscribeWith(getObserver())
        )
    }

    fun getLiveDataForAction(btn: Int, action: Action): LiveData<AppState>{
        when(btn){
            0 -> getResultForAcceptBtn(action)
            1 -> getResultForRejectBtn(action)
        }
        return liveDataForActionResult
    }

    private fun getResultForRejectBtn(action: Action) {
        compositeDisposable.add(
            interactor.getResultForRejectAction(action)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { liveDataForActionResult.value = AppState.Loading(null) }
                .subscribeWith(getCompletableObserver())
        )
    }

    private fun getResultForAcceptBtn(action: Action) {
        compositeDisposable.add(
            interactor.getResultForAcceptAction(action)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { liveDataForActionResult.value = AppState.Loading(null) }
                .subscribeWith(getCompletableObserver())
        )
    }

    private fun getObserver(): DisposableSingleObserver<List<Action>> {
        return object : DisposableSingleObserver<List<Action>>() {

            override fun onSuccess(state: List<Action>) {
                liveDataForViewToObserve.value = AppState.Success<List<Action>>(state)
            }

            override fun onError(e: Throwable) {
                liveDataForViewToObserve.value = AppState.Error(e)
            }

        }
    }

    private fun getCompletableObserver(): DisposableCompletableObserver {
        return object : DisposableCompletableObserver() {

            override fun onError(e: Throwable) {
                liveDataForActionResult.value = AppState.Error(e)
            }

            override fun onComplete() {
                liveDataForActionResult.value = AppState.Success<Unit>(Unit)
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