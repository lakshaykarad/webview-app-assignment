package com.example.learning_basics.ui.theme.screen

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.learning_basics.ui.theme.viewmodel.HomeViewModel
import com.example.learning_basics.ui.theme.viewmodel.UiEvent
import com.google.gson.annotations.Until
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import androidx.compose.runtime.collectAsState


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    onNavigateToWebView: (String) -> Unit,
    onNavigatetoHistory: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val urlInput by viewModel.urlInput.collectAsState()

    // Current Screen
    val backStackEntry = navController.currentBackStackEntry
    // small storage use to receive data from this screen
    val savedStateHandle = backStackEntry?.savedStateHandle

    // clearInput have true or false value if it is true we reset url
    val clearInput by savedStateHandle
        ?.getStateFlow("clear_input", false)
        ?.collectAsState()
        ?: remember { mutableStateOf(false) }

    LaunchedEffect(clearInput) {
        // if clearInput true reset url to empty
        if (clearInput) {
            viewModel.onUrlChange("")
            // reset clearInput to false again
            savedStateHandle?.set("clear_input", false)
        }
    }


    LaunchedEffect(true) {
        viewModel.uiEvent.collect { event ->
            when(event){
                is UiEvent.ShowError -> {
                    Toast.makeText(context, event.error, Toast.LENGTH_SHORT).show()
                }
                is UiEvent.NavigateToWebView -> {
                    onNavigateToWebView(event.url)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Home Browser", fontWeight = FontWeight.Bold)
                },
                actions = {
                    Button(onClick = onNavigatetoHistory, colors = ButtonDefaults.buttonColors(Color.Black)) {
                        Icon(Icons.Default.Menu, contentDescription = "History Button", )
                    }
                }
            )
        }
    ) {padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val pagerState = rememberPagerState(pageCount = { 7 })

            val images = listOf(
                "https://picsum.photos/600/300?random=1",
                "https://picsum.photos/600/300?random=2",
                "https://picsum.photos/600/300?random=3"
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) { page ->
                Card(
                    modifier = Modifier.fillMaxSize().padding(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(images[page]),
                        contentDescription = "Carousel Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))


            OutlinedTextField(
                value = urlInput,
                onValueChange = { viewModel.onUrlChange(it)},
                label = { Text("Enter URL") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardActions = KeyboardActions(
                    onGo = { viewModel.validInput(urlInput) }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.validInput(urlInput) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Open URL")
            }
        }
    }
}