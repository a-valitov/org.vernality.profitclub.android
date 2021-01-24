package org.vernality.profitclub.view.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import org.vernality.profitclub.R
import org.vernality.profitclub.view.fragments.OnBackPressedListener


class SelectOrganizationActivity : AppCompatActivity(), OnBackPresser {

    lateinit var view: View

    var isBackPress: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_organization)

        view = findViewById(R.id.layout_container)

    }





    override fun onBackPressed() {

        if(!isBackPress){
            isBackPress = true
            val fm: FragmentManager = supportFragmentManager
            var backPressedListener: OnBackPressedListener? = null
            for (fragment in fm.fragments) {
                if(fragment is NavHostFragment){
                    val childFm = fragment.childFragmentManager
                    for(childFragment in childFm.fragments){
                        if(childFragment is OnBackPressedListener){
                            backPressedListener = childFragment
                        }
                    }
                }

            }

            if (backPressedListener != null) {
                backPressedListener.onBackPressed(this)
            } else {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }



    }

    override fun setIsBackPress() {
        isBackPress = false
    }
}