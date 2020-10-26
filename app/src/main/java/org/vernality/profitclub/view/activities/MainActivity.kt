package org.vernality.profitclub.view.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.parse.Parse
import com.parse.ParseObject
import com.parse.ParseQuery
import org.vernality.profitclub.R
import org.vernality.profitclub.utils.ui.ActionBottomDialogFragment


class MainActivity : AppCompatActivity() , ActionBottomDialogFragment.ItemClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Parse.initialize(Parse.Configuration.Builder(this)
                .applicationId("org.vernality.alliance") // if defined
                .clientKey("hWlREY7dvWb7sLpCVfZrReWNKPHh4uJT")
                .server("https://alliance.vernality.net/parse")
                .build()
        )

//        val gameScore = ParseObject("GameScore")
//        gameScore.put("score", 1337)
//        gameScore.put("playerName", "Sean Plott")
//        gameScore.put("cheatMode", false)
//        gameScore.saveInBackground()

        val query = ParseQuery.getQuery<ParseObject>("GameScore")
        query.findInBackground { postList, e -> kotlin.run{
            if(e == null) {
                var i:Int=0;
                postList.forEach {
                    val objectId: String = it.getObjectId()
                    println("response ["+i+"]= "+postList[i].get("score")+", id ="+postList[i].objectId)
                    i++
                }

            }
        }}

    }

    override fun onItemClick(item: String?) {
        Toast.makeText(this, "Selected action item is " + item, Toast.LENGTH_LONG).show()
    }
}