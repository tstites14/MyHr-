package com.example.myhr.ui.screens

sealed class Screens(val route: String) {
    object HomeScreen : Screens("home_screen")
}
