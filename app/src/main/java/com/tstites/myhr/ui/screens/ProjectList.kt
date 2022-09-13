package com.tstites.myhr.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tstites.myhr.db.DBConnection
import com.tstites.myhr.obj.Project
import com.tstites.myhr.obj.ProjectDao
import kotlinx.coroutines.runBlocking

class ProjectList {

    @Composable
    fun ProjectListLayout(navController: NavController) {
        val context = LocalContext.current
        val projectDao = DBConnection().buildDB(context).projectDao()

        //Keep a copy of the original data so the database does not have to be read more than once
        val originalData = remember { mutableStateListOf<Project>() }
        val data = remember { mutableStateListOf<Project>() }

        runBlocking {
            if (originalData.isEmpty()) {
                originalData.addAll(setupDB(projectDao = projectDao))
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
                        Card {
                            //TODO
                        }
                    }
                })
            }
        }
            //Underneath the list is the bar that contains the searching, filtering, and adding buttons
            BottomAppBar(backgroundColor = MaterialTheme.colors.primarySurface,
                contentColor = contentColorFor(SnackbarDefaults.backgroundColor),
                elevation = AppBarDefaults.BottomAppBarElevation,
                content = {
                    //TODO
            })
        }

    @Preview
    @Composable
    fun ProjectListLayoutPreview() {
        val nav = rememberNavController()
        ProjectListLayout(navController = nav)
    }

    private fun setupDB(projectDao: ProjectDao): List<Project> {
        //Only insert sample data if the database does not have any entries already
        if (projectDao.getTableEntries() == 0) {
            insertData(projectDao)
        }

        return ArrayList(projectDao.getAllProjects())
    }

    private fun insertData(projectDao: ProjectDao) {
        if (projectDao.getTableEntries() == 0) {
            val p1 = Project(0, "MyHr+", 0.33f)

            projectDao.insertNewProject(p1)
        }
    }
}