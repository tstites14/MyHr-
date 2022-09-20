package com.tstites.myhr.ui.screens

import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tstites.myhr.db.DBConnection
import com.tstites.myhr.obj.*
import com.tstites.myhr.ui.components.CommonElements
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import kotlin.math.roundToInt

class ProjectDetails {
    @Composable
    fun ProjectDetailsLayout(navController: NavController, projectInfo: Bundle?) {
        //All the default UI elements to be inserted
        val elements = CommonElements()
        val textFieldState = remember { mutableStateOf(false) }

        val project = Project(
            projectInfo?.getInt("id") ?: 0,
            projectInfo?.getString("title") ?: "",
            projectInfo?.getFloat("progress") ?: 0f
        )

        //Keep a list of current employees working on the project
        val data = remember { mutableStateListOf<ProjectEmployee>() }

        //Contains all the DAO objects used by the page
        val dbConnection = DBConnection().buildDB(LocalContext.current)
        val projectDao = dbConnection.projectDao()
        val projectEmployeeDao = dbConnection.projectEmployeeDao()
        val employeeDao = dbConnection.employeeDao()


        runBlocking {
            //Insert data into the database if the table is empty
            if (projectEmployeeDao.getTableEntries() == 0)
                insertData(projectEmployeeDao, employeeDao)
            if (data.isEmpty())
                data.addAll(projectEmployeeDao.getEmployeeIDsByProject(project.id))
        }

        Column(modifier = Modifier.fillMaxSize()) {
            //This Card contains the title and average progress
            Card (modifier = Modifier
                .weight(0.35f)
                .padding(12.dp)) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    //Title text box
                    elements.DefaultTextField(text = project.title ?: "", placeholder = "Title",
                        "Title", modifier = Modifier.padding(8.dp), textFieldState.value) {
                        project.title = it
                    }

                    //Spacer to separate title textbox from progress
                    Spacer(modifier = Modifier.padding(12.dp))

                    Text("Current progress:")
                    LinearProgressIndicator(progress = project.progress,
                        modifier = Modifier
                            .padding(16.dp)
                            .height(16.dp)
                            .fillMaxWidth())
                    //Shows the percentage as a number rounded to the nearest percent
                    Text("${(project.progress * 100).roundToInt()}%")
                }
            }

            //This Card contains the individual employees and their progresses
            Card(modifier = Modifier
                .weight(0.5f)
                .padding(12.dp)) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    //Get data to show in list
                    val employees: List<Employee> = dataToEmployees(data, employeeDao)

                    //Start the employee list
                    for (i in 0 until employees.count()) {
                        EmployeeListItem(employee = employees[i], projectEmployeeDao,
                            progress = data[i].currentProgress, project, navController)
                    }

                    //Add a button to add a new employee to the list
                    val addPressed = remember { mutableStateOf(false) }
                    val noErrors = remember { mutableStateOf(true) }
                    val searchName = remember { mutableStateOf("") }
                    val errorMessage = remember { mutableStateOf("") }
                    val errorColor = remember { mutableStateOf(White)}

