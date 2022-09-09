package com.tstites.myhr.ui.screens

import android.os.Bundle

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tstites.myhr.db.DBConnection
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
            employeeInfo?.getString("title") ?: "",
            employeeInfo?.getInt("extension") ?: 1)
        val dbConnection = DBConnection()
        val employeeDao = dbConnection.buildDB(LocalContext.current).employeeDao()

        //use mutableStateOf to force Compose to refresh itself when value changes
        val textFieldState = remember { mutableStateOf(false) }

        Column(modifier = Modifier.fillMaxSize()) {
            Card {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f), horizontalAlignment = Alignment.CenterHorizontally) {
                    elements.DefaultTextField(text = employee.name ?: "", placeholder = "Name",
                        "Name", modifier = Modifier.padding(8.dp), textFieldState.value) {
                        employee.name = it
                    }
                    elements.DefaultTextField(text = employee.address ?: "", placeholder = "Address",
                        "Address", modifier = Modifier.padding(8.dp), state = textFieldState.value) {
                        employee.address = it
                    }
                    elements.DefaultTextField(text = employee.city ?: "", placeholder = "City",
                        "City", modifier = Modifier.padding(8.dp), state = textFieldState.value) {
                        employee.city = it
                    }
                    elements.DefaultTextField(text = employee.state ?: "", placeholder = "State",
                        "State", modifier = Modifier.padding(8.dp), state = textFieldState.value) {
                        employee.state = it
                    }
                    elements.DefaultTextField(text = employee.department ?: "", placeholder = "Department",
                        "Department", modifier = Modifier.padding(8.dp), state = textFieldState.value) {
                        employee.department = it
                    }
                    elements.DefaultTextField(text = employee.jobTitle ?: "", placeholder = "Job Title",
                        "Job Title*", modifier = Modifier.padding(8.dp), state = textFieldState.value) {
                        employee.jobTitle = it
                    }
                    elements.DefaultTextField(text = employee.phoneExtension.toString(), placeholder = "Ext.",
                        "Ext.", modifier = Modifier.padding(8.dp), state = textFieldState.value) {
                        employee.phoneExtension = it.toInt()
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                val editButtonText = remember { mutableStateOf("Edit")}
                val deleteButtonPressed = remember { mutableStateOf(false) }

                if (deleteButtonPressed.value) {
                    AlertDialog(onDismissRequest = { deleteButtonPressed.value = false }, buttons = {
                        Column {
                            Text("Are you sure you would like to delete this entry?",
                                modifier = Modifier.padding(24.dp), textAlign = TextAlign.Center)

                            Row {
                                Button(onClick = {
                                    employeeDao.deleteEmployee(employee)
                                    navController.navigate(Screens.EmployeeList.route)

                                    deleteButtonPressed.value = false
                                }, modifier = Modifier.fillMaxWidth(0.5f).padding(8.dp)) {
                                    Text("Confirm")
                                }
                                Button(onClick = {
                                    deleteButtonPressed.value = false
                                }, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                                    Text("Dismiss")
                                }
                            }
                        }
                    })
                }

                elements.DefaultButton(text = editButtonText.value,
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .weight(0.5f)) {
                    textFieldState.value = true

                    if (editButtonText.value == "Save") {
                        employeeDao.updateExistingEmployee(employee)
                        navController.navigate(Screens.EmployeeList.route)
                    }
                    editButtonText.value = "Save"
                }
                elements.DefaultButton(text = "Delete Entry",
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .weight(0.5f)) {
                    deleteButtonPressed.value = true
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