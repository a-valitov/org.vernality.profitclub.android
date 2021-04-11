package org.vernality.profitclub.view.activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.alexandr.utils.network.OnlineLiveData
import com.example.alexandr.utils.network.isOnline
import com.parse.Parse
import com.parse.ParseObject
import com.parse.ParseQuery
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.inject
import org.vernality.profitclub.R
import org.vernality.profitclub.interactors.MainInteractor
import org.vernality.profitclub.model.repository.RepositoryImplementation
import org.vernality.profitclub.utils.ui.ActionBottomDialogFragment
import org.vernality.profitclub.utils.ui.MyPreferences
import org.vernality.profitclub.utils.ui.RegistrationStatus
import org.vernality.profitclub.utils.ui.UIUtils
import org.vernality.profitclub.view.fragments.ErrorResultDialogFragment
import org.vernality.profitclub.view.fragments.OnBackPressedListener


abstract class OnBackPresserActivity : BaseActivity(), OnBackPresser{

    var isBackPress: Boolean = false

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