package org.vernality.profitclub.view.member.actions

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
import org.vernality.profitclub.utils.ui.addStreamsIO_UI
import org.vernality.profitclub.view.activities.DataRoleSaver
import org.vernality.profitclub.view_model.BaseViewModel

class ActionsForMemberPageViewModel(appContext: Application) : BaseViewModel<AppState>(appContext) {

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
                .addStreamsIO_UI()
                .doOnSubscribe { liveDataForViewToObserve.value = AppState.Loading(null) }
                .subscribeWith(getObserver())
        )
    }

    private fun getResultForPastActions(){
        compositeDisposable.add(
            interactor.getPastActions()
                .addStreamsIO_UI()
                .doOnSubscribe { liveDataForViewToObserve.value = AppState.Loading(null) }
                .subscribeWith(getObserver())
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