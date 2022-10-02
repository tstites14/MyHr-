package com.tstites.myhr.ui.screens

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tstites.myhr.db.DBConnection
import com.tstites.myhr.obj.Customer
import com.tstites.myhr.obj.CustomerProjectDao
import com.tstites.myhr.obj.Project
import com.tstites.myhr.ui.components.CommonElements

class CustomerDetails {
    //Contains the tab names
    enum class Rows {
        Details,
        Projects
    }

    @Composable
    fun CustomerDetailsLayout(navController: NavController, info: Bundle?) {
        //Tab information
        val currentTab = remember { mutableStateOf(Rows.Details) }
        val rowTitles = Rows.values().map { it.name }

        val commonElements = CommonElements()
        val context = LocalContext.current

        //Database connection and DAOs
        val dbConnection = DBConnection().buildDB(LocalContext.current)
        val cDao = dbConnection.customerDao()
        val cpDao = dbConnection.customerProjectDao()
        val pDao = dbConnection.projectDao()

        //data objects
        val data = cDao.selectById(info?.getInt("customerID") ?: 1)

        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.93f)) {
            TabRow(selectedTabIndex = currentTab.value.ordinal,
                modifier = Modifier.fillMaxWidth()) {
                rowTitles.forEachIndexed { index, title ->
                    Tab(text = { Text(title) },
                        selected = currentTab.value.ordinal == index,
                        onClick = { currentTab.value = Rows.values()[index] })
                }
            }

            when(currentTab.value) {
                Rows.Details -> cDao.updateExisting(customerInfo(commonElements, data, context))
                Rows.Projects ->  {
                    val projects = customerProjects(commonElements, data.id, cpDao).toTypedArray()
                    pDao.updateExistingProject(*projects)
                }
            }
        }
    }

    @Composable
    fun customerInfo(common: CommonElements, customer: Customer, context: Context): Customer {
        val textBoxState = remember { mutableStateOf(false) }
        val alertActive = remember { mutableStateOf(false) }

        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.93f)) {
            Card(modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                    common.DefaultTextField(
                            text = customer.name ?: "",
                            placeholder = "Name",
                            title = "Name",
                            modifier = Modifier.padding(8.dp),
                            state = textBoxState.value) {
                        customer.name = it
                    }
                    common.DefaultButton(text = "Contact Info", modifier = Modifier.padding(8.dp)) {
                        alertActive.value = true
                    }
                }
            }
        }

        if (alertActive.value) {
            AlertDialog(onDismissRequest = { alertActive.value = false },
                buttons = {
                    Column(modifier = Modifier.padding(8.dp),
                           horizontalAlignment = Alignment.CenterHorizontally) {
                        common.DefaultButton(text = "Email", modifier = Modifier
                                                                        .padding(8.dp)
                                                                        .fillMaxWidth()) {
                            //Close alert before the email app opens
                            alertActive.value = false

                            try {
                                val email = customer.email

                                val intent = Intent(Intent.ACTION_SEND)
                                //The type "message/rfc822" denotes an email
                                intent.type = "message/rfc822"
                                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))

                                context.startActivity(intent)
                            } catch (e: ActivityNotFoundException) {
                                //No email app is available on this device
                                Toast.makeText(context, "No email apps installed", Toast.LENGTH_LONG).show()
                            }

                        }
                        common.DefaultButton(text = "Phone", modifier = Modifier
                                                                        .padding(8.dp)
                                                                        .fillMaxWidth()) {
                            val phone = customer.phone

                            val intent = Intent(Intent.ACTION_DIAL)
                            intent.data = Uri.parse("tel:$phone")

                            context.startActivity(intent)
                        }
                    }
                })
        }

        return customer
    }

    @Composable
    fun customerProjects(common: CommonElements, customerID: Int,
                         customerProjectDao: CustomerProjectDao): List<Project> {
        val projectList = customerProjectDao.selectProjectsByCustomerID(customerID)

        Column(modifier = Modifier.padding(12.dp)) {
            Text("Hello, ${projectList[0].title}")
        }

        return projectList
    }

    @Preview
    @Composable
    fun CustomerDetailsLayoutPreview() {
        CustomerDetailsLayout(navController = rememberNavController(), null)
    }
}