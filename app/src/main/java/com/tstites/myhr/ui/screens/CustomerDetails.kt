package com.tstites.myhr.ui.screens

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tstites.myhr.db.DBConnection
import com.tstites.myhr.obj.Customer
import com.tstites.myhr.obj.CustomerDao
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
                Rows.Details ->  {
                    if (data != null) {
                        val customer = customerInfo(commonElements, data, context, cDao, navController)
                        cDao.updateExisting(customer)
                    }
                }
                Rows.Projects ->  {
                    val projects = customerProjects(data.id, cpDao, navController).toTypedArray()
                    pDao.updateExistingProject(*projects)
                }
            }
        }
    }

    @Composable
    fun customerInfo(common: CommonElements, customer: Customer, context: Context, cDao: CustomerDao,
                     navController: NavController): Customer {
        val textBoxState = remember { mutableStateOf(false) }
        val alertActive = remember { mutableStateOf(false) }

        Column(modifier = Modifier
            .fillMaxSize()) {
            Card(modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.85f)) {
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
                    common.DefaultTextField(
                            text = customer.address ?: "",
                            placeholder = "Address",
                            title = "Address",
                            modifier = Modifier.padding(8.dp),
                            state = textBoxState.value) {
                        customer.address = it
                    }
                    common.DefaultTextField(
                        text = customer.city ?: "",
                        placeholder = "City",
                        title = "City",
                        modifier = Modifier.padding(8.dp),
                        state = textBoxState.value) {
                        customer.city = it
                    }
                    common.DefaultTextField(
                        text = customer.state ?: "",
                        placeholder = "State",
                        title = "State",
                        modifier = Modifier.padding(8.dp),
                        state = textBoxState.value) {
                        customer.state = it
                    }

                    common.DefaultButton(text = "Contact Info", modifier = Modifier.padding(8.dp)) {
                        alertActive.value = true
                    }
                }
            }

            Row(modifier = Modifier
                .fillMaxWidth()) {
                val editButtonText = remember{ mutableStateOf("Edit") }
                val deleteButtonPressed = remember { mutableStateOf(false) }

                if (textBoxState.value) {
                    editButtonText.value = "Save"
                }

                common.DefaultButton(text = editButtonText.value, modifier = Modifier
                    .padding(12.dp)
                    .weight(0.5f)) {
                    if (editButtonText.value.lowercase() == "save") {
                        cDao.updateExisting(customer)

                        navController.navigate(Screens.CustomerList.route)
                    }

                    textBoxState.value = true
                }
                common.DefaultButton(text = "Delete Entry", modifier = Modifier
                    .padding(12.dp)
                    .weight(0.5f)) {
                    deleteButtonPressed.value = true
                }

                if (deleteButtonPressed.value) {
                    common.defaultConfirmationAlert {
                        cDao.deleteExisting(customer)
                        navController.navigate(Screens.CustomerList.route)
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
    fun customerProjects(customerID: Int,
                         customerProjectDao: CustomerProjectDao, navController: NavController): List<Project> {
        val projectList = customerProjectDao.selectProjectsByCustomerID(customerID)

        Column(modifier = Modifier.padding(12.dp)) {
            projectList.forEach { project ->
                Card(shape = RoundedCornerShape(4.dp), modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        navController.navigate(Screens.ProjectDetails.route + "/${project.id}")
                    }) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(project.title ?: "",
                            modifier = Modifier.padding(8.dp),
                            style = TextStyle(fontSize = 24.sp))

                        LinearProgressIndicator(progress = project.progress,
                            modifier = Modifier
                                .padding(16.dp)
                                .height(16.dp)
                                .fillMaxWidth())
                    }
                }
            }
        }

        return projectList
    }

    @Preview
    @Composable
    fun CustomerDetailsLayoutPreview() {
        CustomerDetailsLayout(navController = rememberNavController(), null)
    }
}