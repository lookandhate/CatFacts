package com.lookandhate.catfacts.composables

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import com.lookandhate.catfacts.AppMain


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
