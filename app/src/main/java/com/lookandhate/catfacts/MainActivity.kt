package com.lookandhate.catfacts

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.lookandhate.catfacts.activities.FactsFromTheNetComposable
import com.lookandhate.catfacts.activities.FavoritesComposable
import com.lookandhate.catfacts.ui.theme.CatFactsTheme
import com.lookandhate.catfacts.viewModels.Fact

object AppMain {
    var factList by mutableStateOf(mutableListOf<Fact>())
    fun getFactIndexByItsText(text: String): Int {
        factList.forEach { it ->
            if (it.factText == text)
                return factList.indexOf(it)
        }
        return -1
    }
    const val factsFromTheNetPageTag = "download"
    const val favoritesFactsPageTag = "favorites"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pageToDisplay: String = intent.getStringExtra("pageToDisplay") ?: AppMain.factsFromTheNetPageTag
        Log.d("MainActivity",
        "pageToDisplay is ${intent.getStringExtra("pageToDisplay")}")
        setContent {
            CatFactsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen(pageToDisplay)
                }
            }
        }
    }
}

@Composable
fun MainScreen(displayingPage: String = "download") {
    val context = LocalContext.current
    val selectedItem = remember { mutableStateOf(displayingPage) }


    Scaffold(
        bottomBar = {
            BottomAppBar {
                BottomNavigation() {

                    BottomNavigationItem(
                        icon = {
                            Icon(Icons.Filled.ArrowDropDown, "")
                        },
                        label = { Text(text = "Facts") },
                        selected = selectedItem.value == AppMain.factsFromTheNetPageTag,
                        onClick = {
                            Toast.makeText(
                                context,
                                "Facts from the net clicked",
                                Toast.LENGTH_SHORT
                            ).show()
                            selectedItem.value = AppMain.factsFromTheNetPageTag

                        },
                        alwaysShowLabel = false
                    )

                    BottomNavigationItem(
                        icon = {
                            Icon(Icons.Filled.Favorite, "")
                        },
                        label = { Text(text = "Favorite") },
                        onClick = {
                            Toast.makeText(context, "Favorite clicked", Toast.LENGTH_SHORT).show()
                            selectedItem.value = AppMain.favoritesFactsPageTag
                        },
                        selected = selectedItem.value == AppMain.favoritesFactsPageTag,
                        alwaysShowLabel = false
                    )
                }
            }
        },
        content = {
            if (selectedItem.value == AppMain.factsFromTheNetPageTag) {
                FactsFromTheNetComposable()
            } else if (selectedItem.value == AppMain.favoritesFactsPageTag) {
                FavoritesComposable()
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CatFactsTheme {
    }
}