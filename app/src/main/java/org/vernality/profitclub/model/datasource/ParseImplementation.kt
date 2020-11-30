package org.vernality.profitclub.model.datasource


import com.parse.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.vernality.profitclub.model.data.*


class ParseImplementation() : DataSource {

    override fun getData(password: String, email: String): User? {

       var user: User? = null
        return user
    }


    override fun registration(_user: User):Completable {
        val user = ParseUser()
        user.username = _user.username
        user.setPassword(_user.password)
        user.email = _user.email

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

    override fun logOut(): Completable {

        return Completable.create {

            ParseUser.logOut();
            if(null == ParseUser.getCurrentUser()) it.onComplete()
            else it.onError(Throwable("Не удалось выйти из профиля"))
        }


    }

    override fun signIn(user: User): Completable {

        return Completable.create {

            ParseUser.logInInBackground(user.login, user.password) { user, e ->
                if (user != null) {
                    println("вход в аккаунт прошел успешно")
                    it.onComplete()
                } else {
                    println("ошибка входа в аккаунт  "+e)
                    it.onError(e)
                }
            }

        }
    }

    override fun resetPassword(email: String): Completable {
        return Completable.create {

            ParseUser.requestPasswordResetInBackground(email) { e ->
                if (e == null) {
                    println("инструкции по сбросу пароля отправлены на указанную почту")
                    it.onComplete()
                } else {
                    println("ошибка сброса пароля  "+e)
                    it.onError(e)
                }
            }

        }
    }


    override fun createOrganization(organization: Organization): Completable {

        return Completable.create {
            val currentUser = ParseUser.getCurrentUser()
            if (currentUser != null) {
                organization.saveInBackground{ e->
                    if(e == null)
                    {
                        val relation: ParseRelation<ParseObject> = currentUser.getRelation("organizations")
                        relation.add(organization)
                        currentUser.saveInBackground{ e->
                            if(e == null)
                            {
                                it.onComplete()
                            }
                            else
                            {   it.onError(e)
                            }
                        }

                    }
                    else it.onError(e)
                }
            } else {
                // Вызов окна входа
                println("------необходимо войти в систему---")
            }
        }
    }

    override fun createSupplier(supplier: Supplier): Completable {

        return Completable.create {
            val currentUser = ParseUser.getCurrentUser()
            if (currentUser != null) {
                println("------currentUser is not null-----")

                supplier.saveInBackground{ e->
                    if(e == null)
                    {
                        val relation: ParseRelation<ParseObject> = currentUser.getRelation("suppliers")
                        relation.add(supplier)
                        currentUser.saveInBackground{ e->
                            if(e == null)
                            {
                                it.onComplete()
                            }
                            else
                            {   it.onError(e)
                            }
                        }

                    }
                    else it.onError(e)
                }

            } else {
                // Вызов окна входа
                println("------необходимо войти в систему---")
            }
        }
    }

    override fun createMember(member: Member): Completable {

        return Completable.create {
            val currentUser = ParseUser.getCurrentUser()
            if (currentUser != null) {
                println("------currentUser is not null-----")

//                member.put("member", member)
                member.saveInBackground{ e->
                    if(e == null)
                    {
                        val relation: ParseRelation<ParseObject> = currentUser.getRelation("member")
                        relation.add(member)
                        currentUser.saveInBackground{ e->
                            if(e == null)
                            {
                                it.onComplete()
                            }
                            else
                            { it.onError(e)
                            }
                        }

                    }
                    else it.onError(e)
                    }

            }
            else
            {
                // Вызов окна входа
                println("------необходимо войти в систему---")
            }
        }
    }

    override fun getUserName(): Single<String> {
        return Single.create {
            val currentUser = ParseUser.getCurrentUser()
            if (currentUser != null) {
                println("------currentUser is not null-----")

                val userName = currentUser.username
                it.onSuccess(userName)
            } else {
                // Вызов окна входа
                println("------необходимо войти в систему---")
                it.onError(Exception("Не удалось войти в систему"))
            }
        }
    }

    override fun getOrganization(): Observable<List<Organization>> {


        return Observable.create {
            val currentUser = ParseUser.getCurrentUser()
            if (currentUser != null) {
                println("------currentUser is not null-----")

                val query: ParseQuery<Organization> = ParseQuery.getQuery(Organization::class.java)
                query.findInBackground { list, e ->
                    if(e == null){
                        println("----OK get Organization-----")
                        list.forEach {
                            val objectId: String = it.getObjectId()
                            println("object =${it.name}")

                        }
                        it.onNext(list)
                    } else {
                        println("----Error get Organization-----")
                        it.onError(e)
                    }
                }

            } else {
                // Вызов окна входа
                println("------необходимо войти в систему---")
                it.onError(Exception("Не удалось войти в систему"))
            }
        }
    }

    override fun getMyOrganizations():Observable<List<Organization>>{
        return Observable.create {
            val currentUser = ParseUser.getCurrentUser()
            if (currentUser != null) {
                println("------currentUser is not null-----")

                val relation: ParseRelation<Organization> = currentUser.getRelation("organizations")
                relation.query.findInBackground { results, e ->
                    if (e != null) {
                        it.onError(e)
                    } else {
                        it.onNext(results)
                    }
                }

            }
        }
    }

    override fun getMySuppliers(): Observable<List<Supplier>> {
        return Observable.create {
            val currentUser = ParseUser.getCurrentUser()
            if (currentUser != null) {
                println("------currentUser is not null-----")

                val relation: ParseRelation<Supplier> = currentUser.getRelation("suppliers")
                relation.query.findInBackground { results, e ->
                    if (e != null) {
                        it.onError(e)
                    } else {
                        it.onNext(results)
                    }
                }

            }
        }
    }

    override fun getMyMembers(): Observable<List<Member>> {
        return Observable.create {
            val currentUser = ParseUser.getCurrentUser()
            if (currentUser != null) {
                println("------currentUser is not null-----")

                val relation: ParseRelation<Member> = currentUser.getRelation("member")
                relation.query.findInBackground { results, e ->
                    if (e != null) {
                        it.onError(e)
                    } else {
                        it.onNext(results)
                    }
                }

            }
        }
    }

    override fun getOrganizationsForMyMembers(): Observable<List<Organization>>{
        return Observable.create {
            val currentUser = ParseUser.getCurrentUser()
            if (currentUser != null) {
                println("------currentUser is not null-----")

                val user = ParseUser.getCurrentUser()
                val relation = user.getRelation<Member>("member")
                relation.query.findInBackground{ results, e ->
                    if (e != null) {
                        // There was an error
                        it.onError(e)
                    } else {
                        // results have all the Posts the current user liked.
                        if(!results.isEmpty()){
                            val query =
                                ParseQuery.getQuery<Organization>("Organization")
                            query.whereEqualTo("members", results[0])

                            query.findInBackground { organizationsList, e ->
                                // commentList now has the comments for myPost
                                if(e == null){
                                    it.onNext(organizationsList)
                                }else{
                                    it.onError(e)
                                }
                            }
                        }else{
                            it.onNext(mutableListOf<Organization>())
                        }

                    }
                }



            }
        }
    }


    override fun becameMemberOfOrganization(member: Member, organization: Organization): Completable {

        return Completable.create {
            val currentUser = ParseUser.getCurrentUser()
            if (currentUser != null) {
                println("------currentUser is not null-----")
                println("------member= " +member.firstName)
                println("------organization= " +organization.name)

                member.saveInBackground{ e->
                    if(e == null)
                    {
                        val relation: ParseRelation<ParseObject> = currentUser.getRelation("member")
                        relation.add(member)
                        currentUser.saveInBackground{ e->
                            if(e == null)
                            {

                            }
                            else
                            { it.onError(e)
                            }
                        }

                        Thread.sleep(1000)

                        val relation2: ParseRelation<ParseObject> = organization.getRelation("members")
                        relation2.add(member)
                        println("organization = "+organization.name)
                        organization.saveInBackground{ e->
                            if(e == null)
                            {
                                it.onComplete()
                            }
                            else
                            { it.onError(e)
                                println(e)
                            }
                        }



                    }
                    else it.onError(e)
                }


            } else {
                // Вызов окна входа
                println("------необходимо войти в систему---")
            }
        }
    }


    override fun getActions(): Single<List<Action>> {

        return Single.create {

            val currentUser = ParseUser.getCurrentUser()
            if (currentUser != null) {

                val query: ParseQuery<Action> = ParseQuery.getQuery(Action::class.java)
                query.findInBackground { list, e ->
                    if (e == null) {

                        it.onSuccess( list )
                        list.forEach {

                            val objectId: String = it.getObjectId()
                            println("++++++action object =${it.message}")
                            println("++++++action object =${it.startDate}")

                        }

                    } else {

                    }
                }

            } else {
                // Вызов окна входа
                println("------необходимо войти в систему---")

            }
        }
    }

    override fun getMembersOfOrganization(organization: Organization): Single<List<Member>> {
        return Single.create {

            val currentUser = ParseUser.getCurrentUser()
            if (currentUser == null) {
                println("------currentUser is not null-----")

                val relation = organization.getRelation<Member>("members")

                val query: ParseQuery<Member> = relation.getQuery()
                query.whereEqualTo("approved", "statusString")
                query.findInBackground { list, e ->
                    if (e == null) {

                        it.onSuccess( list )
                        println("----OK get Organization-----")
                        println("++++++list size =${list.size}")
                        list.forEach {

                            val objectId: String = it.getObjectId()
                            println("++++++object =${it.firstName + it.lastName}")

                        }

                    } else {
                        println("----Error get Organization-----")

                    }
                }

            } else {
                // Вызов окна входа
                println("------необходимо войти в систему---")

            }
        }
    }

    override fun getRequestOfOrganization(organization: Organization): Single<List<Member>> {
        return Single.create {

            val currentUser = ParseUser.getCurrentUser()
            if (currentUser == null) {
                println("------currentUser is not null-----")

                val relation = organization.getRelation<Member>("members")

                val query: ParseQuery<Member> = relation.getQuery()
                query.whereEqualTo("onReview", "statusString")
                query.findInBackground { list, e ->
                    if (e == null) {

                        it.onSuccess( list )
                        println("----OK get Organization-----")
                        println("++++++list size =${list.size}")
                        list.forEach {

                            val objectId: String = it.getObjectId()
                            println("++++++object =${it.firstName + it.lastName}")

                        }

                    } else {
                        println("----Error get Organization-----")

                    }
                }

            } else {
                // Вызов окна входа
                println("------необходимо войти в систему---")

            }
        }
    }

    override fun getCommercialOffers(): Single<List<CommercialOffer>> {
        return Single.create {

            println("into getCommercialOffers()")
            val currentUser = ParseUser.getCurrentUser()
            if (currentUser != null)
            {
               println("currentUser is not null into getCommercialOffers()")


                val query: ParseQuery<CommercialOffer> = ParseQuery.getQuery(CommercialOffer::class.java)
                val list = query.find()

                it.onSuccess(list)

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
