package com.tstites.myhr.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.tstites.myhr.obj.EmployeeDao
import kotlinx.coroutines.runBlocking

class EmployeeList {

    @Composable
    fun EmployeeListLayout(navController: NavController) {
        val context = LocalContext.current
        val empDao = DBConnection().buildDB(context).employeeDao()

        //Keep a copy of the original data so the database does not have to be read more than once
        val originalData = remember { mutableStateListOf<Employee>() }
        val data = remember { mutableStateListOf<Employee>() }
        runBlocking {
            if (originalData.isEmpty()) {
                originalData.addAll(setupDB(empDao))
                data.addAll(originalData)
            }
        }
        Column {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.93f)
            ) {
                //Perform the following operation on each item in the table
                items(data, itemContent = { emp ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        //A Card is essentially a fancy list item that contains its own UI
                        Card(
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    //When the employee is tapped, navigate to the details screen
                                    //using the data from the database
                                    navController.navigate(
                                        Screens.EmployeeDetails.route +
                                        "/${emp.id}/${emp.name}/${emp.address}/${emp.city}/${emp.state}/${emp.department}/${emp.jobTitle}/${emp.phoneExtension}"
                                    )
                                }) {
                            //This is the actual content of the Card
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
            //Underneath the list is the bar that contains the searching, filtering, and adding buttons
            BottomAppBar(backgroundColor = MaterialTheme.colors.primarySurface,
                contentColor = contentColorFor(backgroundColor),
                elevation = AppBarDefaults.BottomAppBarElevation,
                content = {
                    /*This is where the buttons and other content lives*/

                    //These determine whether the buttons are pressed. This is required because
                    //these two buttons open a separate dialog when pressed
                    val filterAlert = remember { mutableStateOf(false) }
                    val searchAlert = remember { mutableStateOf(false) }

                    //A refresh button is important because the data (but not the original data) is
                    //permanently modified when searching/filtering
                    BottomNavigationItem(icon = { Icon(Icons.Outlined.Refresh,
                        "Refresh Data", tint = Color.White) },
                        onClick = {
                            //Replace the modified data with the copy of the original data
                            data.clear()
                            data.addAll(originalData)
                    }, selected = false)
                    BottomNavigationItem(icon = { Icon(Icons.Outlined.Add,
                        "Add New Employee", tint = Color.White) }, onClick = {
                         navController.navigate(Screens.EmployeeNew.route)
                    }, selected = false)
                    BottomNavigationItem(icon = { Icon(Icons.Outlined.Search,
                        "Search", tint = Color.White) }, onClick = {
                        //On clicking, the appropriate boolean is set, and this triggers the dialog
                        //box to open
                        filterAlert.value = false
                        searchAlert.value = true
                    }, selected = false)
                    BottomNavigationItem(icon = { Icon(
                        painterResource(id = R.drawable.ic_outline_filter),
                        "Filter search", tint = Color.White) }, onClick = {
                        filterAlert.value = true
                        searchAlert.value = false
                    }, selected = false)

                    //If the search button is pressed open the search dialog box
                    if (searchAlert.value) {
                        AlertDialog(onDismissRequest = {
                            filterAlert.value = false
                            searchAlert.value = false
                        }, buttons = {
                            /*The "buttons" parameter is where the UI for the alert box goes. It
                            * does not have to only be buttons*/
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally) {
                                val searchField = remember { mutableStateOf("")}

                                //Reset data to original state before starting a new search
                                data.clear()
                                data.addAll(originalData)

                                TextField(modifier = Modifier.padding(8.dp),
                                    placeholder = { Text("Search") },
                                    value = searchField.value,
                                    onValueChange = { searchField.value = it })
                                Button(modifier = Modifier.fillMaxWidth(),
                                    onClick =  {
                                        val resultData = searchDB(searchField.value, data)

                                        if (resultData.isNotEmpty()) {
                                            data.clear()
                                            data.addAll(resultData)
                                        }

                                        //Set the value to false again upon exit to prevent the app
                                        //from getting confused and possibly crashing
                                        searchAlert.value = false
                                    }) {
                                    Text("Search")
                                }
                                Button(modifier = Modifier.fillMaxWidth(),
                                    onClick =  {
                                        //Does nothing but close the dialog without changing anything
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

                                //Reset data to original state before starting a new search
                                data.clear()
                                data.addAll(originalData)

                                //Job title textbox
                                TextField(modifier = Modifier.padding(8.dp),
                                    placeholder = { Text("Job title") },
                                    value = titleField.value,
                                    onValueChange = { titleField.value = it },)

                                //Department textbox
                                TextField(modifier = Modifier.padding(8.dp),
                                    placeholder = { Text("Department") },
                                    value = departmentField.value,
                                    onValueChange = { departmentField.value = it })
                                Button(modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        val resultData = filterDB(
                                            listOf(departmentField.value, titleField.value), data)

                                        if (resultData.isNotEmpty()) {
                                            data.clear()
                                            data.addAll(resultData)
                                        }

                                        filterAlert.value = false
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
            //The search term must not be empty/null or the app will crash
            if (term.isNotEmpty()) {
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
        }

        //Only return a list if there are results to prevent the UI from disappearing when no results are returned
        return if (results.isNotEmpty())
            results.toList()
        else
            emptyList()
    }

    private fun filterDB(terms: List<String>, data: MutableList<Employee>): List<Employee> {
        val results = ArrayList<Employee>()

        data.forEach { emp ->
            terms.forEach { term ->
                if (term.isNotEmpty()) {
                    //Make sure that the found employee isn't already in the results
                    if (emp.department?.contains(term) == true && results.find { emp.id == it.id } == null) {
                        results.add(emp)
                    } else if (emp.jobTitle?.contains(term) == true && results.find { emp.id == it.id } == null) {
                        results.add(emp)
                    }
                }
            }
        }

        return if (results.isNotEmpty())
            results.toList()
        else
            emptyList()
    }

    private fun setupDB(empDao: EmployeeDao): List<Employee> {
        //Only insert sample data if the database does not have any entries already
        if (getEmployeeCount(empDao) == 0) {
            insertData(empDao)
        }

        return ArrayList(pullEmployeeData(empDao))
    }

    private fun insertData(empDao: EmployeeDao) {
        if (empDao.getTableEntries() == 0) {
            val e1 = Employee(1, "John Doe", "12 Test Ave", "Orlando", "FL",
                "Information Technology", "Junior Software Engineer", 100)
            val e2 = Employee(2, "James Phillips", "432 Sample Ave", "Kissimmee", "FL",
                "Information Technology", "Software Engineer", 101)
            val e3 = Employee(3, "Sophia Wright", "641 QA Lane", "Orlando", "FL",
                "Marketing", "Junior Social Media Manager", 102)

            empDao.insertNewEmployee(e1, e2, e3)
        }
    }

    private fun getEmployeeCount(empDao: EmployeeDao): Int {
        return empDao.getTableEntries()
    }

    private fun pullEmployeeData(empDao: EmployeeDao): List<Employee> {
        return empDao.getAllEmployees()
    }
}