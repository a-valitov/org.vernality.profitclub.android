package org.vernality.profitclub.model.repository

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.vernality.profitclub.model.data.*
import org.vernality.profitclub.model.datasource.DataSource
import org.vernality.profitclub.model.datasource.ParseImplementation
import java.util.concurrent.TimeUnit


class RepositoryImplementation(private val dataSource: DataSource) :
    Repository{


    override fun getUser(password: String, email: String): User? {
        return ParseImplementation().getData(password, email)
    }

    override fun registration(user: User) = dataSource.registration(user)

    override fun signIn(user: User) = dataSource.signIn(user)

    override fun logOut()= dataSource.logOut()

    override fun resetPassword(email: String): Completable = dataSource.resetPassword(email)

    override fun createOrganization(organization: Organization) = dataSource.createOrganization(organization)

    override fun createSupplier(supplier: Supplier) = dataSource.createSupplier(supplier)

    override fun createMember(member: Member) = dataSource.createMember(member)

    override fun getUserName() = dataSource.getUserName()

    override fun getOrganization() = dataSource.getOrganization()


    override fun getMyOrganizations() = dataSource.getMyOrganizations()

    override fun getMySuppliers() = dataSource.getMySuppliers()

    override fun getMyMembers() = dataSource.getMyMembers()

    override fun getOrganizationsForMyMembers() = dataSource.getOrganizationsForMyMembers()

    override fun becameMemberOfOrganization(member: Member, organization: Organization)
            = dataSource.becameMemberOfOrganization(member, organization)


    override fun getActions(): Single<List<Action>> = dataSource.getActions()

    override fun getMembersOfOrganization(organization: Organization): Single<List<Member>> =
        dataSource.getMembersOfOrganization(organization)

    override fun getRequestOfOrganization(organization: Organization): Single<List<Member>> =
        dataSource.getRequestOfOrganization(organization)

    override fun getCommercialOffers(): Single<List<CommercialOffer>> =
        dataSource.getCommercialOffers()
}
