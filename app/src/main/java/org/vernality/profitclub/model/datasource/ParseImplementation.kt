package org.vernality.profitclub.model.datasource


import android.content.Context
import com.parse.Parse
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import io.reactivex.Completable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.vernality.profitclub.model.data.User
import java.lang.Exception


class ParseImplementation() : DataSource {

    override fun getData(password: String, email: String): User? {

       var user: User? = null
//       val query = ParseQuery.getQuery<ParseObject>("User")
//        query.whereEqualTo("username", email)
//        query.findInBackground { _user, e ->
//            if(e == null){
//                println("--------объект  ="+ _user)
//                val obj = _user[0]
//
//                user = User(
//                    obj.getString("objectId")!!,
//                    obj.getString("username")!!,
//                    obj.getString("email")!!,
//                    obj.getString("password")!!
//                )
//
//            } else{
//                println("--------объект с именем ="+ email + " не найден")
//            }
//        }

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

    companion object {
        private const val BASE_URL_LOCATIONS = ""
    }
}
