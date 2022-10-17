package ru.manzharovn.foodapp.presentation.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.manzharovn.foodapp.R


@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(60.dp)
            )
            Image(
                modifier = Modifier.size(45.dp),
                painter = painterResource(id = R.drawable.test_pizza_img),
                contentDescription = "ball"
            )
        }
        Text(
            modifier = Modifier.padding(4.dp),
            text = "Loading",
        )
    }
}

@Composable
fun ErrorMessage(status: Status, tryAgain: () -> Unit){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = "Error"
        )
        Button(
            modifier = Modifier.padding(all = 8.dp),
            onClick = tryAgain
        ) {
            Text("Try again")
        }
    }
}