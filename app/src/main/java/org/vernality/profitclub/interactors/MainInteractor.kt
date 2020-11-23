package org.vernality.profitclub.interactors

import android.annotation.SuppressLint
import com.parse.ParseException
import io.reactivex.*
import io.reactivex.Observable
import org.vernality.profitclub.model.data.*
import org.vernality.profitclub.model.repository.Repository
import org.vernality.profitclub.model.repository.RepositoryImplementation
import java.util.*
import java.util.concurrent.TimeUnit



class MainInteractor(
    private val repository: Repository
) {

    @SuppressLint("CheckResult")
    fun registration(user: User): Completable{

       return repository.registration(user)

    }

    fun logOut(): Completable{

        return repository.logOut()

    }

    fun signIn(user: User): Completable{

        return repository.signIn(user)

    }

    fun resetPassword(email: String): Completable{

        return repository.resetPassword(email)

    }

    fun createOrganization(organization: Organization): Completable{

        return repository.createOrganization(organization)

    }

    fun createSupplier(supplier: Supplier): Completable{

        return repository.createSupplier(supplier)

    }

    fun becameMemberOfOrganization(member: Member, organization: Organization): Completable{

        return repository.becameMemberOfOrganization(member, organization)

    }

    fun getOrganization(): Observable<AppState> {
        return repository.getOrganization().map { list -> AppState.Success<List<Organization>>(list) }
    }

    fun getMyOrganizations() = repository.getMyOrganization().map { list -> AppState.Success<List<Organization>>(list) }

    fun getCurrentStocks():Observable<AppState> {
        val stocksList = listOf<org.vernality.profitclub.model.data.Action>(
            Action().apply {
                message = "Купите семечки"
                descriptionOf="Купите семечки по 100 р за кулек"
                startDate="12.03.2021"
                endDate="12.08.2021"
                link="http://www.ru"
            },
            Action().apply {
                message = "Купите спички"
                descriptionOf="Купите семечки по 100 р за кулек"
                startDate="12.03.2021"
                endDate="12.08.2021"
                link="http://www.ru"
            },
            Action().apply {
                message = "Купите дизель"
                descriptionOf="Купите семечки по 10000 р за шт"
                startDate="12.03.2021"
                endDate="12.08.2021"
                link="http://www.ru"
            }
        )

        return Observable.just(AppState.Success<List<org.vernality.profitclub.model.data.Action>>(stocksList))
    }

    fun getPastStocks(): Observable<AppState.Success<List<org.vernality.profitclub.model.data.Action>>>? {
        val stocksList = listOf<org.vernality.profitclub.model.data.Action>(
            Action().apply {
                message = "Купите BMV"
                descriptionOf="Купите BMV за 100 мильенов"
                startDate="12.03.2020"
                endDate="12.08.2020"
                link="http://www.ru"
            },
            Action().apply {
                message = "Купите спички"
                descriptionOf="Купите семечки по 100 р за кулек"
                startDate="12.03.2020"
                endDate="12.08.2020"
                link="http://www.ru"
            },
            Action().apply {
                message = "Купите дизель"
                descriptionOf="Купите семечки по 10000 р за шт"
                startDate="12.03.2020"
                endDate="12.08.2020"
                link="http://www.ru"
            }
        )

        return Observable.just(AppState.Success<List<org.vernality.profitclub.model.data.Action>>(stocksList)).delay(1000, TimeUnit.MILLISECONDS)
    }


    fun getMembers():Observable<AppState> {
        val membersList = listOf<org.vernality.profitclub.model.data.Member>(
            Member().apply {
                name = "Jack"
                firstName="Jack"
                lastName="More"
            },
            Member().apply {
                name = "Nick"
                firstName="Nick"
                lastName="More"
            },
            Member().apply {
                name = "Bob"
                firstName="Bob"
                lastName="More"
            }
        )

        return Observable.just(AppState.Success<List<org.vernality.profitclub.model.data.Member>>(membersList))
    }

    fun getRequestMembers():Observable<AppState> {
        val membersList = listOf<org.vernality.profitclub.model.data.Member>(
            Member().apply {
                name = "Donald"
                firstName="Donald"
                lastName="Trump"
            },
            Member().apply {
                name = "Boris"
                firstName="Boris"
                lastName="Jonson"
            },
            Member().apply {
                name = "Vlad"
                firstName="Vlad"
                lastName="Drackula"
            }
        )

        return Observable.just(AppState.Success<List<org.vernality.profitclub.model.data.Member>>(membersList))
    }


    fun getCommercialOffer():Single<AppState> {

        return repository.getCommercialOffers().map { list -> AppState.Success<List<CommercialOffer>>(list) }
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentActions():Single<List<Action>>{
        val df = java.text.SimpleDateFormat("dd.MM.yyyy")
        val currentDate  = Date()

        return repository.getActions().map{
            val currentActions = mutableListOf<Action>()
            it.forEach {
                if(df.parse(it.startDate) <= currentDate && df.parse(it.endDate) >= currentDate) currentActions.add(it)
            }
            currentActions
        }
    }

    fun getPastActions():Single<List<Action>>{
        val df = java.text.SimpleDateFormat("dd.MM.yyyy")
        val currentDate = Date()

        return repository.getActions().map{
            val pastActions = mutableListOf<Action>()
            it.forEach {
                if(df.parse(it.endDate) < currentDate) pastActions.add(it)

            }
            pastActions
        }
    }

}
