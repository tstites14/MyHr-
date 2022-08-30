package com.tstites.myhr.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.Text
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
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
        var data = ArrayList<Employee>()
        runBlocking {
            data = ArrayList(setupDB(context))
        }

        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)) {
            items(data, itemContent = { emp ->
                val index = data.indexOf(emp)
                
                Row(modifier = Modifier.fillMaxWidth()) {
                    Card(
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(emp.name ?: "SAMPLE", style = TextStyle(
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        ), modifier = Modifier.padding(16.dp))
                    }
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