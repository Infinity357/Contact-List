package com.example.contactapp.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.example.contactapp.StateAndEvent.ContactEvent
import com.example.contactapp.StateAndEvent.ContactState

@Composable
fun HomeScreenTest(
    state: LiveData<ContactState>,
    onEvent:(ContactEvent) -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        state.value?.let {state->
            TextField(
                value = state.firstName,
                onValueChange = {onEvent(ContactEvent.saveFirstName(it))},
                placeholder = { Text(text = "First Name") }
            )
        }

        state.value?.let {state->
            TextField(
                value = state.lastName,
                onValueChange = {onEvent(ContactEvent.saveLastName(it))},
                placeholder = { Text(text = "Last Name") }
            )
        }

        state.value?.let {state->
            TextField(
                value = state.phoneNumber,
                onValueChange = {onEvent(ContactEvent.savePhoneNumber(it))},
                placeholder = { Text(text = "Phone Number") }
            )
        }

        Button(
            onClick = {onEvent(ContactEvent.saveContact)}
        ) {
            Text(text = "Save")
        }

        LazyColumn {
            state.value?.let {
                items(it.contacts){ contact->
                    Column(
                        modifier = Modifier.fillMaxWidth().clickable { onEvent(ContactEvent.deleteContact(contact)) }
                    ) {
                        Text(text = "Name : ${it.firstName} ${it.lastName}")
                        Spacer(Modifier.height(6.dp))
                        Text(text = "PhoneNumber : ${it.phoneNumber}")
                        HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(6.dp   ))
                    }
                }
            }
        }

    }
}