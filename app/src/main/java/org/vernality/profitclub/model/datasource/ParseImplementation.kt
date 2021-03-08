package org.vernality.profitclub.model.datasource


import com.parse.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.vernality.profitclub.model.data.*
import org.vernality.profitclub.utils.ui.*
import org.vernality.profitclub.utils.ui.ErrorsUtils.Companion.getErrorsMessage


class ParseImplementation() : DataSource {

    override fun getData(password: String, email: String): User? {

       var user: User? = null
        return user
    }

    override fun isUserLogged(): Boolean {
        return ParseUser.getCurrentUser() != null

    }

    override fun registration(_user: User):Completable {
        val user = ParseUser()
        user.username = _user.username
        user.setPassword(_user.password)
        user.email = _user.email

        return Completable.create {
            user.signUpInBackground { e ->
                if (e == null) {
                    val installation = ParseInstallation.getCurrentInstallation()
                    installation.put("user", user)
                    installation.saveEventually()
                    it.onComplete()
                } else {
                    it.onError(e)
                }
            }
        }

    }

    override fun logOut(): Completable {

        return Completable.create {

            try {
                ParseUser.logOut();
                if(null == ParseUser.getCurrentUser()) it.onComplete()
                else it.onError(Throwable(getErrorsMessage(ERROR_70)))
            }catch (e: ParseException) {
                it.onError(Throwable(getErrorsMessage(e.code).let { if(it.isEmpty()) e.message else it}))
            }

        }


    }

