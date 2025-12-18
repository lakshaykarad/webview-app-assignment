package com.example.learning_basics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.learning_basics.ui.theme.screen.HistoryScreen
import com.example.learning_basics.ui.theme.screen.HomeScreen
import com.example.learning_basics.ui.theme.screen.Screen
import com.example.learning_basics.ui.theme.screen.WebViewScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = Screen.Home.route) {

                composable(route = Screen.Home.route) {
                    HomeScreen(
                        navController = navController,
                        onNavigateToWebView = { url ->
                            val Url = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                            navController.navigate(Screen.WebView.createRoute(Url))
                        },
                        onNavigatetoHistory = {
                            navController.navigate(Screen.History.route)
                        }
                    )
                }

                composable(
                    route = Screen.WebView.route,
                    // take the link here
                    arguments = listOf(navArgument("url") { type = NavType.StringType })
                ) { backStackEntry ->

                    val urlArg = backStackEntry.arguments?.getString("url") ?: ""
                    // URLDecoder use to decode unwanted char of the links ->
                    // https%3A%2F%2Fgoogle.com%3Fq%3Dandroid -> https://google.com?q=android
                    val passUrl = URLDecoder.decode(urlArg, StandardCharsets.UTF_8.toString())

                    WebViewScreen(
                        initialUrl = passUrl,
                        onBackClick = {
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("clear_input", false) // don't clear the link
                            navController.popBackStack()
                        },
                        onCloseClick = {
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("clear_input", true) // clear the link
                            navController.popBackStack()
                        }
                    )
                }

                composable(route = Screen.History.route) {
                    HistoryScreen(
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}