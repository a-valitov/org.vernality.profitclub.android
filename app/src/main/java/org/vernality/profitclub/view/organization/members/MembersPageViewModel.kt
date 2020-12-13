package org.vernality.profitclub.view.organization.members

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import org.vernality.profitclub.model.data.AppState
import org.vernality.profitclub.model.data.CommercialOffer
import org.vernality.profitclub.model.data.Member
import org.vernality.profitclub.utils.ui.addStreamsIO_UI
import org.vernality.profitclub.view_model.BaseViewModel
import timber.log.Timber

class MembersPageViewModel(appContext: Application) : BaseViewModel<AppState>(appContext) {

    private val liveDataForMemberResult: MutableLiveData<AppState> = MutableLiveData()

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
                .addStreamsIO_UI()
                .doOnSubscribe { liveDataForViewToObserve.value = AppState.Loading(null) }
                .subscribeWith(getObserver())
        )
    }

    private fun getResultForRequestMembers(){
        compositeDisposable.add(
            interactor.getRequestMembers()
                .addStreamsIO_UI()
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

//   liveData для результата одобрения/ неодобрения участника

    fun getLiveDataForResultMember(btn: Int, member: Member): LiveData<AppState>{
        when(btn)
        {
            0 -> getResultForAcceptBtn(member)
            1 -> getResultForRejectBtn(member)
        }

        return liveDataForMemberResult
    }

    private fun getResultForRejectBtn(member: Member) {
        Timber.d("getResultForRejectBtn(offer: CommercialOffer)")
        compositeDisposable.add(
            interactor.getResultForApproveMembersRequest(member)
                .addStreamsIO_UI()
                .doOnSubscribe { liveDataForMemberResult.value = AppState.Loading(null) }
                .subscribeWith(getCompletableObserver())
        )
    }

    private fun getResultForAcceptBtn(member: Member) {
        Timber.d("getResultForAcceptBtn(offer: CommercialOffer)")
        compositeDisposable.add(
            interactor.getResultForRejectMembersRequest(member)
                .addStreamsIO_UI()
                .doOnSubscribe { liveDataForMemberResult.value = AppState.Loading(null) }
                .subscribeWith(getCompletableObserver())
        )
    }

    private fun getCompletableObserver(): DisposableCompletableObserver {
        return object : DisposableCompletableObserver() {

            override fun onError(e: Throwable) {
                liveDataForMemberResult.value = AppState.Error(e)
            }

            override fun onComplete() {
                liveDataForMemberResult.value = AppState.Success<Unit>(Unit)
            }

        }
    }

//   liveData для определения типа выводимого списка (участники/ заявки)

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