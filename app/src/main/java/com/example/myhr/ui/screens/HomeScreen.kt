package com.example.myhr.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

class HomeScreen {
    @Composable
    fun HomeScreenLayout(navController: NavController) {
        Column(modifier = Modifier.background(Color.Red)
            .fillMaxHeight(),
            verticalArrangement = Arrangement.Center) {
            IconButton(modifier = Modifier.weight(0.33f)
                .background(Color.Yellow),
                onClick = { Log.i("test","1") }) {
                Icon(modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Employee Lookup"
                )
            }
            IconButton(modifier = Modifier.weight(0.33f)
                .background(Color.Green),
                onClick = { Log.i("test","2") }) {
                Icon(modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Employee Lookup"
                )
            }
            IconButton(modifier = Modifier.weight(0.33f),
                onClick = { Log.i("test","3") }) {
                Icon(modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Employee Lookup"
                )
            }
        }
    }
    
    @Preview
    @Composable
    fun HomeScreenLayoutPreview() {
        val navController = rememberNavController()

        HomeScreenLayout(navController = navController)
    }
}