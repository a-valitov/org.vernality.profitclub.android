package org.vernality.profitclub.view_model

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.MutableLiveData
import org.koin.core.inject
import org.vernality.profitclub.model.repository.RepositoryImplementation

enum class RoleField{Name, INN, FCS, Phone}


class EnterRoleDataViewModel(appContext: Application) : BaseViewModel(appContext) {


    init {
        println("--------------EnterRoleDataViewModel init")
    }

    private val context = appContext

    private var role: Role? = null

    private val repository by inject<RepositoryImplementation>()

//    private val enterData: MutableMap<RoleField, String?> =
//        mutableMapOf(RoleField.Name to null, RoleField.INN to null, RoleField.FCS to null, RoleField.Phone to null)


    val resultLiveData: MutableLiveData<Result> by lazy {
        MutableLiveData<Result>()
    }


    val registrationOrg: MutableMap<FieldOrg, String?> =
        mutableMapOf(FieldOrg.Name to null, FieldOrg.INN to null, FieldOrg.ContactName to null, FieldOrg.Name to null)


    val messageLiveData: MutableLiveData<String?> by lazy {
        MutableLiveData<String?>()
    }

    fun setRole(_role: Role){
        role = _role
    }


    fun setNameOfOrganization(name: String){
        registrationOrg[FieldOrg.Name] = name
    }

    fun setINN(inn: String){
        registrationOrg[FieldOrg.INN] = inn
    }

    fun setFCS(contactName: String){
        registrationOrg[FieldOrg.ContactName] = contactName
    }

    fun setPhone(phone: String){
        registrationOrg[FieldOrg.Phone] = phone
    }

    fun getFields() = registrationOrg


    fun getRole():org.vernality.profitclub.model.data.Role{
        return org.vernality.profitclub.model.data.Role(name = registrationOrg[FieldOrg.Name]!!,
        INN = registrationOrg[FieldOrg.INN]!!, contactName = registrationOrg[FieldOrg.ContactName]!!,
        phone = registrationOrg[FieldOrg.Phone]!!)
    }

    fun sendData(){
        println("sendData(), role = "+role?.name)
        if ( checkFields()
        ) when(role){
            Role.Organization -> createOrganizationResult()
            Role.Participant -> createMemberResult()
            Role.Provider -> createSupplierResult()
        }
        else resultLiveData.value = Result.Error
    }

    @SuppressLint("CheckResult")
    private fun createOrganizationResult(){
        println("createOrganizationResult()")
        repository.createOrganization(getRole()).subscribe(
            {resultLiveData.value = Result.Success
            println("-------Completed create organization")},
            {e -> messageLiveData.value= e.message}
        )
    }

    @SuppressLint("CheckResult")
    private fun createMemberResult(){
        repository.createMember(getRole()).subscribe(
            {resultLiveData.value = Result.Success},
            {e -> messageLiveData.value= e.message}
        )
    }

    @SuppressLint("CheckResult")
    private fun createSupplierResult(){
        repository.createSupplier(getRole()).subscribe(
            {resultLiveData.value = Result.Success},
            {e -> messageLiveData.value= e.message}
        )
    }

    private fun checkFields():Boolean {
        println("checkFields()")
        return checkName(registrationOrg[FieldOrg.Name])
                && checkINN(registrationOrg[FieldOrg.INN])
                && checkContactName(registrationOrg[FieldOrg.ContactName])
                && checkPhone(registrationOrg[FieldOrg.Phone]
        )
    }



    fun deleteShowedMessage(){
        messageLiveData.value = null
    }


    private fun checkName(name: String?):Boolean{
        if(!(name.isNullOrEmpty() || name.isNullOrBlank())) {
            println("checkName() = true")
            return true
        }
        else {
            println("-------"+message + FieldOrg.Name.name)
            messageLiveData.value = message + FieldOrg.Name.name
            return false
        }
    }

    private fun checkINN(INN: String?):Boolean{
        if(!INN.isNullOrEmpty() && !INN.isNullOrBlank() && (INN.toLongOrNull() !=null)) {
            println("checkINN() = true")
            return true
        }
        else {
            println("-------"+message + FieldOrg.INN.name)
            messageLiveData.value = message + FieldOrg.INN.name
            return false
        }
    }

    private fun checkContactName(name: String?):Boolean{
        val NAME_REGEX = """^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#${'$'}%!\-_?&])(?=\S+${'$'}).{6,40}""".toRegex()
        val NAME_REGEX2 = Regex("""^[A-Z][a-z]{2,}\040[A-Z][a-z]{2,}$""")
        val NAME_REGEX3 = Regex("""^[А-ЯA-Z][а-яa-zА-ЯA-Z\-]{0,}\s[А-ЯA-Z][а-яa-zА-ЯA-Z\-]{1,}(\s[А-ЯA-Z][а-яa-zА-ЯA-Z\-]{1,})?$""")
        if(!name.isNullOrEmpty() && !name.isNullOrBlank()) {
            println("checkContactName() = true")
            return true
        }
        else {
            println("-------name= "+ name+message + FieldOrg.ContactName.name)
            messageLiveData.value = message + FieldOrg.ContactName.name
            return false
        }
    }

    private fun checkPhone(phone: String?):Boolean{
        val PHONE_REGEX = """^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#${'$'}%!\-_?&])(?=\S+${'$'}).{6,40}""".toRegex()
        val PHONE_REGEX2 =Regex("""(\s*)?(\+)?([- _():=+]?\d[- _():=+]?){10,14}(\s*)?""")
        val PHONE_REGEX3 = Regex("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}\$")
        if(!phone.isNullOrBlank() && !phone.isNullOrEmpty() && PHONE_REGEX3.matches(phone)){
            println("checkPhone() = true")
            return true
        } else {
            println("-------phone = "+phone+message + FieldOrg.Phone.name)
            messageLiveData.value = message + FieldOrg.Phone.name
            return false
        }
    }




}