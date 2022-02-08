package com.lookandhate.catfacts.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.lookandhate.catfacts.AppMain
import com.lookandhate.catfacts.ui.theme.CatFactsTheme
import com.lookandhate.catfacts.viewModels.Fact
import okhttp3.*
import org.json.JSONArray
import java.io.IOException


internal fun updateListOfFacts(facts: MutableList<Fact>) {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://cat-fact.herokuapp.com/facts")
        .build()
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                Log.i("updateListOfFacts", "onResponse invoked")
                val arr = JSONArray(response.body!!.string())

                for (i in 0 until arr.length()) {

                    val jsonObject = arr.getJSONObject(i)
                    val catFact = jsonObject.getString("text")
                    Log.d(
                        "updateListOfFacts",
                        "adding $catFact to facts"
                    )
                    if (!facts.any { existedFact -> existedFact.factText == catFact })
                        facts.add(Fact(catFact, false))


                }
                Log.d(
                    "updateListOfFacts",
                    "on response, facts state: ${facts.joinToString(", ")}"
                )
            }
        }
    })

}

internal fun checkListNeedsToBeUpdated(facts: MutableList<Fact>): Boolean {
    Log.d(
        "checkListNeedsToBeUpdated",
        "Size of $facts = ${facts.size}"
    )
    return facts.size == 0
}


@Composable
fun FactsFromTheNetComposable() {
    // Todo: fix data not saving after change of the screen(after redraw)
    val needToUpdateTheList: Boolean = checkListNeedsToBeUpdated(AppMain.factList)
    Log.d(
        "FactsFromTheNetComposable",
        "Checking, do we need to update list with facts: $needToUpdateTheList"
    )
    if (needToUpdateTheList) {
        updateListOfFacts(AppMain.factList)
    }
    Column {
        //Text(text = text.value)
        Log.d(
            "FactsFromTheNetComposable",
            "OnRedraw, facts state: ${AppMain.factList.joinToString(", ")}"
        )
        AppMain.factList.forEach { fact ->
            Card(
                shape = RoundedCornerShape(3.dp),
                modifier = Modifier.padding(3.dp),
                backgroundColor = Color.DarkGray

            ) {
                Row {

                    val checkedState = remember { mutableStateOf(fact.isFavorite) }
                    // Context of current activity that we will use on FactActivity launch

                    val context = LocalContext.current
                    if(checkedState.value)
                        Icon(Icons.Outlined.Favorite, "favorite")
                    else
                        Icon(Icons.Filled.Favorite, "favorite")
                    Text(
                        text = fact.factText,
                        modifier = Modifier
                            .width(400.dp)
                            .clickable {
                                // Launching Fact activity

                                // Creating intent for activity to start
                                val intent = Intent(context, FactActivity::class.java)

                                // Passing selected fact to activity
                                intent.putExtra("fact", fact)
                                intent.putExtra("fromPage", AppMain.factsFromTheNetPageTag)
                                Log.d(
                                    "FactsFromTheNetComposable:Card:Row:Text",
                                    "Starting FactActivity and passing $fact as fact"
                                )

                                // Starting Activity
                                context.startActivity(intent)

                            }
                    )
                }
            }
        }
    }

}