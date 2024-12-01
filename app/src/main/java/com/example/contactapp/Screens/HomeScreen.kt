package com.example.contactapp.Screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.example.contactapp.DataBase.Contact
import com.example.contactapp.StateAndEvent.ContactEvent
import com.example.contactapp.StateAndEvent.ContactState
import kotlinx.coroutines.flow.StateFlow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: StateFlow<ContactState>,
    onEvent: (ContactEvent) -> Unit,
    getImageIndex: (String) -> Int
) {
    val contactState = state.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                actions = {
                    SortDropDown(
                        state,
                        onEvent = onEvent
                    )
                },
                modifier = Modifier
                    .shadow(
                        elevation = 10.dp,
                    ),
                colors = TopAppBarDefaults.topAppBarColors(Color(red = 0 , green =160 , blue = 227 , alpha = 255)),
                title = { Text(text = "Contacts", modifier = Modifier.padding(horizontal = 20.dp),color = Color.White)}
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = Color(red = 0 , green =160 , blue = 227 , alpha = 255),
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
                getImageIndex = getImageIndex
            )
            LazyColumn {
                items(contactState.contacts){contact->
                    ContactItem(
                        contact,
                        onClick = {
//                            onEvent(ContactEvent.DeleteContact(contact))
                            selectedContact.value = contact
                        },
                        onEvent = onEvent,
                        getImageIndex = getImageIndex
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