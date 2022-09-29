package com.tstites.myhr.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

class CustomerDetails {
    @Composable
    fun CustomerDetailsLayout(navController: NavController) {

    }

    @Preview
    @Composable
    fun CustomerDetailsLayoutPreview() {
        CustomerDetailsLayout(navController = rememberNavController())
    }
}