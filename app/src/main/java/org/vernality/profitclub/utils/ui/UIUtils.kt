package org.vernality.profitclub.utils.ui

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.Navigation
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.vernality.profitclub.R
import org.vernality.profitclub.utils.ui.RegistrationStatus.*
import org.vernality.profitclub.view.fragments.SuccessResultDialogFragment
import org.vernality.profitclub.view.fragments.TypeDialogFragment


enum class RegistrationStatus{
    First, Registered, SelectedRole
}

class UIUtils {

    companion object: KoinComponent {

        public fun getRegistrationStatus(): RegistrationStatus {
            val pref: MyPreferences by inject<MyPreferences>()
            val value = pref.getStrPref(APP_PREF_REG_STATUS)
            var status = First
            when (value) {
                Registered.name -> status = Registered
                SelectedRole.name -> status = SelectedRole
            }
            return status

        }

        public fun setRegistrationStatus(status: RegistrationStatus) {
            val pref: MyPreferences by inject<MyPreferences>()
            pref.setStrPref(APP_PREF_REG_STATUS, status.name)
        }

        public fun checkGmail(gmail: String):Boolean{
            return android.util.Patterns.EMAIL_ADDRESS.matcher(gmail).matches()
        }

        public fun openWebPage(context: Context, address: String){
            val uri = Uri.parse(address)
            val openLinkIntent = Intent(Intent.ACTION_VIEW, uri).addFlags(FLAG_ACTIVITY_NEW_TASK)

            if (openLinkIntent.resolveActivity(context.packageManager) != null) {
                startActivity(context, openLinkIntent, Bundle.EMPTY)
            } else {
                Log.d("Intent", "Не получается обработать намерение!")
            }
        }


        public fun configureDialogFragment(fragment: SuccessResultDialogFragment, type: TypeDialogFragment): SuccessResultDialogFragment {

            when (type) {
                TypeDialogFragment.Registration -> return configureRegistrationDialog(fragment)
                TypeDialogFragment.RoleApproveAdmin -> return configureRoleApproveAdminDialog(fragment)
                TypeDialogFragment.LogOrgAccount -> return configureLogOrgAccountDialog(fragment)
                TypeDialogFragment.ResetPassword -> return configureResetPasswordDialog(fragment)
                TypeDialogFragment.ApproveAction -> return configureAcceptActionDialog(fragment)
                else -> throw Throwable("недопустимый тип диалогового фрагмента")

            }

        }




        private fun configureRegistrationDialog(fragment: SuccessResultDialogFragment): SuccessResultDialogFragment{
           return fragment.apply {
               setTitleOnViews(resources.getStringArray(R.array.Registration))
               backTV?.visibility = View.GONE
           }
        }

        private fun configureRoleApproveAdminDialog(fragment: SuccessResultDialogFragment): SuccessResultDialogFragment{
            return fragment.apply {
                setTitleOnViews(resources.getStringArray(R.array.RoleApproveAdmin))
                backTV?.visibility = View.GONE
            }
        }

        private fun configureLogOrgAccountDialog(fragment: SuccessResultDialogFragment): SuccessResultDialogFragment{
            return fragment.apply {
                setTitleOnViews(resources.getStringArray(R.array.LogOrgAccount))
            }
        }

        private fun configureResetPasswordDialog(fragment: SuccessResultDialogFragment): SuccessResultDialogFragment{
            return fragment.apply {
                setTitleOnViews(resources.getStringArray(R.array.ResetPassword))
            }
        }

        private fun configureAcceptActionDialog(fragment: SuccessResultDialogFragment): SuccessResultDialogFragment{
            return fragment.apply {
                setTitleOnViews(resources.getStringArray(R.array.AcceptAction))
            }
        }

    }
}