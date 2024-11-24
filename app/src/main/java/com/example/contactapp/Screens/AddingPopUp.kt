package com.example.contactapp.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import com.example.contactapp.StateAndEvent.ContactEvent
import com.example.contactapp.StateAndEvent.ContactState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun AddingPopUp(
    state : StateFlow<ContactState>,
    onEvent:(ContactEvent)->Unit
){

    val contactState = state.collectAsState().value

    AlertDialog(
        onDismissRequest = {
            onEvent(ContactEvent.doneAdding)
        },
        title = {
            Text(text = "Add Contact")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = contactState.firstName ,
                    onValueChange = {
                        onEvent(ContactEvent.SaveFirstName(it))
                    },
                    placeholder = { Text(text = "First Name") }
                )
                TextField(
                    value = contactState.lastName ,
                    onValueChange = {
                        onEvent(ContactEvent.SaveLastName(it))
                    },
                    placeholder = {
                        Text(text = "Last Name")
                    }
                )
                TextField(
                    value = contactState.phoneNumber ,
                    onValueChange = {
                        onEvent(ContactEvent.SavePhoneNumber(it))
                    },
                    placeholder = { Text(text = "Phone Number") }
                )
            }
        },
        confirmButton ={
            TextButton(onClick = {
                onEvent(ContactEvent.SaveContact)
                onEvent(ContactEvent.doneAdding)
            }) {
                Text(text = "Save")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onEvent(ContactEvent.doneAdding)
            }) {
                Text(text = "Cancel")
            }
        }
    )
}