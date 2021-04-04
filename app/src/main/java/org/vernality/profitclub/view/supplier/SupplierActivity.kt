package org.vernality.profitclub.view.supplier

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.setMargins
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.android.synthetic.main.fragment_my_organizations_list.view.*
import org.koin.android.ext.android.get
import org.vernality.profitclub.R
import org.vernality.profitclub.model.data.Action
import org.vernality.profitclub.model.data.Organization
import org.vernality.profitclub.utils.ui.MyPreferences
import org.vernality.profitclub.utils.ui.UIUtils
import org.vernality.profitclub.utils.ui.WAS_ADMIN_APPROVAL_SHOWN
import org.vernality.profitclub.view.activities.MainActivity
import org.vernality.profitclub.view.activities.SelectOrganizationActivity
import org.vernality.profitclub.view.fragments.SuccessResultDialogFragment
import org.vernality.profitclub.view.fragments.TypeDialogFragment
import org.vernality.profitclub.view.organization.ORGANIZATION
import java.lang.reflect.Field
import java.text.DateFormat
import java.util.*

const val SUPPLIER = "supplier"

class SupplierActivity : AppCompatActivity() {

    lateinit var settingsIV: ImageView
    lateinit var popupMenu:PopupMenu

    var supplier: ParseObject? = null
    lateinit var pref: MyPreferences
    var objIdSet: Set<String>? = null

    lateinit var layoutPlaceSnack: CoordinatorLayout
    var isBackPress: Boolean = false

    private lateinit var successResultDialog: SuccessResultDialogFragment

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supplier)

        val toolbar: Toolbar = findViewById(R.id.myToolbar)
        setSupportActionBar(toolbar)
//        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment_supplier)


        initViews()
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_actions, R.id.navigation_members, R.id.navigation_suppliers
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)

        val arguments = intent.extras
        if(arguments != null) {
            supplier = arguments.getParcelable<ParseObject>(SUPPLIER)
        }

        supplier?.let {
            UIUtils.wasApprovalDialogShown(it.objectId)
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
                supplier?.let{
                    UIUtils.saveApprovalDialogShownEvent(it.objectId)
                }
            }

        successResultDialog.show(supportFragmentManager, this.toString())
    }

    override fun onBackPressed() {

        if(!isBackPress){
            isBackPress = true
            val snackbar =
                Snackbar.make(layoutPlaceSnack,"", Snackbar.LENGTH_SHORT)

            snackbar.addCallback(object : Snackbar.Callback(){
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    setIsBackPress()
                }
            })
            snackbar.setText(getString(R.string.back_pressed_retry))
            var view = snackbar.view
            val tv =
                view.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
            tv.textAlignment = View.TEXT_ALIGNMENT_CENTER
            snackbar.view.setBackground(resources.getDrawable(R.drawable.card_info_lite))
            val params = view.layoutParams as CoordinatorLayout.LayoutParams
            params.gravity = Gravity.TOP
            params.setMargins(800)


            snackbar.show()

        } else {
            onBackPressed()
        }

    }

    fun setIsBackPress() {
        isBackPress = false
    }
}