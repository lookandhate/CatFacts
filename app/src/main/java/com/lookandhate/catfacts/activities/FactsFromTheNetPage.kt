package com.lookandhate.catfacts.activities


import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import com.lookandhate.catfacts.AppMain
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
            FactCard(factToDisplay = fact)
        }
    }

}