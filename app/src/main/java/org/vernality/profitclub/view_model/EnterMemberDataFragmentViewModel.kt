package org.vernality.profitclub.view_model

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Completable
import io.reactivex.observers.DisposableCompletableObserver
import org.vernality.profitclub.model.data.*
import org.vernality.profitclub.utils.ui.UIUtils
import org.vernality.profitclub.utils.ui.addStreamsIO_UI
import org.vernality.profitclub.view.activities.DataRoleSaver

enum class FieldMember{FirstName, LastName}

class EnterMemberDataFragmentViewModel(appContext: Application) : BaseViewModel<AppState>(appContext) {

    private val context  = appContext

    var member = Member()

    var agree: Boolean = false

    fun getLiveDataAndStartGetResult(): LiveData<AppState> {

        if(checkFields())
        {
            getResult()
            DataRoleSaver.member = member
        }

        return liveDataForViewToObserve
    }

    @SuppressLint("CheckResult")
    private fun getResult(){

        val completable: Completable

//        completable = interactor.becameMemberOfOrganization(member)
//        }
//
//        completable
//            .addStreamsIO_UI()
//            .doOnSubscribe { liveDataForViewToObserve.value = AppState.Loading(null) }
//            .subscribeWith(getObserver())

    }

    private fun getObserver(): DisposableCompletableObserver {
        return object : DisposableCompletableObserver() {

            override fun onError(e: Throwable) {
                liveDataForViewToObserve.value = AppState.Error(e)
            }

            override fun onComplete() {
                liveDataForViewToObserve.value = AppState.Success<Unit>(Unit)
                DataRoleSaver.member = member
            }

        }
    }



    fun setFirstName(name: String){
        member.firstName = name
    }

    fun setLastName(name: String){
        member.lastName = name
    }

    fun setAgree() {agree = true}

    fun openPagePrivacyPolicy(){
        UIUtils.openWebPage(context, URI_PRIVACY_POLICY)
    }


    private fun checkFields():Boolean {
        println("checkFields()")
        return checkName(member.firstName)
                && checkName(member.lastName)
                && checkAgree()

    }


    private fun checkName(name: String?):Boolean{
        if(!(name.isNullOrEmpty() || name.isNullOrBlank())) {
            println("checkName() = true")
            return true
        }
        else {
            println("-------"+message + FieldOrg.Name.name)
            liveDataForViewToObserve.value = AppState.Error(Throwable("некорректно введено имя"))
            return false
        }
    }

    private fun checkAgree(): Boolean{
        if(agree) return true
        else
        {
            liveDataForViewToObserve.value = AppState.Error(Throwable("необходимо подтвердить согласие с политикой конфедециальности"))
            return false
        }
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}