package org.vernality.profitclub.view_model

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.get
import org.koin.core.inject
import org.vernality.profitclub.di.application
import java.util.*

enum class Role{Provider, Organization, Participant}


class RoleSelectViewModel(appContext: Application) : BaseViewModel(appContext) {

    private val context = appContext

    private var role: Role? = null

    private val registration: MutableMap<Field, String?> =
        mutableMapOf(Field.Login to null, Field.Gmail to null, Field.Password1 to null, Field.Password2 to null)


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