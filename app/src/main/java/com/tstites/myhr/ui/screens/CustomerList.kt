package com.tstites.myhr.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tstites.myhr.db.DBConnection
import com.tstites.myhr.obj.*
import com.tstites.myhr.ui.components.DBInitialization
import kotlinx.coroutines.runBlocking

class CustomerList {
    @Composable
    fun CustomerListLayout(navController: NavController) {
        val context = LocalContext.current

        //Database connection info/DAOs
        val dbConnection = DBConnection().buildDB(context)
        val dbInit = DBInitialization()

        val eDao: EmployeeDao = dbConnection.employeeDao()
        val pDao: ProjectDao = dbConnection.projectDao()
        val peDao: ProjectEmployeeDao = dbConnection.projectEmployeeDao()
        val cDao: CustomerDao = dbConnection.customerDao()
        val cpDao: CustomerProjectDao = dbConnection.customerProjectDao()

        //Database data
        val originalData = remember { mutableStateListOf<Customer>() }
        val data = remember { mutableStateListOf<Customer>() }

        runBlocking {
            if (originalData.isEmpty()) {
                originalData.addAll(setupDB(dbInit, eDao, pDao, peDao, cDao, cpDao))
                data.addAll(originalData)
            }
        }

        Column(modifier = Modifier.fillMaxSize(),
               verticalArrangement = Arrangement.Center) {
            LazyColumn(modifier = Modifier
                       .fillMaxWidth()
                       .fillMaxHeight(0.93f)) {
                items(data, itemContent = { customer ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Card(shape = RoundedCornerShape(4.dp),
                             modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                        .clickable {
                                            navController.navigate(Screens.CustomerDetails.route + "/${customer.id}")
                                        }) {
                            Column {
                                Text(
                                    customer.name ?: "SAMPLE", style = TextStyle(
                                        fontSize = 20.sp,
                                    ), modifier = Modifier.padding(8.dp)
                                )
                                Text(
                                    "${customer.city}, ${customer.state}", style = TextStyle(
                                        fontSize = 16.sp,
                                        color = Color.DarkGray
                                    ), modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                })
            }
        }
    }

    @Preview
    @Composable
    fun CustomerListLayoutPreview() {
        CustomerListLayout(navController = rememberNavController())
    }

    //Refactored insertData and the contents of setupDB into their own class and methods
    private fun setupDB(dbInit: DBInitialization,
                        ed: EmployeeDao,
                        pd: ProjectDao,
                        ped: ProjectEmployeeDao,
                        cd: CustomerDao,
                        cpd: CustomerProjectDao): List<Customer> {
        dbInit.init(ed, pd, ped, cd, cpd)

        return ArrayList(cd.selectAll())
    }


}