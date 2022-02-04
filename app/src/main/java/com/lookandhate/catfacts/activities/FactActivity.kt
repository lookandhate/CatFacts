package com.lookandhate.catfacts.activities

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
                    Log.d("FactActivity",
                    "Got $fact from intent, launching composable")
                    FactPage(fact)
                }
            }
        }
    }
}

@Composable
fun FactPage(factToDispay: Fact?) {
    Log.d("FactPage","Got $factToDispay as fact")
    val fact = factToDispay ?: Fact("Null", true)
    Text(text = fact.factText)
    Switch(
        checked = fact.isFavorite,
        onCheckedChange = { fact.isFavorite = !fact.isFavorite })
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview4() {
    CatFactsTheme {

    }
}