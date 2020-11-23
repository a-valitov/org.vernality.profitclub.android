package org.vernality.profitclub.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.vernality.profitclub.model.data.AppState
import org.vernality.profitclub.utils.DataSaver

enum class Role{Supplier, Organization, Member}

enum class FieldOrg{Name, INN, ContactName, Phone}

class RoleSelectFragmentViewModel(appContext: Application) : BaseViewModel<AppState>(appContext) {


    private var role: Role? = null

    fun getLiveDataAndStartGetResult(): LiveData<AppState> {

        if(checkFields()) getResult()
        else liveDataForViewToObserve.value = AppState.Error(Throwable("ошибка заполнения полей"))

        return liveDataForViewToObserve
    }

    private fun checkFields():Boolean = role != null


    private fun getResult(){
        liveDataForViewToObserve.value = AppState.Success<Unit>(Unit)
    }


    fun setRoleProvider(){
        role = Role.Supplier
        DataSaver.setData(role)
    }

    fun setRoleOrganization(){
        role = Role.Organization
        DataSaver.setData(role)
    }

    fun setRoleParticipant(){
        role = Role.Member
        DataSaver.setData(role)
    }


    fun getRole():Role?{
        return role
    }



    fun clearResult() {
        liveDataForViewToObserve.value = null}


    override fun onCleared() {
        println("-------onCleared in RoleSelectViewModel")
        super.onCleared()
        compositeDisposable.clear()
        liveDataForViewToObserve.value = null
    }

}