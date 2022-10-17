package ru.manzharovn.foodapp.presentation.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ru.manzharovn.foodapp.R

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int,
    @DrawableRes val icon: Int
) {
    object Menu : Screen("menu", R.string.menu, R.drawable.ic_menu)
    object Profile : Screen("profile", R.string.profile, R.drawable.ic_profile)
    object ShoppingCart: Screen("shopping_cart", R.string.shopping_cart, R.drawable.ic_shopping_cart)
}

val screens = listOf(
    Screen.Menu,
    Screen.Profile,
    Screen.ShoppingCart
)