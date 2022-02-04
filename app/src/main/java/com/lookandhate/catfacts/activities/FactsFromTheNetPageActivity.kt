package com.lookandhate.catfacts.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lookandhate.catfacts.AppMain
import com.lookandhate.catfacts.ui.theme.CatFactsTheme
import com.lookandhate.catfacts.viewModels.Fact
import okhttp3.*
import org.json.JSONArray
import java.io.IOException


internal fun updateListOfFacts(facts: MutableList<Fact>) {
    val client: OkHttpClient = OkHttpClient()
    val request = Request.Builder()
        .url("https://cat-fact.herokuapp.com/facts")
        .build()
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                Log.i("PlaceholderFactsFromTheNetComposable", "onResponse invoked")
                val arr = JSONArray(response.body!!.string())

                for (i in 0 until arr.length()) {

                    val jsonObject = arr.getJSONObject(i)
                    val catFact = jsonObject.getString("text")
                    Log.d(
                        "PlaceholderFactsFromTheNetComposable",
                        "adding $catFact to facts"
                    )
                    if (!facts.any { existedFact -> existedFact.factText == catFact })
                        facts.add(Fact(catFact, false))


                }
                Log.d(
                    "PlaceholderFactsFromTheNetComposable",
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

class FactsFromTheNetPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CatFactsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    PlaceholderFactsFromTheNetComposable()
                }
            }
        }
    }
}

@Composable
fun PlaceholderFactsFromTheNetComposable() {
    // Todo: fix data not saving after change of the screen(after redraw)
    val needToUpdateTheList: Boolean = checkListNeedsToBeUpdated(AppMain.factList)
    Log.d(
        "PlaceholderFactsFromTheNetComposable",
        "Checking, do we need to update list with facts: $needToUpdateTheList"
    )
    if (needToUpdateTheList) {
        updateListOfFacts(AppMain.factList)
    }
    Column {
        //Text(text = text.value)
        Log.d(
            "PlaceholderFactsFromTheNetComposable",
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
                    Text(
                        text = fact.factText,
                        modifier = Modifier.width(350.dp)
                    )
                    Switch(
                        checked = checkedState.value,
                        onCheckedChange = {
                            fact.isFavorite = !fact.isFavorite;
                            checkedState.value = fact.isFavorite;
                            Log.d(
                                "PlaceholderFactsFromTheNetComposable:Row",
                                "Changing state of $fact is $it"
                            )
                        }
                    )
                }
            }
        }
        Button(
            onClick = { updateListOfFacts(AppMain.factList) },
            content = { Text("Click me") }

        )
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    CatFactsTheme {
        PlaceholderFactsFromTheNetComposable()
    }
}