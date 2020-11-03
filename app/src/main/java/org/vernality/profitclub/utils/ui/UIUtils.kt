package org.vernality.profitclub.utils.ui

import org.koin.core.KoinComponent
import org.koin.core.inject
import org.vernality.profitclub.utils.ui.RegistrationStatus.*


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

    }
}