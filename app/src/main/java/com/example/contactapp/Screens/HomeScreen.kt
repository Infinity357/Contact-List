package com.example.contactapp.Screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.contactapp.DataBase.Contact
import com.example.contactapp.StateAndEvent.ContactEvent
import com.example.contactapp.StateAndEvent.ContactState
import kotlinx.coroutines.flow.StateFlow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: StateFlow<ContactState>,
    onEvent: (ContactEvent) -> Unit
) {
    val contactState = state.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .shadow(
                        elevation = 10.dp,
                    ),
                colors = TopAppBarDefaults.topAppBarColors(Color.Blue),
                title = { Text(text = "Contacts", modifier = Modifier.padding(horizontal = 20.dp))}
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(ContactEvent.nowAdding)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Contact"
                )
            }
        }
    ) {innerPadding ->

        val selectedContact = remember { mutableStateOf<Contact?>(null) }

        BackHandler(
            selectedContact.value != null
        ) {
            selectedContact.value = null
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
        ){
            searchBar(
                state = state,
                onEvent = onEvent,
            )
            LazyColumn {
                items(contactState.contacts){contact->
                    ContactItem(
                        contact,
                        onClick = {
//                            onEvent(ContactEvent.DeleteContact(contact))
                            selectedContact.value = contact
                        },
                        onEvent = onEvent
                    )
                }
            }

            selectedContact.value?.let {
                CallDialog(it.phoneNumber)
            }
        }

        if(contactState.isAddingContact){
            AddingPopUp(
                state = state,
                onEvent = onEvent
            )
        }

    }

}