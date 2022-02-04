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
import com.lookandhate.catfacts.activities.FactsFromTheNetPageActivity
import com.lookandhate.catfacts.activities.FavoritesFactsActivity
import com.lookandhate.catfacts.activities.PlaceholderFactsFromTheNetComposable
import com.lookandhate.catfacts.activities.PlaceholderFavoritesComposable
import com.lookandhate.catfacts.ui.theme.CatFactsTheme
import com.lookandhate.catfacts.viewModels.Fact

object AppMain {
    var factList by mutableStateOf(mutableListOf<Fact>())
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CatFactsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val selectedItem = remember { mutableStateOf("download") }

    val factsFromTheNetPageTag = "download"
    val favoritesFactsPageTag = "favorites"
    Scaffold(
        bottomBar = {
            BottomAppBar {
                BottomNavigation() {

                    BottomNavigationItem(
                        icon = {
                            Icon(Icons.Filled.ArrowDropDown, "")
                        },
                        label = { Text(text = "Facts") },
                        selected = selectedItem.value == factsFromTheNetPageTag,
                        onClick = {
                            Toast.makeText(
                                context,
                                "Facts from the net clicked",
                                Toast.LENGTH_SHORT
                            ).show()
                            selectedItem.value = factsFromTheNetPageTag

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
                            selectedItem.value = favoritesFactsPageTag
                        },
                        selected = selectedItem.value == favoritesFactsPageTag,
                        alwaysShowLabel = false
                    )
                }
            }
        },
        content = {
            if (selectedItem.value == factsFromTheNetPageTag) {
                PlaceholderFactsFromTheNetComposable()
            } else if (selectedItem.value == favoritesFactsPageTag) {
                PlaceholderFavoritesComposable()
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CatFactsTheme {
        Greeting("Android")
        //MainScreen()
    }
}