package org.vernality.profitclub.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.vernality.profitclub.R
import org.vernality.profitclub.utils.ui.RegistrationStatus
import org.vernality.profitclub.utils.ui.UIUtils

class ContainerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
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