    override fun signIn(user: User): Completable {

        return Completable.create {

            ParseUser.logInInBackground(user.login, user.password) { user, e ->
                if (user != null) {
                    val installation = ParseInstallation.getCurrentInstallation()
                    installation.put("user", user)
                    installation.saveEventually()
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
            try {
                ParseUser.requestPasswordReset(email)
                it.onComplete()
            }catch (e: ParseException) {
                println("++++++ intersept error code = ${e.code}")
                it.onError(Throwable(getErrorsMessage(e.code).let { if(it.isEmpty()) e.message else it}))
            }

        }
    }


    override fun createOrganization(organization: Organization): Completable {

        return Completable.create {

            try {
                val currentUser = ParseUser.getCurrentUser()
                if (currentUser != null) {
                    organization.save()
                    val relation: ParseRelation<ParseObject> =
                        currentUser.getRelation(KEY_ORGANIZATIONS)
                    relation.add(organization)
                    currentUser.save()
                    it.onComplete()
                }else {
                    // Вызов окна входа
                    println("------необходимо войти в систему---")
                }

            }catch (e: ParseException) {
                println("++++++ intersept error code = ${e.code}")
                it.onError(Throwable(getErrorsMessage(e.code).let { if(it.isEmpty()) e.message else it}))
            }

        }
    }

    override fun createSupplier(supplier: Supplier): Completable {

        return Completable.create {

            try {
                val currentUser = ParseUser.getCurrentUser()
                if (currentUser != null) {
                    supplier.save()
                    val relation: ParseRelation<ParseObject> = currentUser.getRelation("suppliers")
                    relation.add(supplier)
                    currentUser.save()
                    it.onComplete()
                }else {
                    // Вызов окна входа
                    println("------необходимо войти в систему---")
                }

            }catch (e: ParseException) {
                println("++++++ intersept error code = ${e.code}")
                it.onError(Throwable(getErrorsMessage(e.code).let { if(it.isEmpty()) e.message else it}))
            }

        }
    }

    override fun createMember(member: Member): Completable {

        return Completable.create {

            try {
                val currentUser = ParseUser.getCurrentUser()
                if (currentUser != null) {
                    member.save()
                    val relation: ParseRelation<ParseObject> = currentUser.getRelation("member")
                    relation.add(member)
                    currentUser.save()
                    it.onComplete()
                }else {
                    // Вызов окна входа
                    println("------необходимо войти в систему---")
                }

            }catch (e: ParseException) {
                println("++++++ intersept error code = ${e.code}")
                it.onError(Throwable(getErrorsMessage(e.code).let { if(it.isEmpty()) e.message else it}))
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
                it.onError(Exception("Не удалось войти в систему"))
            }
        }
    }

    override fun getOrganization(): Observable<List<Organization>> {

        return Observable.create {

            try {
                val currentUser = ParseUser.getCurrentUser()
                if (currentUser != null) {
                    val query: ParseQuery<Organization> = ParseQuery.getQuery(Organization::class.java)
                    it.onNext(query.find())
                }else {
                    // Вызов окна входа
                    println("------необходимо войти в систему---")
                }

            }catch (e: ParseException) {
                println("++++++ intersept error code = ${e.code}")
                it.onError(Throwable(getErrorsMessage(e.code).let { if(it.isEmpty()) e.message else it}))
            }

        }
    }

    override fun getMyOrganizations():Observable<List<Organization>>{
        return Observable.create {

            try {
                val currentUser = ParseUser.getCurrentUser()
                if (currentUser != null) {
                    val relation: ParseRelation<Organization> = currentUser.getRelation(KEY_ORGANIZATIONS)
                    it.onNext(relation.query.find())
                }else {
                    // Вызов окна входа
                    println("------необходимо войти в систему---")
                }

            }catch (e: ParseException) {
                println("++++++ intersept error code = ${e.code}")
                it.onError(Throwable(getErrorsMessage(e.code).let { if(it.isEmpty()) e.message else it}))
            }

        }
    }

    override fun getMySuppliers(): Observable<List<Supplier>> {
        return Observable.create {

            try {
                val currentUser = ParseUser.getCurrentUser()
                if (currentUser != null) {
                    val relation: ParseRelation<Supplier> = currentUser.getRelation(KEY_SUPPLIERS)
                    it.onNext(relation.query.find())
                }else {
                    // Вызов окна входа
                    println("------необходимо войти в систему---")
                }

            }catch (e: ParseException) {
                println("++++++ intersept error code = ${e.code}")
                it.onError(Throwable(getErrorsMessage(e.code).let { if(it.isEmpty()) e.message else it}))
            }

        }
    }

    override fun getMyMembers(): Observable<List<Member>> {
        return Observable.create {

            try {
                val currentUser = ParseUser.getCurrentUser()
                if (currentUser != null) {
                    val relation: ParseRelation<Member> = currentUser.getRelation(KEY_MEMBER)
                    it.onNext(relation.query.find())
                }else {
                    // Вызов окна входа
                    println("------необходимо войти в систему---")
                }

            }catch (e: ParseException) {
                println("++++++ intersept error code = ${e.code}")
                it.onError(Throwable(getErrorsMessage(e.code).let { if(it.isEmpty()) e.message else it}))
            }

        }
    }

    override fun getOrganizationsForMyMembers(): Observable<List<Organization>>{
        return Observable.create {

            val currentUser = ParseUser.getCurrentUser()
            if (currentUser != null) {

                val user = ParseUser.getCurrentUser()
                val relation = user.getRelation<Member>(KEY_MEMBER)
                relation.query.findInBackground{ results, e ->
                    if (e != null) {
                        // There was an error
                        it.onError(e)
                    } else {
                        // results have all the Posts the current user liked.
                        if(!results.isEmpty()){
                            val query =
                                ParseQuery.getQuery<Organization>(KEY_ORGANIZATION)
                            query.whereEqualTo(KEY_MEMBERS, results[0])

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

            try {
                val currentUser = ParseUser.getCurrentUser()
                if (currentUser != null) {

                    member.save()
                    val relation: ParseRelation<ParseObject> = currentUser.getRelation(KEY_MEMBER)
                    relation.add(member)
                    currentUser.save()

                    val relation2: ParseRelation<ParseObject> = organization.getRelation(KEY_MEMBERS)
                    relation2.add(member)
                    val params = HashMap<String, String>()
                    params["organizationId"] = organization.objectId
                    params["memberId"] = member.objectId

                    ParseCloud.callFunction<Any?>("applyAsAMemberToOrganization", params)


                    it.onComplete()

                }else {
                    // Вызов окна входа
                    println("------необходимо войти в систему---")
                }

            }catch (e: ParseException) {
                println("++++++ intersept error code = ${e.code}")
                it.onError(Throwable(getErrorsMessage(e.code).let { if(it.isEmpty()) e.message else it}))
            }

        }
    }


    override fun getActions(): Single<List<Action>> {

        return Single.create {

            try {
                val currentUser = ParseUser.getCurrentUser()
                if (currentUser != null) {

                    val query: ParseQuery<Action> = ParseQuery.getQuery(Action::class.java)
                    val actions = query.find().apply { forEach { action ->

                        action.supplier?.fetchIfNeeded<Supplier>()

                    } }

                    it.onSuccess( actions )

                }else {
                    // Вызов окна входа
                    println("------необходимо войти в систему---")
                }

            }catch (e: ParseException) {
                println("++++++ intersept error code = ${e.code}")
                it.onError(Throwable(getErrorsMessage(e.code).let { if(it.isEmpty()) e.message else it}))
            }

        }
    }

    override fun getMembersOfOrganization(organization: Organization): Single<List<Member>> {
        return Single.create {

            try {
                val currentUser = ParseUser.getCurrentUser()
                if (currentUser != null) {

                    val relation = organization.getRelation<Member>(KEY_MEMBERS)
                    val query: ParseQuery<Member> = relation.getQuery()
                    query.whereEqualTo(KEY_APPROVED, KEY_STATUS_STRING)
                    it.onSuccess( query.find() )

                }else {
                    // Вызов окна входа
                    println("------необходимо войти в систему---")
                }

            }catch (e: ParseException) {
                println("++++++ intersept error code = ${e.code}")
                it.onError(Throwable(getErrorsMessage(e.code).let { if(it.isEmpty()) e.message else it}))
            }

        }
    }

    override fun getRequestOfOrganization(organization: Organization): Single<List<Member>> {
        return Single.create {

            try {
                val currentUser = ParseUser.getCurrentUser()
                if (currentUser != null) {

                    val relation = organization.getRelation<Member>(KEY_MEMBERS)
                    val query: ParseQuery<Member> = relation.getQuery()
                    query.whereEqualTo(KEY_ON_REVIEW, KEY_STATUS_STRING)
                    it.onSuccess( query.find() )

                }else {
                    // Вызов окна входа
                    println("------необходимо войти в систему---")
                }

            }catch (e: ParseException) {
                println("++++++ intersept error code = ${e.code}")
                it.onError(Throwable(getErrorsMessage(e.code).let { if(it.isEmpty()) e.message else it}))
            }

        }
    }

    override fun getCommercialOffers(): Single<List<CommercialOffer>> {
        return Single.create {

            try {
                val currentUser = ParseUser.getCurrentUser()
                if (currentUser != null)
                {
                    val query: ParseQuery<CommercialOffer> = ParseQuery.getQuery(CommercialOffer::class.java)
                    it.onSuccess(query.find())

                } else {
                    // Вызов окна входа
                }

            }catch (e: ParseException) {
                println("++++++ intersept error code = ${e.code}")
                it.onError(Throwable(getErrorsMessage(e.code).let { if(it.isEmpty()) e.message else it}))
            }

        }
    }

    override fun createAction(action: Action, supplier: Supplier): Completable {
        return Completable.create {

            try {

                val currentUser = ParseUser.getCurrentUser()
                if (currentUser != null) {
                    action.put(KEY_SUPPLIER, supplier)
                    action.getRelation<ParseUser>(KEY_USER).add(currentUser)
                    action.put(KEY_STATUS_STRING, KEY_APPROVED)
                    action.start?.let { action.put(KEY_START_DATE, it) }
                    action.end?.let { action.put(KEY_END_DATE, it) }

                    val acl = ParseACL()
                    acl.publicReadAccess = true
                    acl.setRoleWriteAccess(KEY_ADMINISTRATOR, true)

                    action.acl = acl

                    val parseFile: ParseFile = ParseFile(KEY_IMAGE_PNG,action.image)
                    parseFile.save()

                    action.put(KEY_IMAGE_FILE, parseFile)

                    action.save()

                    it.onComplete()


                } else {
                    // Вызов окна входа
                }

            }catch (e: ParseException) {
                println("++++++ intersept error code = ${e.code}")
                it.onError(Throwable(getErrorsMessage(e.code).let { if(it.isEmpty()) e.message else it}))
            }

        }
    }

    override fun createOffer(offer: CommercialOffer, supplier: Supplier): Completable {
        return Completable.create {

            try {

                val currentUser = ParseUser.getCurrentUser()
                if (currentUser != null) {
                    offer.put(KEY_SUPPLIER, supplier)
                    offer.getRelation<ParseUser>(KEY_USER).add(currentUser)
                    offer.put(KEY_STATUS_STRING, KEY_APPROVED)

                    val acl = ParseACL()
                    acl.publicReadAccess = true
                    acl.setRoleWriteAccess(KEY_ADMINISTRATOR, true)
                    offer.acl = acl

                    val parseFile: ParseFile = ParseFile(KEY_IMAGE_PNG, offer.image)
                    parseFile.save()

                    offer.put(KEY_IMAGE_FILE, parseFile)

                    val parseFileList = mutableListOf<ParseFile>()


                    offer.listOfDocs.forEach {

                        offer.add(KEY_ATTACHMENT_NAMES, it.key)
                        val parseFileDoc: ParseFile = ParseFile(it.key, it.value)
                        parseFileDoc.save()
                        parseFileList.add(parseFileDoc)
                    }

                    offer.addAll(KEY_ATTACHMENT_FILES, parseFileList)

                    offer.save()

                    it.onComplete()


                } else {
                    // Вызов окна входа

                }

            }catch (e: ParseException) {
                println("++++++ intersept error code = ${e.code}")
                it.onError(Throwable(getErrorsMessage(e.code).let { if(it.isEmpty()) e.message else it}))
            }
        }
    }



    companion object {
        private const val BASE_URL_LOCATIONS = ""
    }
}
