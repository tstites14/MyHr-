package com.tstites.myhr.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tstites.myhr.db.DBConnection
import com.tstites.myhr.obj.*
import kotlinx.coroutines.runBlocking

class ProjectList {

    @Composable
    fun ProjectListLayout(navController: NavController) {
        val context = LocalContext.current

        //Database connections
        val dbConnection = DBConnection().buildDB(context)
        val projectDao = dbConnection.projectDao()
        val projectEmployeeDao = dbConnection.projectEmployeeDao()
        val employeeDao = dbConnection.employeeDao()

        //Keep a copy of the original data so the database does not have to be read more than once
        val originalData = remember { mutableStateListOf<Project>() }
        val data = remember { mutableStateListOf<Project>() }

        runBlocking {
            if (originalData.isEmpty()) {
                originalData.addAll(setupDB(projectDao, projectEmployeeDao, employeeDao))
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
                items(data, itemContent = { project ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        //A Card is essentially a fancy list item that contains its own UI
                        Card(
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    navController.navigate(Screens.ProjectDetails.route +
                                        "/${project.id}/${project.title}/${project.progress}/")
                                }
                        ) {
                            Column {
                                Text(project.title ?: "Missing Data", style = TextStyle(fontSize = 20.sp),
                                    modifier = Modifier.padding(16.dp))
                                LinearProgressIndicator(progress = project.progress,
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .height(16.dp)
                                        .fillMaxWidth())
                            }
                        }
                    }
                })
            }

            //Underneath the list is the bar that contains the searching, filtering, and adding buttons
            BottomAppBar(backgroundColor = MaterialTheme.colors.primarySurface,
                contentColor = contentColorFor(SnackbarDefaults.backgroundColor),
                elevation = AppBarDefaults.BottomAppBarElevation,
                content = {
                    //TODO: ADD SEARCH, FILTER, AND ADD BUTTONS HERE
                })
        }
    }

    @Preview
    @Composable
    fun ProjectListLayoutPreview() {
        val nav = rememberNavController()
        ProjectListLayout(navController = nav)
    }

    private fun setupDB(projectDao: ProjectDao,
                        projectEmployeeDao: ProjectEmployeeDao, employeeDao: EmployeeDao): List<Project> {
        //Only insert sample data if the database does not have any entries already
        if (projectDao.getTableEntries() == 0) {
            insertData(projectDao, projectEmployeeDao, employeeDao)
        }

        //Calculate and update each project's average to reflect the actual value
        projectDao.getAllProjects().forEach {
            val avg = projectEmployeeDao.getProgressAverageByProject(it.id)
            it.progress = avg

            projectDao.updateExistingProject(it)
        }

        return ArrayList(projectDao.getAllProjects())
    }

    private fun insertData(projectDao: ProjectDao, projectEmployeeDao: ProjectEmployeeDao, employeeDao: EmployeeDao) {
        if (projectDao.getTableEntries() == 0) {
            val p1 = Project(0, "MyHr+", 0f)
            val p2 = Project(0, "Docx to Pdf Converter", 0f)

            projectDao.insertNewProject(p1, p2)
        }

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
}