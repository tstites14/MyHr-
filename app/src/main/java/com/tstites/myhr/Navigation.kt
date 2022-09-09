package com.tstites.myhr

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tstites.myhr.ui.screens.*

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
        composable(Screens.EmployeeNew.route) {
            val eNew = EmployeeNew()
            eNew.EmployeeNewLayout(navController = navController)
        }
        composable(Screens.EmployeeDetails.route + "/{id}/{name}/{address}/{city}/{state}/{department}/{title}/{extension}",
                    arguments = listOf(
                        navArgument("id") {
                            type = NavType.IntType
                            defaultValue = 1
                        }, navArgument("name") {
                            type = NavType.StringType
                            defaultValue = "Jonathan Button"
                        }, navArgument("address") {
                            type = NavType.StringType
                            defaultValue = "123 Default Ave"
                        }, navArgument("city") {
                            type = NavType.StringType
                            defaultValue = "Orlando"
                        }, navArgument("state") {
                            type = NavType.StringType
                            defaultValue = "FL"
                        }, navArgument("department") {
                            type = NavType.StringType
                            defaultValue = "Information Technology"
                        }, navArgument("title") {
                            type = NavType.StringType
                            defaultValue = "Software Engineer"
                        }, navArgument("extension") {
                            type = NavType.StringType
                            defaultValue = "001"
                        }
                    )) {
            val eDetails = EmployeeDetails()
            eDetails.EmployeeDetailsLayout(navController = navController, it.arguments)
        }
    }
}