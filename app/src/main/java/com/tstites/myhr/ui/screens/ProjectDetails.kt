package com.tstites.myhr.ui.screens

import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tstites.myhr.obj.Project
import com.tstites.myhr.ui.components.CommonElements

class ProjectDetails {

    @Composable
    fun ProjectDetailsLayout(navController: NavController, projectInfo: Bundle?) {
        val elements = CommonElements()
        val textFieldState = remember { mutableStateOf(false) }

        val project = Project(
            projectInfo?.getInt("id") ?: 0,
            projectInfo?.getString("title") ?: "",
            projectInfo?.getFloat("progress") ?: 0f
        )

        Column(modifier = Modifier.fillMaxSize()) {
            Card {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
                    .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    //Title text box
                    elements.DefaultTextField(text = project.title ?: "", placeholder = "Title",
                        "Title", modifier = Modifier.padding(8.dp), textFieldState.value) {
                        project.title = it
                    }
                    Spacer(modifier = Modifier.padding(24.dp))

                    Text("Current progress:")
                    LinearProgressIndicator(progress = project.progress,
                        modifier = Modifier
                            .padding(16.dp)
                            .height(16.dp)
                            .fillMaxWidth())
                    Text("${project.progress}%")
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

                                }, modifier = Modifier.fillMaxWidth(0.5f).padding(8.dp)) {
                                    Text("Confirm")
                                }

                                Button(onClick = {
                                    deleteButtonPressed.value = false
                                }, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                                    Text("Dismiss")
                                }
                            }
                        }
                    })
                }
            }
        }
    }

    @Preview
    @Composable
    fun ProjectDetailsLayoutPreview() {
        val navController = rememberNavController()
        ProjectDetailsLayout(navController = navController, null)
    }
}