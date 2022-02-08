package com.lookandhate.catfacts

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Favorite

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

import com.lookandhate.catfacts.activities.FactsFromTheNetComposable
import com.lookandhate.catfacts.composables.FavoritesComposable
import com.lookandhate.catfacts.ui.theme.CatFactsTheme
import com.lookandhate.catfacts.viewModels.Fact

object AppMain {
    val factList = mutableStateListOf<Fact>()
    fun getFactIndexByItsText(text: String): Int {
        factList.forEach {
            if (it.factText == text)
                return factList.indexOf(it)
        }
        return -1
    }

    const val factsFromTheNetPageTag = "download"
    const val favoritesFactsPageTag = "favorites"
    val pageToDisplay = mutableStateOf<String>(factsFromTheNetPageTag)

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
fun MainScreen() {
    val selectedItem = remember { mutableStateOf(AppMain.pageToDisplay.value) }
    Scaffold(
        bottomBar = {
            BottomAppBar {
                BottomNavigation() {
                    BottomNavigationItem(
                        icon = {
                            Icon(Icons.Filled.ArrowDropDown, "")
                        },
                        label = { Text(text = stringResource(R.string.facts_from_net_page)) },
                        selected = selectedItem.value == AppMain.factsFromTheNetPageTag,
                        onClick = {
                            selectedItem.value = AppMain.factsFromTheNetPageTag
                        },
                        alwaysShowLabel = false
                    )

                    BottomNavigationItem(
                        icon = {
                            Icon(Icons.Filled.Favorite, "")
                        },
                        label = { Text(text = stringResource(R.string.favorites_facts)) },
                        onClick = {
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
