package org.vernality.profitclub.model.datasource

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.vernality.profitclub.model.data.*

interface DataSource {

    fun getData(password: String, email:String):User?



    fun registration(user: User):Completable

    fun logOut():Completable

    fun signIn(user: User):Completable

    fun resetPassword(email: String):Completable

    fun createOrganization(organization: Organization):Completable

    fun createSupplier(supplier: Supplier): Completable

    fun createMember(member: Member): Completable

    fun getUserName():Single<String>

    fun getOrganization():Observable<List<Organization>>

    fun getMyOrganizations():Observable<List<Organization>>

    fun getMySuppliers():Observable<List<Supplier>>

    fun getMyMembers():Observable<List<Member>>

    fun getOrganizationsForMyMembers(): Observable<List<Organization>>

    fun becameMemberOfOrganization(member: Member, organization: Organization): Completable

    fun getActions():Single<List<Action>>

    fun getMembersOfOrganization(organization: Organization):Single<List<Member>>

    fun getRequestOfOrganization(organization: Organization): Single<List<Member>>

    fun getCommercialOffers(): Single<List<CommercialOffer>>

    fun createAction(action: Action, supplier: Supplier): Completable

    fun createOffer(offer: CommercialOffer, supplier: Supplier): Completable

}
