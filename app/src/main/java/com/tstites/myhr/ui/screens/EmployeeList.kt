package com.tstites.myhr.ui.screens

import android.content.Context
import android.util.Log
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tstites.myhr.db.DBConnection
import com.tstites.myhr.obj.Employee
import kotlinx.coroutines.runBlocking

class EmployeeList {

    @Composable
    fun EmployeeListLayout(navController: NavController) {
        val context = LocalContext.current
        var data: ArrayList<Employee>
        runBlocking {
            data = ArrayList(setupDB(context))
        }
        Column {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.93f)
            ) {
                items(data, itemContent = { emp ->
                    val index = data.indexOf(emp)

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Card(
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
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
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Outlined.Settings, "Filter search",
                            tint = Color.White)
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Outlined.Search, "Search",
                            tint = Color.White)
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

    private fun setupDB(context: Context): List<Employee> {
        var data = ArrayList<Employee>()
        /*CoroutineScope(IO).launch {
            data = ArrayList(pullEmployeeData(context))
        }*/
        data = ArrayList(pullEmployeeData(context))
        Log.i("TEST", data.toString())

        return data
    }

    fun insertData(context: Context) {
        val eTable = DBConnection().buildDB(context)
        val eDao = eTable.employeeDao()

        val e1: Employee = Employee(1, "John Doe", "12 Test Ave", "Orlando", "FL",
            "Information Technology", "Junior Software Engineer", 100)
        val e2: Employee = Employee(2, "James Phillips", "432 Sample Ave", "Kissimmee", "FL",
            "Information Technology", "Software Engineer", 101)
        //val e3: Employee
        //val e4: Employee
        //val e5: Employee

        eDao.insertNewEmployee(e1, e2)
    }

    fun pullEmployeeData(context: Context): List<Employee> {
        val employeeTable = DBConnection().buildDB(context)
        val empDao = employeeTable.employeeDao()

        return empDao.getAllEmployees()
    }
}