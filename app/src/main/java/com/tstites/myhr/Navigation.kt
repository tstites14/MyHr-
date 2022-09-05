package com.tstites.myhr

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tstites.myhr.ui.screens.EmployeeDetails
import com.tstites.myhr.ui.screens.EmployeeList
import com.tstites.myhr.ui.screens.HomeScreen
import com.tstites.myhr.ui.screens.Screens

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.HomeScreen.route) {
        composable(Screens.HomeScreen.route) {
            val home = HomeScreen()
            home.HomeScreenLayout(navController = navController)
        }
        composable(Screens.EmployeeList.route) {
            val eList = EmployeeList()
            eList.EmployeeListLayout(navController = navController)
        }
        composable(Screens.EmployeeDetails.route) {
            val eDetails = EmployeeDetails()
            eDetails.EmployeeDetailsLayout(navController = navController)
        }
    }
}