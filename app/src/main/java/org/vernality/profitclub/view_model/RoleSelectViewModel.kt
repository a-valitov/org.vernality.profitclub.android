package org.vernality.profitclub.view_model

import android.app.Application
import androidx.lifecycle.MutableLiveData

enum class Role{Provider, Organization, Participant}

enum class FieldOrg{Name, INN, ContactName, Phone}

class RoleSelectViewModel(appContext: Application) : BaseViewModel(appContext) {

    private val context = appContext

    private var role: Role? = null

    val resultLiveData: MutableLiveData<Result?> by lazy {
        MutableLiveData<Result?>()
    }

    val messageLiveData: MutableLiveData<String?> by lazy {
        MutableLiveData<String?>()
    }


    fun setRoleProvider(){
        role = Role.Provider
    }

    fun setRoleOrganization(){
        role = Role.Organization
    }

    fun setRoleParticipant(){
        role = Role.Participant
    }


    fun getRole():Role?{
        return role
    }


    fun resume(){

        if(checkBox()) resultLiveData.value = Result.Success

    }

    fun checkBox():Boolean{
        return role != null
    }

    fun deleteShowedMessage(){
        messageLiveData.value = null
    }

    fun clearResult() {resultLiveData.value = null}


    override fun onCleared() {
        println("-------onCleared in RoleSelectViewModel")
        super.onCleared()

        resultLiveData.value = null
    }



}