package ru.manzharovn.foodapp.presentation.ui.screen

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import ru.manzharovn.foodapp.R
import ru.manzharovn.foodapp.presentation.model.Food
import ru.manzharovn.foodapp.presentation.ui.theme.FoodAppTheme
import ru.manzharovn.foodapp.presentation.ui.viewmodel.MenuViewModel
import ru.manzharovn.foodapp.presentation.utils.*

@Composable
fun MenuScreen(menuViewModel: MenuViewModel) {
    when(menuViewModel.status) {
        Status.OK -> {
            Menu(
                selectedCity = menuViewModel.selectedCity,
                cities = cities,
                bannerIds = bannerIds,
                categories = menuViewModel.categories,
                currentCategory = menuViewModel.currentCategory,
                onCategoryClick = menuViewModel::onCategoryClick,
                onCityChange = menuViewModel::onCityChange,
                imageLoader = menuViewModel.imageLoader,
                food = menuViewModel.food
            )
        }
        Status.LOADING -> {
            LoadingScreen()
        }
        else -> {
            ErrorMessage(
                status = menuViewModel.status,
                tryAgain = menuViewModel::tryAgain
            )
        }
    }

}

@Composable
fun Menu(
    selectedCity: String,
    food: List<Food>,
    cities: List<String>,
    bannerIds: List<Int>,
    categories: List<String>,
    imageLoader: ImageLoader,
    currentCategory: String,
    onCityChange: (String) -> Unit,
    onCategoryClick: (String) -> Unit
) {
    val maxBannerHeight = 122
    var bannerHeight by remember {
        mutableStateOf(maxBannerHeight)
    }
    var columnScrollEnabled by remember {
        mutableStateOf(true)
    }
    val userScrollEnabled = bannerHeight == 0
    val elevation by animateDpAsState(targetValue = if(userScrollEnabled) 10.dp else 0.dp)
    Column(
        modifier = Modifier.scrollable(
            orientation = Orientation.Vertical,
            enabled = columnScrollEnabled,
            state = rememberScrollableState { delta ->
                bannerHeight += delta.toInt()
                if(bannerHeight < 0) {
                    bannerHeight = 0
                    columnScrollEnabled = false
                }
                if(bannerHeight > maxBannerHeight) {
                    bannerHeight = maxBannerHeight
                }
                delta
            }
        )
    ) {
        Header(
            selectedCity = selectedCity,
            cities = cities,
            bannerIds = bannerIds,
            categories = categories,
            currentCategory = currentCategory,
            onCategoryClick = onCategoryClick,
            onCityChange = onCityChange,
            elevation = elevation,
            bannerHeight = bannerHeight.dp
        )
        FoodList(
            modifier = Modifier.fillMaxSize(),
            food = food,
            userScrollEnabled = userScrollEnabled,
            onTopReached = {
                columnScrollEnabled = true
            },
            imageLoader = imageLoader
        )
    }
}

@Composable
fun Header(
    selectedCity: String,
    cities: List<String>,
    bannerIds: List<Int>,
    bannerHeight: Dp,
    categories: List<String>,
    currentCategory: String,
    elevation: Dp,
    onCityChange: (String) -> Unit,
    onCategoryClick: (String) -> Unit
) {
    Surface(
        elevation = elevation
    ) {
        Column(
        ) {
            NavigationBar(
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 16.dp)
                    .fillMaxWidth(),
                selectedCity = selectedCity,
                cities = cities,
                onCityChange = onCityChange
            )
            Banners(
                modifier = Modifier,
                bannerIds = bannerIds,
                bannerHeight = bannerHeight
            )
            FoodCategories(
                modifier = Modifier
                    .fillMaxWidth(),
                categories = categories,
                currentCategory = currentCategory,
                onCategoryClick = onCategoryClick
            )
        }
    }
}

