package org.vernality.profitclub.view_model

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.get
import org.koin.core.inject
import org.vernality.profitclub.di.application
import java.util.*

enum class RoleField{Name, INN, FCS, Phone}


class EnterRoleDataViewModel(appContext: Application) : BaseViewModel(appContext) {

    private var counter =0

    private val context = appContext

    private var role: Role? = null

    private val enterData: MutableMap<RoleField, String?> =
        mutableMapOf(RoleField.Name to null, RoleField.INN to null, RoleField.FCS to null, RoleField.Phone to null)


    val resultLiveData: MutableLiveData<Result> by lazy {
        MutableLiveData<Result>()
    }


    fun setNameOfOrganization(name: String){
        enterData[RoleField.Name] = name
        Toast.makeText(context, "enter name = " + name, Toast.LENGTH_LONG).show()
        println("Maps RoleField.Name = " + enterData[RoleField.Name])
    }

    fun setINN(inn: String){
        enterData[RoleField.INN] = inn
    }

    fun setFCS(fcs: String){
        enterData[RoleField.FCS] = fcs
    }

    fun setPhone(phone: String){
        enterData[RoleField.Phone] = phone
    }

    fun getFields():MutableMap<RoleField, String?>{

        counter++

        println("---- counter = "+counter)

        for(o in enterData){

            println("viewModel maps = "+ o.key+ "  " + o.value)
        }

        return enterData
    }


    fun sendData(){

        if (checkFields() == Result.Success) resultLiveData.value = Result.Success
        else resultLiveData.value = Result.Error
    }

    private fun checkFields():Result {
        return Result.Success
    }




}