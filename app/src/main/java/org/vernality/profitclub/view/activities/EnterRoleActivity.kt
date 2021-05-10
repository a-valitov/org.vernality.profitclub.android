package org.vernality.profitclub.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.vernality.profitclub.R
import org.vernality.profitclub.model.data.Member
import org.vernality.profitclub.model.data.Organization
import org.vernality.profitclub.model.data.Supplier


public class EnterRoleActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_role)
    }

}

object DataRoleSaver{
    var organization:Organization? = null

    var supplier: Supplier? = null

    var member: Member? = null
}