@Composable
fun FoodList(
    modifier: Modifier = Modifier,
    userScrollEnabled: Boolean,
    onTopReached: () -> Unit,
    imageLoader: ImageLoader,
    food: List<Food>,
) {
    val scrollState = rememberLazyListState()
    val firstVisibleItemScrollOffset by remember { derivedStateOf { scrollState.firstVisibleItemScrollOffset } }
    val firstVisibleItemIndex by remember { derivedStateOf { scrollState.firstVisibleItemIndex } }
    if(firstVisibleItemIndex == 0 && firstVisibleItemScrollOffset == 0) {
        onTopReached()
    }
    LazyColumn(
        modifier = modifier,
        state = scrollState,
        userScrollEnabled = userScrollEnabled,
    ) {
        itemsIndexed(food) { index, item ->
            FoodCard(
                modifier = Modifier.padding(16.dp),
                food = item,
                imageLoader = imageLoader
            )
            if(index != food.lastIndex) {
                Divider(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
fun Banners(
    modifier: Modifier = Modifier,
    bannerHeight: Dp,
    bannerIds: List<Int>
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(bannerIds) {
            Banner(
                imageId = it,
                startPadding = if(bannerIds.first() == it) 16.dp else 0.dp,
                bannerHeight = bannerHeight,
                endPadding = if(bannerIds.last() == it) 16.dp else 0.dp,
            )

        }
    }
}

@Composable
fun NavigationBar(
    modifier: Modifier = Modifier,
    selectedCity: String,
    cities: List<String>,
    onCityChange: (String) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CitySelector(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            selectedCity = selectedCity,
            cities = cities,
            onCityChange = onCityChange
        )
        QrCode(modifier = Modifier.padding(0.dp))
    }
}

@Composable
fun FoodCategories(
    modifier: Modifier = Modifier,
    categories: List<String>,
    currentCategory: String,
    onCategoryClick: (String) -> Unit,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) {
            Category(
                modifier = Modifier.padding(vertical = 24.dp),
                onCategoryClick = onCategoryClick,
                isActive = currentCategory == it,
                startPadding = if(categories.first() == it) 16.dp else 0.dp,
                endPadding = if(categories.last() == it) 16.dp else 0.dp,
                name = it
            )
        }
    }
}


@Composable
fun Category(
    modifier: Modifier = Modifier,
    onCategoryClick: (String) -> Unit,
    startPadding: Dp,
    endPadding: Dp,
    isActive: Boolean,
    name: String
) {
    val elevation: Dp by animateDpAsState(
        targetValue = if (isActive) 0.dp else 10.dp
    )
    val textColor: Color by animateColorAsState(
        targetValue =
            if(isActive)
                MaterialTheme.colors.primary
            else
                Color.LightGray
    )
    val backgroundColor: Color by animateColorAsState(
        targetValue =
            if(isActive)
                MaterialTheme.colors.primary.copy(alpha = .2f)
            else
                MaterialTheme.colors.background
    )
    Surface(
        modifier = modifier
            .padding(start = startPadding, end = endPadding)
            .size(width = 88.dp, height = 32.dp)
            .clickable { onCategoryClick(name) }
            .shadow(elevation = elevation, shape = RoundedCornerShape(6.dp)),
        shape = RoundedCornerShape(6.dp),
        color = backgroundColor
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.button,
                color = textColor,
                text = name
            )
        }
    }
}

@Composable
fun QrCode(
    modifier: Modifier = Modifier
) {
    IconButton(
        modifier = modifier,
        onClick = { /*TODO*/ }
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = R.drawable.qrcode),
            contentDescription = "arrow button"
        )
    }
}

@Composable
fun CitySelector(
    modifier: Modifier = Modifier,
    selectedCity: String,
    onCityChange: (String) -> Unit,
    cities: List<String>
) {
    var expanded by remember { mutableStateOf(false) }
    val degress: Float by animateFloatAsState(targetValue = if(expanded) -90f else 0f)
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Crossfade(targetState = selectedCity) {
            Text(
                text = it,
                fontWeight = FontWeight.Medium
            )
        }
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                modifier = Modifier.rotate(degress),
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = "arrow button")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            cities.forEach {
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onCityChange(it)
                    })
                {
                    Text(it)
                }
            }
        }
    }
}

@Composable
fun Banner(
    modifier: Modifier = Modifier,
    startPadding: Dp,
    bannerHeight: Dp,
    endPadding: Dp,
    imageId: Int
) {
    Image(
        modifier = modifier
            .padding(start = startPadding, end = endPadding)
            .size(width = 300.dp, height = bannerHeight)
            .clip(RoundedCornerShape(10.dp)),
        contentScale = ContentScale.Crop,
        painter = painterResource(id = imageId),
        contentDescription = "banner image"
    )
}

@Composable
fun FoodImage(
    imageUrl: String?,
    imageLoader: ImageLoader
) {
    val context = LocalContext.current
    val placeholderImage = R.drawable.placeholder

    val imageRequest = ImageRequest.Builder(context)
        .data(imageUrl)
        .memoryCacheKey(imageUrl)
        .diskCacheKey(imageUrl)
        .placeholder(placeholderImage)
        .error(placeholderImage)
        .fallback(placeholderImage)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()

    AsyncImage(
        modifier = Modifier.size(135.dp),
        contentScale = ContentScale.FillBounds,
        model = imageRequest,
        contentDescription = null,
        imageLoader = imageLoader
    )
}

@Composable
fun FoodCard(
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader,
    food: Food,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        FoodImage(imageUrl = food.imageUrl, imageLoader = imageLoader)
        Column(
            modifier = Modifier
                .height(135.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = food.name,
                fontWeight = FontWeight.Bold,
            )
            Text(
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.body2,
                color = Color.Gray,
                text = food.description ?: ""
            )
            OutlinedButton(
                modifier = Modifier.align(Alignment.End),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colors.primary
                ),
                onClick = {}
            ) {
                Text(stringResource(id = R.string.price, food.price ?: 0))
            }
        }

    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewFoodCard() {
//    FoodAppTheme {
//        FoodCard(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            food = Food(
//                name = "Маргарита",
//                description = "Ветчина,шампиньоны, увеличинная порция моцареллы, томатный соус",
//                price = 500,
//                imageUrl = ""
//            )
//        )
//    }
//}

@Preview(showBackground = true)
@Composable
fun PreviewBanner() {
    FoodAppTheme {
        Banner(
            imageId = R.drawable.banner_1,
            startPadding = 0.dp,
            bannerHeight = 112.dp,
            endPadding = 0.dp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCitySelector() {
    FoodAppTheme {
        CitySelector(
            selectedCity = "Москва",
            cities = listOf(),
            onCityChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNavigationBar() {
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        selectedCity = "Москва",
        cities = listOf(),
        onCityChange = {}
    )
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewHeader() {
//    Header(
//        selectedCity = "Москва",
//        cities = listOf(),
//        bannerIds = listOf(R.drawable.banner_1, R.drawable.banner_2),
//        ca
//    )
//}
