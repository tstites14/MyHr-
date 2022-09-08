package com.tstites.myhr.ui.screens

import android.os.Bundle
import android.util.Log
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
import com.tstites.myhr.obj.Employee
import com.tstites.myhr.ui.components.CommonElements

class EmployeeDetails {

    @Composable
    fun EmployeeDetailsLayout(navController: NavController, employeeInfo: Bundle?) {
        val elements = CommonElements()
        val employee = Employee(
            employeeInfo?.getInt("id") ?: 0,
            employeeInfo?.getString("name") ?: "",
            employeeInfo?.getString("address") ?: "",
            employeeInfo?.getString("city") ?: "",
            employeeInfo?.getString("state") ?: "",
            employeeInfo?.getString("department") ?: "",
            "",
            employeeInfo?.getInt("extension") ?: 1)

        //use mutableStateOf to force Compose to refresh itself when value changes
        val textFieldState = remember { mutableStateOf(false) }

        Column(modifier = Modifier.fillMaxSize()) {
            Card {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f), horizontalAlignment = Alignment.CenterHorizontally) {
                    elements.DefaultTextField(text = employee.name ?: "", placeholder = "Name",
                        modifier = Modifier.padding(12.dp), textFieldState.value) {
                        employee.name = it
                    }
                    elements.DefaultTextField(text = employee.address ?: "", placeholder = "Address",
                        modifier = Modifier.padding(12.dp), state = textFieldState.value) {
                        employee.address = it
                    }
                    elements.DefaultTextField(text = employee.city ?: "", placeholder = "City",
                        modifier = Modifier.padding(12.dp), state = textFieldState.value) {
                        employee.city = it
                    }
                    elements.DefaultTextField(text = employee.state ?: "", placeholder = "State",
                        modifier = Modifier.padding(12.dp), state = textFieldState.value) {
                        employee.state = it
                    }
                    elements.DefaultTextField(text = employee.department ?: "", placeholder = "Department",
                        modifier = Modifier.padding(12.dp), state = textFieldState.value) {
                        employee.department = it
                    }
                    elements.DefaultTextField(text = employee.phoneExtension.toString(), placeholder = "Ext.",
                        modifier = Modifier.padding(12.dp), state = textFieldState.value) {
                        employee.phoneExtension = it.toInt()
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                val editButtonText = remember { mutableStateOf("Edit")}

                elements.DefaultButton(text = editButtonText.value,
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .weight(0.5f)) {
                    textFieldState.value = true

                    if (editButtonText.value == "Save") {
                        /*TODO*/
                    }
                    editButtonText.value = "Save"
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
        
        EmployeeDetailsLayout(navController = navController, null)
    }
}