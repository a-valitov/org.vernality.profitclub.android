package org.vernality.profitclub.utils.ui

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.setMargins
import com.google.android.material.snackbar.Snackbar
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.inject
import org.vernality.profitclub.R
import org.vernality.profitclub.model.data.OrganizationStatus
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
                TypeDialogFragment.ApproveDelivery -> return configureApproveDeliveryDialog(fragment)
                TypeDialogFragment.RequestApprovedAdmin -> return configureRequestApprovedAdminDialog(fragment)
                TypeDialogFragment.SubmitAction -> return configureSubmitActionDialog(fragment)
                TypeDialogFragment.WaitAdministratorApproval -> return configureWaitApprovalDialog(fragment)
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

        private fun configureApproveDeliveryDialog(fragment: SuccessResultDialogFragment): SuccessResultDialogFragment{
            return fragment.apply {
                setTitleOnViews(resources.getStringArray(R.array.ApproveDelivery))
            }
        }

        private fun configureRequestApprovedAdminDialog(fragment: SuccessResultDialogFragment): SuccessResultDialogFragment{
            return fragment.apply {
                setTitleOnViews(resources.getStringArray(R.array.RequestApprovedAdmin))
                backTV?.visibility = View.GONE
            }
        }

        private fun configureSubmitActionDialog(fragment: SuccessResultDialogFragment): SuccessResultDialogFragment{
            return fragment.apply {
                setTitleOnViews(resources.getStringArray(R.array.SubmitAction))
            }
        }

        private fun configureWaitApprovalDialog(fragment: SuccessResultDialogFragment): SuccessResultDialogFragment{
            return fragment.apply {
                setTitleOnViews(resources.getStringArray(R.array.WaitAdministratorApproval))
                backTV?.visibility = View.GONE
            }
        }

        fun paintStatusText(textView: TextView, status: String?){
            when(status){
                OrganizationStatus.onReview.name -> {
                    textView.setText(status)
                textView.setTextColor(textView.context.getColor(R.color.colorStatusOnReview))}
                OrganizationStatus.rejected.name, OrganizationStatus.excluded.name -> {
                    textView.setText(status)
                    textView.setTextColor(textView.context.getColor(R.color.colorStatusRejected))}
                OrganizationStatus.approved.name -> textView.visibility = View.INVISIBLE
                else -> textView.visibility = View.INVISIBLE

            }
        }

        fun wasApprovalDialogShown(id: String): Boolean{
            val pref = get<MyPreferences> ()
            val objIdSet = pref.getStringSet(WAS_ADMIN_APPROVAL_SHOWN)
            return if(objIdSet != null && objIdSet.isNotEmpty()){
                objIdSet.contains(id)
            } else false
        }

        fun saveApprovalDialogShownEvent(id: String){

            val pref = get<MyPreferences> ()
            val objIdSet = pref.getStringSet(WAS_ADMIN_APPROVAL_SHOWN)

            var objIdSetTemp = objIdSet
            if(objIdSetTemp != null && objIdSetTemp.size > 0){
                if(!objIdSetTemp.contains(id)) {
                    pref.setStringSet(WAS_ADMIN_APPROVAL_SHOWN, objIdSetTemp.plus(id))

                } else {}
            } else {
                val objIdSetTemp = HashSet<String>()
                objIdSetTemp.add(id)
                pref.setStringSet(WAS_ADMIN_APPROVAL_SHOWN, objIdSetTemp)

            }

        }


        fun showSnackbar(layoutPlaceSnack: View, text: String, context: Context, func: ()->Unit ): Snackbar {
            val snackbar =
                Snackbar.make(layoutPlaceSnack,"", Snackbar.LENGTH_SHORT)

            snackbar.addCallback(object : Snackbar.Callback(){
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    func()
                }
            })
            snackbar.setText(text)
            var view = snackbar.view
            val tv =
                view.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
            tv.textAlignment = View.TEXT_ALIGNMENT_CENTER
            snackbar.view.setBackground(context.resources.getDrawable(R.drawable.card_info_lite))
            val params = view.layoutParams as CoordinatorLayout.LayoutParams
            params.gravity = Gravity.TOP
            params.setMargins(800)

            return snackbar
        }

    }
}