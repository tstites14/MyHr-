package com.tstites.myhr.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tstites.myhr.R
import com.tstites.myhr.db.AppDatabase
import com.tstites.myhr.db.DBConnection
import com.tstites.myhr.obj.Employee
import kotlinx.coroutines.runBlocking

class EmployeeList {

    @Composable
    fun EmployeeListLayout(navController: NavController) {
        val context = LocalContext.current
        val dbConnection = DBConnection().buildDB(context)

        val originalData = remember { mutableStateListOf<Employee>() }
        val data = remember { mutableStateListOf<Employee>() }
        runBlocking {
            if (originalData.isEmpty()) {
                originalData.addAll(setupDB(context,dbConnection))
                data.addAll(originalData)
            }
        }
        Column {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.93f)
            ) {
                items(data, itemContent = { emp ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Card(
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    navController.navigate(Screens.EmployeeDetails.route)
                                }) {
                            Column {
                                Text(
                                    emp.name ?: "SAMPLE", style = TextStyle(
                                        fontSize = 20.sp,
                                    ), modifier = Modifier.padding(8.dp)
                                )
                                Text(
                                    emp.jobTitle ?: "IT", style = TextStyle(
                                        fontSize = 16.sp,
                                        color = Color.DarkGray
                                    ), modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                })
            }
            BottomAppBar(backgroundColor = MaterialTheme.colors.primarySurface,
                contentColor = contentColorFor(backgroundColor),
                elevation = AppBarDefaults.BottomAppBarElevation,
                content = {
                    val filterAlert = remember { mutableStateOf(false) }
                    val searchAlert = remember { mutableStateOf(false) }

                    IconButton(onClick = {
                        filterAlert.value = true
                        searchAlert.value = false
                    }) {
                        Icon(
                            painterResource(id = R.drawable.ic_outline_filter),
                            "Filter search", tint = Color.White)
                    }
                    IconButton(onClick = {
                        filterAlert.value = false
                        searchAlert.value = true
                    }) {
                        Icon(Icons.Outlined.Search, "Search",
                            tint = Color.White)
                    }

                    //If the search button is pressed open the search dialog box
                    if (searchAlert.value) {
                        AlertDialog(onDismissRequest = {
                            filterAlert.value = false
                            searchAlert.value = false
                        }, buttons = {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally) {
                                val searchField = remember { mutableStateOf("")}

                                TextField(modifier = Modifier.padding(8.dp),
                                    placeholder = { Text("Search") },
                                    value = searchField.value,
                                    onValueChange = { searchField.value = it })
                                Button(modifier = Modifier.fillMaxWidth(),
                                    onClick =  {
                                        data.clear()
                                        data.addAll(originalData)

                                        val resultData = searchDB(searchField.value, data)

                                        if (resultData.isNotEmpty()) {
                                            data.clear()
                                            data.addAll(resultData)
                                        }

                                        searchAlert.value = false
                                    }) {
                                    Text("Search")
                                }
                                Button(modifier = Modifier.fillMaxWidth(),
                                    onClick =  {
                                        filterAlert.value = false
                                        searchAlert.value = false
                                }) {
                                    Text("Cancel")
                                }
                            }
                        })
                    //If the filter button is pressed open the filter dialog box
                    } else if (filterAlert.value) {
                        AlertDialog(onDismissRequest = {
                            filterAlert.value = false
                            searchAlert.value = false
                        }, buttons = {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally) {
                                val departmentField = remember { mutableStateOf("") }
                                val titleField = remember { mutableStateOf("") }

                                TextField(modifier = Modifier.padding(8.dp),
                                    placeholder = { Text("Job title") },
                                    value = titleField.value,
                                    onValueChange = { titleField.value = it },)
                                TextField(modifier = Modifier.padding(8.dp),
                                    placeholder = { Text("Department") },
                                    value = departmentField.value,
                                    onValueChange = { departmentField.value = it })
                                Button(modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        /*TODO*/
                                    }) {
                                    Text("Apply Filter")
                                }
                                Button(modifier = Modifier.fillMaxWidth(),
                                    onClick =  {
                                        filterAlert.value = false
                                        searchAlert.value = false
                                    }) {
                                    Text("Cancel")
                                }
                            }
                        })
                    }
                })
        }
    }

    @Preview
    @Composable
    fun EmployeeListLayoutPreview() {
        val navController = rememberNavController()

        EmployeeListLayout(navController = navController)
    }

    private fun searchDB(term: String, data: MutableList<Employee>): List<Employee> {
        val results = ArrayList<Employee>()

        data.forEach {
            //Check for phone extension match
            if (term.isDigitsOnly() && term.toInt() == it.phoneExtension) {
                results.add(it)
            } else if (it.name?.contains(term) == true) {
                results.add(it)
            } else if (it.department?.contains(term) == true) {
                results.add(it)
            } else if (it.jobTitle?.contains(term) == true) {
                results.add(it)
            }
        }

        return results.toList()
    }

    private fun filterDB(terms: List<String>, data: ArrayList<Employee>) {
        val results = ArrayList<Employee>()


    }

    private fun setupDB(context: Context, dbConnection: AppDatabase): List<Employee> {
        if (getEmployeeCount(context, dbConnection) == 0) {
            insertData(context, dbConnection)
        }

        val data: ArrayList<Employee> = ArrayList(pullEmployeeData(context, dbConnection))
        Log.i("TEST", data.toString())

        return data
    }

    private fun insertData(context: Context, dbConnection: AppDatabase) {
        val eDao = dbConnection.employeeDao()

        if (eDao.getTableEntries() == 0) {
            val e1: Employee = Employee(1, "John Doe", "12 Test Ave", "Orlando", "FL",
                "Information Technology", "Junior Software Engineer", 100)
            val e2: Employee = Employee(2, "James Phillips", "432 Sample Ave", "Kissimmee", "FL",
                "Information Technology", "Software Engineer", 101)
            //val e3: Employee
            //val e4: Employee
            //val e5: Employee

            eDao.insertNewEmployee(e1, e2)
        }
    }

    private fun getEmployeeCount(context: Context, dbConnection: AppDatabase): Int {
        val empDao = dbConnection.employeeDao()

        return empDao.getTableEntries()
    }

    private fun pullEmployeeData(context: Context, dbConnection: AppDatabase): List<Employee> {
        val empDao = dbConnection.employeeDao()

        return empDao.getAllEmployees()
    }
}