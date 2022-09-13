package com.tstites.myhr.ui.screens

/*The Screens class provides a routing table for the app to reference when a request to navigate
* to a different page*/
sealed class Screens(val route: String) {
    object HomeScreen : Screens("home_screen")

    object EmployeeList: Screens("employee_list")
    object EmployeeDetails: Screens("employee_details")
    object EmployeeNew: Screens("employee_new")

    object ProjectList: Screens("project_list")
}
