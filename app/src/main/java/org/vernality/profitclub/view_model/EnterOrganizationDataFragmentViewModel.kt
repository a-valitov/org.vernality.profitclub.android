package org.vernality.profitclub.view_model

import android.annotation.SuppressLint
import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Completable
import io.reactivex.observers.DisposableCompletableObserver
import org.vernality.profitclub.model.data.*
import org.vernality.profitclub.utils.DataSaver
import org.vernality.profitclub.utils.ui.UIUtils

enum class RoleField{Name, INN, FCS, Phone}

const val URI_PRIVACY_POLICY = "https://vernality.org/about.php"

class EnterOrganizationDataFragmentViewModel(appContext: Application) : BaseViewModel<AppState>(appContext) {


    var baseCompany: BaseCompany = BaseCompany()


//    private var role: Role? = null

    private val context  = appContext

    val role = DataSaver.getData<Role>()

    var agree: Boolean = false

    fun getLiveDataAndStartGetResult(): LiveData<AppState> {

        if(true) getResult()

        return liveDataForViewToObserve
    }

    @SuppressLint("CheckResult")
    private fun getResult(){

        val completable: Completable

        completable = if(role == Role.Organization) {
            val organization = Organization().apply { setFields(baseCompany) }
            interactor.createOrganization(organization)
        }
        else {
            val supplier = Supplier().apply { setFields(baseCompany) }
            interactor.createSupplier(supplier)
        }
        completable
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { liveDataForViewToObserve.value = AppState.Loading(null) }
                .subscribeWith(getObserver())


    }



    private fun getObserver(): DisposableCompletableObserver {
        return object : DisposableCompletableObserver() {

            override fun onError(e: Throwable) {
                liveDataForViewToObserve.value = AppState.Error(e)
            }

            override fun onComplete() {
                liveDataForViewToObserve.value = AppState.Success<Unit>(Unit)
            }

        }
    }




//    fun setRole(_role: Role){
//        role = _role
//    }


    fun setNameOfOrganization(name: String){
        baseCompany?.name = name
    }

    fun setINN(inn: String){
        baseCompany?.inn = inn
    }

    fun setFCS(contactName: String){
        baseCompany?.contactName = contactName
    }

    fun setPhone(phone: String){
        baseCompany?.phone = phone
    }

    fun setAgree() {agree = true}

    fun openPagePrivacyPolicy(){
        UIUtils.openWebPage(context, URI_PRIVACY_POLICY)
    }


    private fun checkFields():Boolean {
        println("checkFields()")
        return checkName(baseCompany?.name)
                && checkINN(baseCompany?.inn)
                && checkContactName(baseCompany?.contactName)
                && checkPhone(baseCompany?.phone)
                && checkAgree()
    }



    private fun checkName(name: String?):Boolean{
        if(!(name.isNullOrEmpty() || name.isNullOrBlank())) {
            println("checkName() = true")
            return true
        }
        else {
            println("-------"+message + FieldOrg.Name.name)
            liveDataForViewToObserve.value = AppState.Error(Throwable("поле имя заполнено некорректно"))
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
            liveDataForViewToObserve.value = AppState.Error(Throwable("поле ИНН заполнено некорректно"))
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
            liveDataForViewToObserve.value = AppState.Error(Throwable("поле ФИО заполнено некорректно"))
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
            liveDataForViewToObserve.value = AppState.Error(Throwable("введен некорректный номер телефона"))
            return false
        }
    }

    private fun checkAgree(): Boolean{
        if(agree) return true
        else
        {
            liveDataForViewToObserve.value = AppState.Error(Throwable("Необходимо подтвердить согласие с политикой конфедециальности"))
            return false
        }
    }



    override fun onCleared() {
        compositeDisposable.clear()
    }

}