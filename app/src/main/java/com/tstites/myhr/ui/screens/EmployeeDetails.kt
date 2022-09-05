package com.tstites.myhr.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

class EmployeeDetails {

    @Composable
    fun EmployeeDetailsLayout(navController: NavController) {

    }
    
    @Preview
    @Composable
    fun EmployeeDetailsLayoutPreview() {
        val navController = rememberNavController()
        
        EmployeeDetailsLayout(navController = navController)
    }
}