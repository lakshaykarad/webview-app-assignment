package com.example.learning_basics.ui.theme.screen

sealed class Screen(val route: String) {

    object Home : Screen("home_screen")
    object History : Screen("history_screen")

    object WebView : Screen("webview_screen/{url}") {
        fun createRoute(encodedUrl: String): String {
            return "webview_screen/$encodedUrl"
        }
    }
}