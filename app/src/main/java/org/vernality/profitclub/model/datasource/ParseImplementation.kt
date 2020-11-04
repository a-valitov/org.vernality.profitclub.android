package org.vernality.profitclub.model.datasource


import com.parse.ParseObject
import com.parse.ParseUser
import io.reactivex.Completable
import org.vernality.profitclub.model.data.Role
import org.vernality.profitclub.model.data.User


class ParseImplementation() : DataSource {

    override fun getData(password: String, email: String): User? {

       var user: User? = null
        return user
    }

    override fun setData(_user: User) {
        val user = ParseObject("User")
        user.put("objectId", _user.hashCode())
        user.put("username", _user.login)
        user.put("password", _user.password)
        user.put("email", _user.email)
        user.saveInBackground()
    }

    override fun registration(userName: String, password: String, email: String):Completable {
        val user = ParseUser()
        user.username = userName
        user.setPassword(password)
        user.email = email

        return Completable.create {
            user.signUpInBackground { e ->
                if (e == null) {
                    // Hooray! Let them use the app now.
                    println("регистрация прошла успешно")
                    it.onComplete()
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    println("ошибка регистрации  "+e)
                    it.onError(e)
                }
            }
        }

    }


    override fun createOrganization(role: Role): Completable {

        return Completable.create {
            val currentUser = ParseUser.getCurrentUser()
            if (currentUser != null) {
                println("------currentUser is not null-----name = " + currentUser.username)
                println("------Role name =  "+ role.name)

                val organization = ParseObject("Organization")
                organization.put("name", role.name)
                organization.put("inn", role.INN)
                organization.put("contact", role.contactName)
                organization.put("phone", role.phone)


                currentUser.put("organization", organization)
                currentUser.saveInBackground { e-> if(e == null) it.onComplete()
                else it.onError(e)
                }
            } else {
                // Вызов окна входа
                println("------необходимо войти в систему---")
            }
        }
    }

    override fun createSupplier(role: Role): Completable {

        return Completable.create {
            val currentUser = ParseUser.getCurrentUser()
            if (currentUser != null) {
                println("------currentUser is not null-----")

                val supplier = ParseObject("Supplier")
                supplier.put("name", role.name)
                supplier.put("inn", role.INN)
                supplier.put("contact", role.contactName)
                supplier.put("phone", role.phone)

                currentUser.put("suppliers", supplier)
                currentUser.saveInBackground { e-> if(e == null) it.onComplete()
                else it.onError(e)
                }
            } else {
                // Вызов окна входа
                println("------необходимо войти в систему---")
            }
        }
    }

    override fun createMember(role: Role): Completable {

        return Completable.create {
            val currentUser = ParseUser.getCurrentUser()
            if (currentUser != null) {
                println("------currentUser is not null-----")

//                val member = ParseObject("Member")
//                member.put("name", role.name)
//                member.put("inn", role.INN)
//                member.put("contact", role.contactName)
//                member.put("phone", role.phone)
//
//                currentUser.put("member", member)
//                currentUser.saveInBackground { e-> if(e == null) it.onComplete()
//                else it.onError(e)
//                }
            } else {
                // Вызов окна входа
                println("------необходимо войти в систему---")
            }
        }
    }


    companion object {
        private const val BASE_URL_LOCATIONS = ""
    }
}
