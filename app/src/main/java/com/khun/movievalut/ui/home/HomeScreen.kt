package com.khun.movievalut.ui.home

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.khun.movievalut.R
import com.khun.movievalut.ui.theme.Purple40
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var firstBackPressed by remember {
        mutableStateOf(false)
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.home),
            style = typography.titleLarge,
            color = Purple40
        )
    }

    BackHandler {
        if (firstBackPressed){
            val activity = context as Activity
            activity.finish()
        }else{
            firstBackPressed = true
            Toast.makeText(context, "Press again to exit", Toast.LENGTH_LONG).show()
            coroutineScope.launch {
                delay(2000L)
                firstBackPressed = false
            }
        }
    }
}