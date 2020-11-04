package org.vernality.profitclub.model.datasource

import io.reactivex.Completable
import org.vernality.profitclub.model.data.Role
import org.vernality.profitclub.model.data.User

interface DataSource {

    fun getData(password: String, email:String):User?

    fun setData(user: User)

    fun registration(userName: String, password: String, email: String):Completable

    fun createOrganization(role: Role):Completable

    fun createSupplier(role: Role): Completable

    fun createMember(role: Role): Completable


}
