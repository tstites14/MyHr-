package com.tstites.myhr.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tstites.myhr.ui.components.CommonElements

class EmployeeDetails {

    @Composable
    fun EmployeeDetailsLayout(navController: NavController) {
        val elements = CommonElements()

        //use mutableStateOf to force Compose to refresh itself when value changes
        val textFieldState = remember { mutableStateOf(false) }

        Column(modifier = Modifier.fillMaxSize()) {
            Card {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f), horizontalAlignment = Alignment.CenterHorizontally) {
                    elements.DefaultTextField(text = "", placeholder = "Name",
                        modifier = Modifier.padding(12.dp), textFieldState.value)
                    elements.DefaultTextField(text = "", placeholder = "Address",
                        modifier = Modifier.padding(12.dp), state = textFieldState.value)
                    elements.DefaultTextField(text = "", placeholder = "City",
                        modifier = Modifier.padding(12.dp), state = textFieldState.value)
                    elements.DefaultTextField(text = "", placeholder = "State",
                        modifier = Modifier.padding(12.dp), state = textFieldState.value)
                    elements.DefaultTextField(text = "", placeholder = "Department",
                        modifier = Modifier.padding(12.dp), state = textFieldState.value)
                    elements.DefaultTextField(text = "", placeholder = "Ext.",
                        modifier = Modifier.padding(12.dp), state = textFieldState.value)
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                
                elements.DefaultButton(text = "Edit",
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .weight(0.5f)) {
                    textFieldState.value = true
                }
                elements.DefaultButton(text = "Delete Entry",
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .weight(0.5f)) {

                }
            }
        }
    }
    
    @Preview
    @Composable
    fun EmployeeDetailsLayoutPreview() {
        val navController = rememberNavController()
        
        EmployeeDetailsLayout(navController = navController)
    }
}