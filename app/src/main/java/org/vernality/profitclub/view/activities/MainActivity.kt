package org.vernality.profitclub.view.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.parse.Parse
import com.parse.ParseObject
import com.parse.ParseQuery
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.vernality.profitclub.R
import org.vernality.profitclub.model.repository.RepositoryImplementation
import org.vernality.profitclub.utils.ui.ActionBottomDialogFragment
import org.vernality.profitclub.utils.ui.MyPreferences


class MainActivity : AppCompatActivity() , ActionBottomDialogFragment.ItemClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pref = get<MyPreferences> ()

    }

    override fun onItemClick(item: String?) {
        Toast.makeText(this, "Selected action item is " + item, Toast.LENGTH_LONG).show()
    }
}