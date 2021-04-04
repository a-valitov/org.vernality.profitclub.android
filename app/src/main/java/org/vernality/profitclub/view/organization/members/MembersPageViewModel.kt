package org.vernality.profitclub.view.organization.members

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import org.vernality.profitclub.model.data.AppState
import org.vernality.profitclub.model.data.CommercialOffer
import org.vernality.profitclub.model.data.Member
import org.vernality.profitclub.model.data.Organization
import org.vernality.profitclub.utils.ui.addStreamsIO_UI
import org.vernality.profitclub.view_model.BaseViewModel
import timber.log.Timber

class MembersPageViewModel(appContext: Application) : BaseViewModel<AppState>(appContext) {

    private val liveDataForMemberResult: MutableLiveData<AppState> = MutableLiveData()

    fun getLiveDataAndStartGetResult(page:Int, organization: Organization): LiveData<AppState> {

        when(page){
            0 -> getResultForMembers(organization)
            1 -> getResultForRequestMembers(organization)
        }



        return liveDataForViewToObserve
    }

    private fun getResultForMembers(organization: Organization){
        compositeDisposable.add(
            interactor.getMembers(organization)
                .addStreamsIO_UI()
                .doOnSubscribe { liveDataForViewToObserve.value = AppState.Loading(null) }
                .subscribeWith(getObserver())
        )
    }

    private fun getResultForRequestMembers(organization: Organization){
        compositeDisposable.add(
            interactor.getRequestMembers(organization)
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