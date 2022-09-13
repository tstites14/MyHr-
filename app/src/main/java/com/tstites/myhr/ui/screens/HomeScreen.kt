package com.tstites.myhr.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

class HomeScreen {
    @Composable
    fun HomeScreenLayout(navController: NavController) {
        //This column has three IconButtons that split the screen into thirds.
        //Each button navigates to a different portion of the application
        Column(modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center) {
            IconButton(modifier = Modifier.weight(0.33f),
                onClick = {
                    navController.navigate(Screens.EmployeeList.route)
                }) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Icon(modifier = Modifier.fillMaxWidth().fillMaxHeight(.8f),
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Employee Lookup"
                    )
                    Text("Employee Records", modifier = Modifier.fillMaxWidth(),
                        style = TextStyle(fontSize = 24.sp, textAlign = TextAlign.Center))
                }
            }
            IconButton(modifier = Modifier.weight(0.33f),
                onClick = {
                    navController.navigate(Screens.ProjectList.route)
                }) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Icon(modifier = Modifier.fillMaxWidth().fillMaxHeight(.8f),
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Project Management"
                    )
                    Text("*Coming Soon*", modifier = Modifier.fillMaxWidth(),
                        style = TextStyle(fontSize = 24.sp, textAlign = TextAlign.Center))
                }
            }
            IconButton(modifier = Modifier.weight(0.33f),
                onClick = { Log.i("test","3") }) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Icon(modifier = Modifier.fillMaxWidth().fillMaxHeight(.8f),
                        imageVector = Icons.Filled.Face,
                        contentDescription = "Customer Management"
                    )
                    Text("*Coming Soon*",
                        modifier = Modifier.fillMaxWidth(),
                        style = TextStyle(fontSize = 24.sp, textAlign = TextAlign.Center))
                }
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