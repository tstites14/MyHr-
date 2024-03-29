package com.tstites.myhr.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.room.Dao
import com.tstites.myhr.ui.screens.Screens

/*Android uses the Jetpack Compose system to display the UI. This uses @Composable functions
* to set up individual UI elements with their own states and variables that may be modified individually.
* For the most part, the application will use the same design for each type of element (button, textbox, etc.)
* and therefore it is possible to create a "library" of common elements that may be picked from as needed
* to maintain a uniform UI style*/
class CommonElements {
    @Composable
    fun DefaultButton(text: String, modifier: Modifier, onClick: () -> Unit) {
        Button(
            onClick = onClick,
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.elevation(2.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
        ) {
            //This is the label text for the button
            Text(text = text, modifier = Modifier.padding(8.dp))
        }
    }
    
    @Composable
    fun DefaultTextField(text: String, placeholder: String, title: String, modifier: Modifier, state: Boolean,
                        textBoxUpdate: (currentText: String) -> Unit) {
        /*Because each element has its own state, only one variable is needed to control the UI for
        * every element individually*/

        //This determines the text to be displayed when the user types
        val textState = remember { mutableStateOf(text) }

        TextField(textStyle = TextStyle(fontSize = 20.sp), value = textState.value,
            onValueChange = {
                textState.value = it
                textBoxUpdate(textState.value) }, label = { Text(title) }, modifier = modifier,
            shape = RoundedCornerShape(4.dp), enabled = state, placeholder = { Text(placeholder) })
    }

    //This is identical to DefaultTextField() but instead uses a number-only field
    @Composable
    fun DefaultTextFieldNum(text: String, placeholder: String, title: String, modifier: Modifier, state: Boolean,
                         textBoxUpdate: (currentText: String) -> Unit) {
        val textState = remember { mutableStateOf(text) }

        TextField(textStyle = TextStyle(fontSize = 20.sp), value = textState.value,
            onValueChange = {
                textState.value = it
                textBoxUpdate(textState.value)
            }, label = { Text(title) }, modifier = modifier,
            shape = RoundedCornerShape(4.dp), enabled = state, placeholder = { Text(placeholder) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }

    @Composable
    fun defaultConfirmationAlert(delete: () -> Unit): Boolean {
        val buttonPressed = remember { mutableStateOf(true) }

        AlertDialog(onDismissRequest = { buttonPressed.value = false }, buttons = {
            Column {
                Text("Are you sure you would like to delete this entry?",
                    modifier = Modifier.padding(24.dp), textAlign = TextAlign.Center)

                Row {
                    Button(onClick = {
                        delete()

                        buttonPressed.value = false
                    }, modifier = Modifier.fillMaxWidth(0.5f).padding(8.dp)) {
                        Text("Confirm")
                    }
                    Button(onClick = {
                        buttonPressed.value = false
                    }, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        Text("Dismiss")
                    }
                }
            }
        })

        return buttonPressed.value
    }

    /*The @Preview annotation allows Android Studio to display a realtime preview of the given
    * @Composable element*/
    @Preview
    @Composable
    fun DefaultButtonPreview() {
        DefaultButton(text = "Sample", modifier = Modifier.padding(4.dp), ) {

        }
    }
    
    @Preview
    @Composable
    fun DefaultTextFieldPreview() {
        DefaultTextField(text = "", "Sample", "Sample Text", modifier = Modifier.padding(4.dp), false) {

        }
    }
}