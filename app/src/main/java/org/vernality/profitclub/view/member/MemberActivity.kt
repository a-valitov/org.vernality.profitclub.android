package org.vernality.profitclub.view.member

import android.content.Intent
import android.icu.text.TimeZoneFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.setMargins
import com.google.android.material.snackbar.Snackbar
import com.parse.ParseObject
import org.koin.android.ext.android.get
import org.vernality.profitclub.R
import org.vernality.profitclub.utils.ui.MyPreferences
import org.vernality.profitclub.utils.ui.UIUtils
import org.vernality.profitclub.utils.ui.WAS_ADMIN_APPROVAL_SHOWN
import org.vernality.profitclub.view.activities.SelectOrganizationActivity
import org.vernality.profitclub.view.fragments.SuccessResultDialogFragment
import org.vernality.profitclub.view.fragments.TypeDialogFragment
import java.lang.reflect.Field
import java.lang.reflect.Member

const val MEMBER = "member"

class MemberActivity : AppCompatActivity() {

    lateinit var settingsIV: ImageView
    lateinit var popupMenu:PopupMenu
    private lateinit var successResultDialog: SuccessResultDialogFragment
    var member: ParseObject? = null
    lateinit var pref: MyPreferences
    var objIdSet: Set<String>? = null

    lateinit var layoutPlaceSnack: CoordinatorLayout
    var isBackPress: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member)

        val toolbar: Toolbar = findViewById(R.id.myToolbar)
        setSupportActionBar(toolbar)
        initViews()

        val arguments = intent.extras
        if(arguments != null) {
            member = arguments.getParcelable<ParseObject>(MEMBER)
        }

        member?.let {
           if(UIUtils.wasApprovalDialogShown(it.objectId)) showSuccessDialog()
        }


    }

    fun initViews(){

        settingsIV = findViewById(R.id.iv_more)

        initPopupMenu(settingsIV)

        layoutPlaceSnack = findViewById(R.id.place_snack)

        settingsIV.setOnClickListener {

            val menuHelper: Any
            val argTypes: Array<Class<*>?>
            try {
                val fMenuHelper: Field =
                    PopupMenu::class.java.getDeclaredField("mPopup")
                fMenuHelper.setAccessible(true)
                menuHelper = fMenuHelper.get(popupMenu)
                argTypes = arrayOf(Boolean::class.javaPrimitiveType)
                menuHelper.javaClass.getDeclaredMethod("setForceShowIcon", *argTypes)
                    .invoke(menuHelper, true)
                menuHelper.javaClass.getDeclaredMethod("setLayoutGravity", *argTypes)
                    .invoke(menuHelper, Gravity.END)
            } catch (e: Exception) {
            }
            popupMenu.show()
        }
    }

    private fun initPopupMenu(view: View){
        val wrapper = ContextThemeWrapper(this, R.style.AppTheme_PopupOverlay)
        popupMenu = PopupMenu(wrapper, view)
        popupMenu.inflate(R.menu.main_toolbar_menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_menu_exit -> {
                    Toast.makeText(this, "exit clicked", Toast.LENGTH_LONG).show()
                    navigateToMyOrganizationList()

                    true
                }
                else -> false
            }
        }

    }


    private fun navigateToMyOrganizationList(){
        val intent = Intent(this, SelectOrganizationActivity::class.java)
        startActivity(intent)

    }

    private fun showSuccessDialog(){
        successResultDialog =
            SuccessResultDialogFragment.newInstance(
                TypeDialogFragment.RequestApprovedAdmin
            ) {
                member?.let{
                    UIUtils.saveApprovalDialogShownEvent(it.objectId)
                }
            }

        successResultDialog.show(supportFragmentManager, this.toString())

    }

    override fun onBackPressed() {

        if(!isBackPress){
            isBackPress = true
            val snackbar = UIUtils.showSnackbar(layoutPlaceSnack, getString(R.string.back_pressed_retry),
            this, {setIsBackPress()})

            snackbar.show()

        } else {
            onBackPressed()
        }

    }

    fun setIsBackPress() {
        isBackPress = false
    }
}