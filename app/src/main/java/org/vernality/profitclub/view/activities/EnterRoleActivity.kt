package org.vernality.profitclub.view.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.parse.ParseObject
import com.parse.ParseQuery
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.vernality.profitclub.R
import org.vernality.profitclub.model.data.Role
import org.vernality.profitclub.model.datasource.ParseImplementation
import org.vernality.profitclub.view_model.EnterRoleActivityViewModel
import org.vernality.profitclub.view_model.EnterRoleDataViewModel


public class EnterRoleActivity : AppCompatActivity() {

    val viewModelRoleActivity by viewModel<EnterRoleActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_role)
    }

}