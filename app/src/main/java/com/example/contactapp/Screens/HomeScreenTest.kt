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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.example.contactapp.DataBase.Contact
import com.example.contactapp.StateAndEvent.ContactEvent
import com.example.contactapp.StateAndEvent.ContactState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun HomeScreenTest(
    state: StateFlow<ContactState>,
    onEvent: (ContactEvent) -> Unit
) {
    val contactState = state.collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextField(
            value = contactState.firstName,
            onValueChange = { onEvent(ContactEvent.SaveFirstName(it)) },
            placeholder = { Text(text = "First Name") }
        )

        TextField(
            value = contactState.lastName,
            onValueChange = { onEvent(ContactEvent.SaveLastName(it)) },
            placeholder = { Text(text = "Last Name") }
        )

        TextField(
            value = contactState.phoneNumber,
            onValueChange = { onEvent(ContactEvent.SavePhoneNumber(it)) },
            placeholder = { Text(text = "Phone Number") }
        )

        Button(
            onClick = { onEvent(ContactEvent.SaveContact) },
            enabled = contactState.firstName.isNotEmpty() && contactState.lastName.isNotEmpty() && contactState.phoneNumber.isNotEmpty()
        ) {
            Text(text = "Save")
        }

        LazyColumn {
            items(contactState.contacts) { contact ->
                Column(
                    modifier = Modifier.fillMaxWidth().clickable { onEvent(ContactEvent.DeleteContact(contact)) }
                ) {
                    Text(text = "Name: ${contact.firstName} ${contact.lastName}")
                    Spacer(Modifier.height(6.dp))
                    Text(text = "PhoneNumber: ${contact.phoneNumber}")
                    HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(6.dp))
                }
            }
        }
    }
}
