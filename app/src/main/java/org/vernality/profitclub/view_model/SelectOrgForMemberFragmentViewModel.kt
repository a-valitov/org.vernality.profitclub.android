package org.vernality.profitclub.view_model

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import org.koin.core.inject
import org.vernality.profitclub.model.data.AppState
import org.vernality.profitclub.model.data.Organization
import org.vernality.profitclub.model.repository.RepositoryImplementation
import org.vernality.profitclub.view.activities.DataRoleSaver


class SelectOrgForMemberFragmentViewModel(appContext: Application) : BaseViewModel<AppState>(appContext) {


    var member = DataRoleSaver.member

    lateinit var organization: Organization

    private val repository: RepositoryImplementation by inject()

    val resultLiveData: MutableLiveData<AppState> by lazy {
        MutableLiveData<AppState>()
    }

    val listLiveData: MutableLiveData<AppState> by lazy {
        MutableLiveData<AppState>()
    }


    fun getLiveDataAndStartGetResult(organization: Organization): LiveData<AppState> {

        if(true) getResult(organization)

        return resultLiveData
    }

    @SuppressLint("CheckResult")
    private fun getResult(organization: Organization){

            interactor.becameMemberOfOrganization(member!!, organization)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .doOnSubscribe { resultLiveData.value = AppState.Loading(null) }
            .subscribeWith(getCompletableObserver())


    }


    private fun getCompletableObserver(): DisposableCompletableObserver {
        return object : DisposableCompletableObserver() {

            override fun onError(e: Throwable) {
                resultLiveData.value = AppState.Error(e)
                println("Error = " +e)
            }

            override fun onComplete() {
                resultLiveData.value = AppState.Success<Unit>(Unit)
            }

        }
    }





    @SuppressLint("CheckResult")
    fun getUserName():String{
        var name =""
        repository.getUserName().subscribe(
            {s-> name = s},
            {t-> resultLiveData.value = AppState.Error(t)}
        )

        return name
    }

    @SuppressLint("CheckResult")
    fun getOrganizationList(){

        println("into viewModel getOrganization List")
        compositeDisposable.add(
            interactor.getOrganization()
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .doOnSubscribe { listLiveData.value = AppState.Loading(null) }
                    .subscribeWith(getObserver())
        )

    }

    private fun getObserver(): DisposableObserver<AppState> {
        return object : DisposableObserver<AppState>() {

            override fun onNext(state: AppState) {
                println("into onnNext, apstate = "+state)
                listLiveData.value = state
            }

            override fun onError(e: Throwable) {
                listLiveData.value = AppState.Error(e)
            }

            override fun onComplete() {
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        resultLiveData.value = null
    }



}