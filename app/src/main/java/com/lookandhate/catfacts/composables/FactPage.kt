package com.lookandhate.catfacts.composables

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.lookandhate.catfacts.AppMain
import com.lookandhate.catfacts.R
import com.lookandhate.catfacts.activities.textForButton
import com.lookandhate.catfacts.viewModels.Fact

@Composable
fun FactPage(factToDispay: Fact?, imageUrl: String?) {
    Log.d("FactPage", "Got $factToDispay as fact")
    val fact = factToDispay ?: Fact("Null", true)
    val checkedState = remember { mutableStateOf(fact.isFavorite) }
    val factIndexInArray = AppMain.getFactIndexByItsText(fact.factText)
    Column() {
        Text(
            text = fact.factText,
            modifier = Modifier.padding(5.dp)
        )
        Image(
            painter = rememberImagePainter(imageUrl,
                builder = { placeholder(R.drawable.downloading_indicator) }),
            contentDescription = null,
            modifier = Modifier.size(800.dp),
        )
    }
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = stringResource(textForButton(checkedState.value)),
            modifier = Modifier.clickable {
                AppMain.factList[factIndexInArray].isFavorite = !checkedState.value
                checkedState.value = !checkedState.value
            })
    }
}