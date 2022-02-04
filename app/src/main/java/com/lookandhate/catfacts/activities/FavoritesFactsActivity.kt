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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.lookandhate.catfacts.AppMain
import com.lookandhate.catfacts.ui.theme.CatFactsTheme

class FavoritesFactsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            CatFactsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    FavoritesComposable()
                }
            }
        }
    }
}

@Composable
fun FavoritesComposable() {
    // Todo: fix data not saving after change of the screen(after redraw)
    Column {
        //Text(text = text.value)
        Log.d(
            "FavoritesComposable",
            "OnRedraw, facts state: ${AppMain.factList.joinToString(", ")}"
        )
        AppMain.factList.forEach { fact ->
            if (fact.isFavorite) {
                Card(
                    shape = RoundedCornerShape(3.dp),
                    modifier = Modifier.padding(3.dp),

                    backgroundColor = Color.DarkGray
                ) {
                    Row {
                        val context = LocalContext.current
                        Icon(Icons.Filled.Favorite, "favorite")
                        Text(
                            text = fact.factText,
                            modifier = Modifier
                                .width(400.dp)
                                .clickable {
                                    val intent = Intent(context, FactActivity::class.java)
                                    intent.putExtra("fact", fact)
                                    intent.putExtra("fromPage", AppMain.favoritesFactsPageTag)
                                    Log.d(
                                        "FavoritesComposable:Card:Row:Text",
                                        "Starting FactActivity and passing $fact as fact"
                                    )
                                    context.startActivity(intent)

                                }
                        )
                    }
                }
            }
        }
    }
}
