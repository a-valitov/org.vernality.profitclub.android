package org.vernality.profitclub.view_model

import android.app.Application
import androidx.lifecycle.MutableLiveData
import org.koin.android.viewmodel.ext.android.viewModel


public class EnterRoleActivityViewModel(appContext: Application) : BaseViewModel(appContext) {

    private val context = appContext


    private val selectRole: MutableLiveData<Role> by lazy{MutableLiveData<Role>()}



    fun setRoleProvider(){
        selectRole.value = Role.Provider
    }

    fun setRoleOrganization(){
        selectRole.value = Role.Organization
        println("-----------setRole Organization")
    }

    fun setRoleParticipant(){
        selectRole.value = Role.Participant
    }

    fun setRole(role: Role){
        selectRole.value = role
    }


    fun getRoleLiveData():MutableLiveData<Role>{
        return selectRole
    }





}