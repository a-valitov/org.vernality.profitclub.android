package org.vernality.profitclub.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import org.vernality.profitclub.model.data.*
import org.vernality.profitclub.utils.DataSaver
import org.vernality.profitclub.utils.ui.addStreamsIO_UI
import org.vernality.profitclub.view.activities.DataRoleSaver
import org.vernality.profitclub.view_model.BaseViewModel

class MyOrganizationsListFragmentViewModel(appContext: Application) : BaseViewModel<AppState>(appContext) {


    data class MyOrganizationsData(val dataOrg:List<Organization>, val dataSup:List<Supplier>, val dataMem:List<Member>)

    private val liveDataForLogOutResult: MutableLiveData<AppState> = MutableLiveData()

    fun getLiveDataAndStartGetResult(): LiveData<AppState> {

        getResult()

        return liveDataForViewToObserve
    }

    fun getLiveDataAndStartGetResultForLogOut(): LiveData<AppState> {

        getResultForLogOut()

        return liveDataForLogOutResult
    }


    private fun getResult(){
        compositeDisposable.add(
            interactor.getDataForMyOrganizations()
                .addStreamsIO_UI()
                .doOnSubscribe { liveDataForViewToObserve.value = AppState.Loading(null) }
                .subscribeWith(getObserver())
        )
    }

    private fun getResultForLogOut(){
        compositeDisposable.add(
            interactor.logOut()
                .addStreamsIO_UI()
                .doOnSubscribe { liveDataForViewToObserve.value = AppState.Loading(null) }
                .subscribeWith(getCompletableObserver())
        )
    }

    fun setOrganization(organization: Organization){
        DataSaver.setData(organization)
    }

    private fun getObserver(): DisposableObserver<AppState> {
        return object : DisposableObserver<AppState>() {
            override fun onComplete() {

            }

            override fun onNext(t: AppState) {
                liveDataForViewToObserve.value = t
            }

            override fun onError(e: Throwable) {
                liveDataForViewToObserve.value = AppState.Error(e)
            }

        }
    }

    private fun getCompletableObserver(): DisposableCompletableObserver {
        return object : DisposableCompletableObserver() {

            override fun onError(e: Throwable) {
                liveDataForLogOutResult.value = AppState.Error(e)
            }

            override fun onComplete() {
                liveDataForLogOutResult.value = AppState.Success<Unit>(Unit)
            }

        }
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}