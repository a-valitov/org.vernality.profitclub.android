package org.vernality.profitclub.model.repository

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.vernality.profitclub.model.data.*

interface Repository{


    fun getUser(password: String, email: String):User?

    fun registration(user: User):Completable

    fun signIn(user: User):Completable

    fun logOut():Completable

    fun resetPassword(email: String):Completable

    fun createOrganization(organization: Organization):Completable

    fun createSupplier(supplier: Supplier):Completable

    fun createMember(member: Member):Completable

    fun getUserName():Single<String>

    fun getOrganization(): Observable<List<Organization>>

    fun getMyOrganization():Single<List<Organization>>

    fun becameMemberOfOrganization(member: Member, organization: Organization): Completable

    fun getActions():Single<List<Action>>

    fun getMembersOfOrganization(organization: Organization): Single<List<Member>>

    fun getRequestOfOrganization(organization: Organization): Single<List<Member>>

    fun getCommercialOffers(): Single<List<CommercialOffer>>
}
