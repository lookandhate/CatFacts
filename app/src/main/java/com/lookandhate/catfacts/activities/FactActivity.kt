package com.lookandhate.catfacts.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lookandhate.catfacts.AppMain
import com.lookandhate.catfacts.MainActivity
import com.lookandhate.catfacts.activities.ui.theme.CatFactsTheme
import com.lookandhate.catfacts.viewModels.Fact

class FactActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CatFactsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val fact = intent.getParcelableExtra<Fact>("fact")
                    Log.d(
                        "FactActivity",
                        "Got $fact from intent, launching composable"
                    )
                    FactPage(fact)
                }
            }
        }
    }

    override fun onBackPressed() {
        val launchIntent = Intent(this, MainActivity::class.java)
        launchIntent.putExtra("pageToDisplay", intent.getStringExtra("fromPage"))
        Log.d(
            "FactActivity:onBackPressed",
            "Launching MainActivity with intent $launchIntent"
        )
        startActivity(launchIntent)
    }
}

@Composable
fun FactPage(factToDispay: Fact?) {
    Log.d("FactPage", "Got $factToDispay as fact")
    val fact = factToDispay ?: Fact("Null", true)
    val checkedState = remember { mutableStateOf(fact.isFavorite) }
    val factIndexInArray = AppMain.getFactIndexByItsText(fact.factText)
    Text(text = fact.factText)
    // TODO: REPLACE SWITCH WITH BUTTON/CLICKABLE TEXT
    Switch(
        checked = checkedState.value,
        onCheckedChange = {
            AppMain.factList[factIndexInArray].isFavorite = !checkedState.value
            checkedState.value = !checkedState.value
        })
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview4() {
    CatFactsTheme {

    }
}