package org.vernality.profitclub.view.member

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import org.vernality.profitclub.R
import org.vernality.profitclub.view.activities.SelectOrganizationActivity
import java.lang.reflect.Field

class MemberActivity : AppCompatActivity() {

    lateinit var settingsIV: ImageView
    lateinit var popupMenu:PopupMenu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member)
        val toolbar: Toolbar = findViewById(R.id.myToolbar)
        setSupportActionBar(toolbar)
        initViews()


    }

    fun initViews(){

        settingsIV = findViewById(R.id.iv_more)

        initPopupMenu(settingsIV)

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
}