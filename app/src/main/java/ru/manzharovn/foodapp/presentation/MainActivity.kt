package ru.manzharovn.foodapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.manzharovn.foodapp.presentation.ui.screen.MenuScreen
import ru.manzharovn.foodapp.presentation.ui.screen.ProfileScreen
import ru.manzharovn.foodapp.presentation.ui.screen.ShoppingCartScreen
import ru.manzharovn.foodapp.presentation.ui.theme.BottomNavColor
import ru.manzharovn.foodapp.presentation.ui.theme.FoodAppTheme
import ru.manzharovn.foodapp.presentation.ui.theme.UnselectedBottomItemColor
import ru.manzharovn.foodapp.presentation.ui.viewmodel.MenuViewModel
import ru.manzharovn.foodapp.presentation.ui.viewmodel.MenuViewModelFactory
import ru.manzharovn.foodapp.presentation.utils.Screen
import ru.manzharovn.foodapp.presentation.utils.screens
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var menuViewModelFactory: MenuViewModelFactory
    val menuViewModel: MenuViewModel by viewModels {
        menuViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            FoodAppTheme {
                FoodApp(menuViewModel)
            }
        }
    }
}

@Composable
fun FoodApp(menuViewModel: MenuViewModel) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation(
                backgroundColor = BottomNavColor
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                screens.forEach { screen ->
                    BottomNavigationItem(
                        selectedContentColor = MaterialTheme.colors.primary,
                        unselectedContentColor = UnselectedBottomItemColor,
                        icon = { Icon(painter = painterResource(id = screen.icon), contentDescription = null) },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Menu.route, Modifier.padding(innerPadding)) {
            composable(Screen.Menu.route) { MenuScreen(menuViewModel = menuViewModel) }
            composable(Screen.Profile.route) { ProfileScreen() }
            composable(Screen.ShoppingCart.route) { ShoppingCartScreen() }
        }
    }
}

