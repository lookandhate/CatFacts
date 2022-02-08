package com.lookandhate.catfacts.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.lookandhate.catfacts.AppMain
import com.lookandhate.catfacts.R
import com.lookandhate.catfacts.activities.ui.theme.CatFactsTheme
import com.lookandhate.catfacts.viewModels.Fact
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


fun textForButton(state: Boolean) =
    if (!state)
        "Add to favorites"
    else "Remove from favorites"

class FactActivity : ComponentActivity() {
    var url: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Get URL for cat image
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://aws.random.cat/meow")
            .build()

        // Getting image of cat and writing it to activity public field.
        // So we can get url later, when we will call Composable fun
        //
        // There actually cold be the method, to call FactPage, with url, but without saving it
        // to FactActivity field. But i could not figure it out, YET
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {

                    val imageUrl = JSONObject(response.body!!.string()).getString("file")
                    Log.i(
                        "FactActivity:onCreate:OkHttpClient.newCall",
                        "onResponse invoked. URL is $imageUrl"
                    )
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
                                FactPage(fact, imageUrl)
                            }
                        }
                    }
                }
            }
        })

        setContent {
            CatFactsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val factToDisplay = intent.getParcelableExtra<Fact>("fact")
                    Log.d(
                        "FactActivity",
                        "Got $factToDisplay from intent, launching composable"
                    )
                    FactPage(factToDisplay, this.url)
                }
            }
        }

    }
}

@Composable
fun FactPage(factToDispay: Fact?, imageUrl: String?) {
    Log.d("FactPage", "Got $factToDispay as fact")
    val fact = factToDispay ?: Fact("Null", true)
    val checkedState = remember { mutableStateOf(fact.isFavorite) }
    val factIndexInArray = AppMain.getFactIndexByItsText(fact.factText)
    Column() {
        Text(text = fact.factText,
        modifier = Modifier.padding(5.dp))

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
            text = textForButton(checkedState.value),
            modifier = Modifier.clickable {
                AppMain.factList[factIndexInArray].isFavorite = !checkedState.value
                checkedState.value = !checkedState.value
            })
    }
}