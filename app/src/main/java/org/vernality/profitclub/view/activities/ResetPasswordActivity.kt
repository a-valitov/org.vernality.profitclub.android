package org.vernality.profitclub.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import org.vernality.profitclub.R
import org.vernality.profitclub.model.data.Member
import org.vernality.profitclub.model.data.Organization
import org.vernality.profitclub.model.data.Supplier
import org.vernality.profitclub.view.fragments.OnBackPressedListener


public class ResetPasswordActivity : AppCompatActivity() {

    var isBackPress: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
    }


}

