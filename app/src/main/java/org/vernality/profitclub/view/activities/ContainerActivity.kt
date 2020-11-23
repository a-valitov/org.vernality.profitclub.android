package org.vernality.profitclub.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.parse.ParseObject
import com.parse.ParseQuery
import org.koin.android.ext.android.inject
import org.koin.core.inject
import org.vernality.profitclub.R
import org.vernality.profitclub.model.data.Member
import org.vernality.profitclub.model.repository.RepositoryImplementation
import org.vernality.profitclub.utils.ui.RegistrationStatus
import org.vernality.profitclub.utils.ui.UIUtils
import org.vernality.profitclub.view_model.Role

class ContainerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        val repo: RepositoryImplementation by inject<RepositoryImplementation>()

//        repo.createMember(
//            Member().apply {
//                firstName = "Black"
//                lastName = "Jack"
//            }
//
//        ).subscribe(
//            { println("create member succes")},
//            {t -> println("error create member error = " + t)}
//        )

//        val query2 = ParseQuery.getQuery<ParseObject>("Organization")
//        query2.whereEqualTo("name", "ООО Куку")
//        query2.findInBackground { list, e -> kotlin.run{
//
//            if(e == null) {
//                println("----ООО Куку finded,  postlist: " + list)
//
//                val organization = list[0]
//
//                val member = Member().apply {
//                    firstName = "Ben"
//                    lastName = "Laden"
//                }

//                member.saveInBackground({e->
//                    if(e == null ){
//                        println("member save")
//
//                        val rel =organization.getRelation<ParseObject>("members")
//                        rel.query.findInBackground { objects, e ->
//                            if(e == null ){
//                                println("finded relation members")
//
//                                rel.add(member)
//                                organization.saveInBackground({e->
//                                    if(e == null )
//                                    {
//                                        println("member saved in organization")
//                                    }
//                                    else
//                                    {
//                                        println("Error, member NOT saved in organization, error = "+e)
//                                    }
//                                })
//
//                            }
//                        }
//
//
//                    }
//
//
//                })
//
//            }else{
//                println("------query error = " +e)
//            }
//
//        }}



        UIUtils.setRegistrationStatus(RegistrationStatus.First)
        checkStatus()
        finish()

    }

    private fun checkStatus() {
        val status = UIUtils.getRegistrationStatus()
        when(status){
            RegistrationStatus.First->startRegistrationActivity()
            RegistrationStatus.Registered->startSelectRoleActivity()
            RegistrationStatus.SelectedRole->startSelectOrganizationActivity()
        }
    }


    private fun startRegistrationActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun startSelectRoleActivity(){
        val intent = Intent(this, EnterRoleActivity::class.java)
        startActivity(intent)
    }

    private fun startSelectOrganizationActivity(){
        val intent = Intent(this, SelectOrganizationActivity::class.java)
        startActivity(intent)
    }
}