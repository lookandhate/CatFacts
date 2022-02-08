package com.lookandhate.catfacts.activities

import android.content.Intent
import android.util.Log
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
import com.lookandhate.catfacts.viewModels.Fact


@Composable
fun FactCard(factToDisplay: Fact) {
    Card(
        shape = RoundedCornerShape(3.dp),
        modifier = Modifier.padding(3.dp),

        backgroundColor = Color.DarkGray
    ) {
        Row {
            val context = LocalContext.current
            Icon(Icons.Filled.Favorite, "favorite")
            Text(
                text = factToDisplay.factText,
                modifier = Modifier
                    .width(400.dp)
                    .clickable {
                        val intent = Intent(context, FactActivity::class.java)


                        intent.putExtra("fact", factToDisplay)
                        Log.d(
                            "FactCard",
                            "Starting FactActivity and passing $factToDisplay as fact"
                        )
                        context.startActivity(intent)

                    }
            )
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
                FactCard(factToDisplay = fact)
            }
        }
    }
}
