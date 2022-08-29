package com.tstites.myhr.ui.screens

sealed class Screens(val route: String) {
    object HomeScreen : Screens("home_screen")
    object EmployeeList: Screens("employee_list")
}
