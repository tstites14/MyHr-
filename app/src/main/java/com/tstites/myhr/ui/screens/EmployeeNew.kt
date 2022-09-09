package com.tstites.myhr.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tstites.myhr.db.DBConnection
import com.tstites.myhr.obj.Employee
import com.tstites.myhr.ui.components.CommonElements

class EmployeeNew {

    @Composable
    fun EmployeeNewLayout(navController: NavController) {
        val elements = CommonElements()
        val context = LocalContext.current

        val name = remember { mutableStateOf("")}
        val address = remember { mutableStateOf("")}
        val city = remember { mutableStateOf("")}
        val state = remember { mutableStateOf("")}
        val department = remember { mutableStateOf("")}
        val jobTitle = remember { mutableStateOf("")}
        val extension = remember { mutableStateOf(0)}

        Column(modifier = Modifier.fillMaxSize()) {
            Card {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.85f)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    elements.DefaultTextField(text = name.value, placeholder = "Name",
                        "Name*", modifier = Modifier.padding(8.dp), true) {
                        name.value = it
                    }
                    elements.DefaultTextField(text = address.value, placeholder = "Address",
                        "Address*", modifier = Modifier.padding(8.dp), state = true) {
                        address.value = it
                    }
                    elements.DefaultTextField(text = city.value, placeholder = "City",
                        "City*", modifier = Modifier.padding(8.dp), state = true) {
                        city.value = it
                    }
                    elements.DefaultTextField(text = state.value, placeholder = "State",
                        "State*", modifier = Modifier.padding(8.dp), state = true) {
                        state.value = it
                    }
                    elements.DefaultTextField(text = department.value, placeholder = "Department",
                        "Department*", modifier = Modifier.padding(8.dp), state = true) {
                        department.value = it
                    }
                    elements.DefaultTextField(text = jobTitle.value, placeholder = "Job Title",
                        "Job Title*", modifier = Modifier.padding(8.dp), state = true) {
                        jobTitle.value = it
                    }
                    elements.DefaultTextFieldNum(text = extension.value.toString(), placeholder = "Ext.",
                        "Ext.*", modifier = Modifier.padding(8.dp), state = true) {
                        extension.value = it.toInt()
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                elements.DefaultButton(text = "Save", modifier = Modifier
                    .padding(8.dp)
                    .weight(0.5f)) {
                    val employee = Employee(
                        0, name.value, address.value, city.value, state.value, department.value,
                        jobTitle.value, extension.value)

                    val empDao = DBConnection().buildDB(context).employeeDao()
                    empDao.insertNewEmployee(employee)
                    navController.navigate(Screens.EmployeeList.route)
                }
                elements.DefaultButton(text = "Discard", modifier = Modifier
                    .padding(8.dp)
                    .weight(0.5f)) {
                    navController.navigate(Screens.EmployeeList.route)
                }
            }
        }
    }

    @Preview
    @Composable
    fun EmployeeNewLayoutPreview() {
        val navController = rememberNavController()
        EmployeeNewLayout(navController)
    }
}