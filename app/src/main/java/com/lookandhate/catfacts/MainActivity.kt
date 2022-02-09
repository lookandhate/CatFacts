package com.lookandhate.catfacts

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Favorite

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import com.lookandhate.catfacts.composables.FactsFromTheNetComposable
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

}

sealed class Screen(val route: String, @StringRes val screenLabelResource: Int, val icon: ImageVector) {
    object Download : Screen("download", R.string.facts_from_net_page, Icons.Filled.ArrowDropDown)
    object Favorites : Screen("favorites", R.string.favorites_facts_page, Icons.Filled.Favorite)
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
    val navController = rememberNavController()
    val navigationScreens = listOf(
        Screen.Download,
        Screen.Favorites
    )

    Scaffold(
        bottomBar = {
            BottomAppBar {
                BottomNavigation() {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    navigationScreens.forEach { screen ->
                        BottomNavigationItem(
                            icon = {
                                Icon(screen.icon, null)
                            },
                            label = { Text(text = stringResource(screen.screenLabelResource)) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            alwaysShowLabel = true
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.Download.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Screen.Download.route) { FactsFromTheNetComposable() }
            composable(Screen.Favorites.route) { FavoritesComposable() }
        }
    }

}
