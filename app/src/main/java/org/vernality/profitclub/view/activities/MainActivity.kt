package org.vernality.profitclub.view.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.vernality.profitclub.R
import org.vernality.profitclub.interactors.MainInteractor
import org.vernality.profitclub.utils.ui.ActionBottomDialogFragment
import org.vernality.profitclub.utils.ui.MyPreferences


class MainActivity : OnBackPresserActivity(), ActionBottomDialogFragment.ItemClickListener, OnBackPresser{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pref = get<MyPreferences> ()

        val interactor: MainInteractor by inject()

        if(interactor.isUserLogged()){
            val intent = Intent(this, SelectOrganizationActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onItemClick(item: String?) {
        Toast.makeText(this, "Selected action item is " + item, Toast.LENGTH_LONG).show()
    }


}