                    if (addPressed.value) {
                        AlertDialog(modifier = Modifier.height(175.dp),
                            onDismissRequest = {
                                addPressed.value = false
                                noErrors.value = true
                                errorColor.value = White
                                searchName.value = ""
                                errorMessage.value = ""
                            }, buttons = {
                                Column(modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally) {
                                    elements.DefaultTextField(
                                        text = searchName.value,
                                        placeholder = "Name",
                                        title = "Name",
                                        modifier = Modifier.padding(8.dp),
                                        state = true) {
                                        searchName.value = it
                                    }

                                    //Error text when the name is not found
                                    Text(errorMessage.value,
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        color = errorColor.value,
                                        style = TextStyle(fontSize = 16.sp))

                                    if (!noErrors.value) {
                                        errorColor.value = Red
                                    }

                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        Button(onClick = {
                                            try {
                                                val employeeFound = employeeDao.getEmployeeByName(searchName.value)

                                                /*Technically Employee is a non-nullable type
                                                * but it can absolutely be null if this function returns
                                                * nothing. I am not sure why this happens*/
                                                if (employeeFound == null) {
                                                    noErrors.value = false
                                                    errorMessage.value = "This employee does not exist. Please try again."
                                                } else if (data.any { it.employeeID == employeeFound.id }) {
                                                    noErrors.value = false
                                                    errorMessage.value = "This employee is already working on this project." +
                                                            " Please try again."
                                                } else {
                                                    val projectEmployee = ProjectEmployee(employeeFound.id, project.id, 0f)
                                                    data.add(projectEmployee)
                                                    projectEmployeeDao.insertNewProjectEmployee(projectEmployee)

                                                    //Close the window and return all values to their defaults
                                                    addPressed.value = false
                                                    noErrors.value = true
                                                    errorColor.value = White
                                                    searchName.value = ""
                                                    errorMessage.value = ""
                                                }
                                            } catch (e: Exception) {
                                                noErrors.value = false
                                            }
                                        }, modifier = Modifier
                                            .fillMaxWidth(0.5f)
                                            .padding(8.dp)) {
                                            Text("Confirm")
                                        }

                                        Button(onClick = {
                                            addPressed.value = false
                                            noErrors.value = true
                                            searchName.value = ""
                                            errorColor.value = White
                                        }, modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)) {
                                            Text("Dismiss")
                                        }
                                    }
                                }
                            }
                        )
                    }

                    IconButton(onClick = {
                            addPressed.value = true
                        }, modifier = Modifier
                        .padding(2.dp)) {
                        Icon(Icons.Rounded.AddCircle, "Add new employees to the project",
                            modifier = Modifier
                                .size(64.dp)
                                .fillMaxWidth())
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                //Store whether the button has been pressed and the state of the edit button
                //since it also is the save button
                val editButtonText = remember { mutableStateOf("Edit")}
                val deleteButtonPressed = remember { mutableStateOf(false) }

                elements.DefaultButton(text = editButtonText.value,
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(0.5f)) {
                    //Enable the textboxes to allow them to be modified
                    textFieldState.value = true

                    //Perform the save function if the button has been pressed before
                    if (editButtonText.value == "Save") {
                        //TODO
                    }
                    editButtonText.value = "Save"
                }
                elements.DefaultButton(text = "Delete Project",
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(0.5f)) {
                    deleteButtonPressed.value = true
                }

                if (deleteButtonPressed.value) {
                    AlertDialog(onDismissRequest = { deleteButtonPressed.value = false }, buttons = {
                        Column {
                            Text("Are you sure you would like to delete this entry?",
                                modifier = Modifier.padding(24.dp), textAlign = TextAlign.Center)

                            Row {
                                Button(onClick = {
                                    projectDao.deleteProject(project)
                                    navController.navigate(Screens.ProjectList.route)
                                }, modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .padding(8.dp)) {
                                    Text("Confirm")
                                }

                                Button(onClick = {
                                    deleteButtonPressed.value = false
                                }, modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)) {
                                    Text("Dismiss")
                                }
                            }
                        }
                    })
                }
            }
        }
    }

    @Composable
    fun EmployeeListItem(employee: Employee, dao: ProjectEmployeeDao, progress: Float, project: Project, navController: NavController) {
        Row(modifier = Modifier
            .height(60.dp)
            .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(employee.name ?: "Unknown Employee",
                modifier = Modifier
                    .padding(4.dp)
                    .drawWithContent { drawContent() }
                    .weight(0.5f),
                style = TextStyle(fontSize = 20.sp))

            LinearProgressIndicator(progress = progress, modifier = Modifier
                .weight(0.4f)
                .height(12.dp)
                .padding(end = 16.dp))

            IconButton(onClick = {
                    dao.deleteProjectEmployeeByID(employee.id, project.id)

                    navController.navigate(Screens.ProjectDetails.route +
                        "/${project.id}/${project.title}/${project.progress}/")
                },
                modifier = Modifier
                    .drawWithContent { drawContent() }
                    .padding(4.dp)
                    .weight(0.1f)) {
                Icon(Icons.Rounded.Delete, "Remove employee from project")
            }
        }
    }

    private fun dataToEmployees(data: List<ProjectEmployee>,
                                            dao: EmployeeDao): List<Employee> {
        val employees: ArrayList<Employee> = arrayListOf()

        for (projectEmployee in data) {
            employees.add(dao.getEmployeeById(projectEmployee.employeeID))
        }

        return employees.toList().ifEmpty { emptyList() }
    }

    //Insert data into the ProjectEmployee table
    private fun insertData(projectEmployeeDao: ProjectEmployeeDao, employeeDao: EmployeeDao) {
        if (projectEmployeeDao.getTableEntries() == 0) {
            val pe1 = ProjectEmployee(1, 1, 0.33f)
            val pe2 = ProjectEmployee(2, 1, 0.5f)
            val pe3 = ProjectEmployee(1, 2, 0f)

            projectEmployeeDao.insertNewProjectEmployee(pe1, pe2, pe3)
        }

        if (employeeDao.getTableEntries() == 0) {
            val e1 = Employee(1, "John Doe", "12 Test Ave", "Orlando", "FL",
                "Information Technology", "Junior Software Engineer", 100)
            val e2 = Employee(2, "James Phillips", "432 Sample Ave", "Kissimmee", "FL",
                "Information Technology", "Software Engineer", 101)
            val e3 = Employee(3, "Sophia Wright", "641 QA Lane", "Orlando", "FL",
                "Marketing", "Junior Social Media Manager", 102)

            employeeDao.insertNewEmployee(e1, e2, e3)
        }
    }

    @Preview
    @Composable
    fun ProjectDetailsLayoutPreview() {
        val navController = rememberNavController()
        ProjectDetailsLayout(navController = navController, null)
    }
    
    @Preview
    @Composable
    fun EmployeeListItemPreview() {
        val navController = rememberNavController()
        val emp = Employee(1, "John Doe", null, null, null, 
            null, null, 111)
        
        EmployeeListItem(employee = emp, DBConnection().buildDB(LocalContext.current).projectEmployeeDao(),.5f,
            Project(0, "SAMPLE", 0f), navController)
    }
}