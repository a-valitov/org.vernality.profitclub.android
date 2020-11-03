package org.vernality.profitclub.model.repository

import org.vernality.profitclub.model.data.User

interface Repository<T> {

    fun getData(user: User): T

    fun getUser(password: String, email: String):User?
}
