package org.vernality.profitclub.view_model

import android.app.Application
import androidx.lifecycle.MutableLiveData

enum class Role{Provider, Organization, Participant}


class RoleSelectViewModel(appContext: Application) : BaseViewModel(appContext) {

    private val context = appContext

    private var role: Role? = null

    private val registration: MutableMap<Field, String?> =
        mutableMapOf(Field.Login to null, Field.Gmail to null, Field.Password to null, Field.Password2 to null)


    val resultLiveData: MutableLiveData<Result> by lazy {
        MutableLiveData<Result>()
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

    }




}