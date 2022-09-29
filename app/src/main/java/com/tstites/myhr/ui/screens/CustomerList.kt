package com.tstites.myhr.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

class CustomerList {
    @Composable
    fun CustomerListLayout(navController: NavController) {

    }

    @Preview
    @Composable
    fun CustomerListLayoutPreview() {
        CustomerListLayout(navController = rememberNavController())
    }
}