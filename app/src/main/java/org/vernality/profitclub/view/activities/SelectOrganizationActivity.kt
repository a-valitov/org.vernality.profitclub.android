package org.vernality.profitclub.view.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import org.vernality.profitclub.R
import org.vernality.profitclub.view.fragments.OnBackPressedListener


class SelectOrganizationActivity : OnBackPresserActivity(), OnBackPresser {

    lateinit var view: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_organization)

        view = findViewById(R.id.layout_container)

    }

}