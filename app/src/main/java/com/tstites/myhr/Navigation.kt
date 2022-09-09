package com.tstites.myhr

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tstites.myhr.ui.screens.*

/*In order to navigate to other pages, this file is required. It contains a single @Composable that
* links the Screens.kt routing table to the actual page's .kt file and all the @Composable elements
* that it contains.*/
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
        //This screen uses parameters from a different screen
        composable(Screens.EmployeeDetails.route + "/{id}/{name}/{address}/{city}/{state}/{department}/{title}/{extension}",
            //This parameter dictates what arguments are present in the route, as well as
            //their types and default values
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
                    type = NavType.IntType
                    defaultValue = 1
                }
            )) {
            val eDetails = EmployeeDetails()
            eDetails.EmployeeDetailsLayout(navController = navController, it.arguments)
        }
    }
}