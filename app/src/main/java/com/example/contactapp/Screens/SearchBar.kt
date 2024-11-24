package com.example.contactapp.Screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.contactapp.DataBase.Contact
import com.example.contactapp.StateAndEvent.ContactEvent
import com.example.contactapp.StateAndEvent.ContactState
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun searchBar(
    state: StateFlow<ContactState>,
    onEvent:(ContactEvent)->Unit,
){
    val selectedContact = remember { mutableStateOf<Contact?>(null) }
    val contactState = state.collectAsState().value

    BackHandler(
        selectedContact.value != null
    ) {
        selectedContact.value = null
    }

    SearchBar(
        query = contactState.searchQuery,
        onQueryChange ={
            onEvent(ContactEvent.onSearchQueryChange(it))
        } ,
        onSearch = {
            onEvent(ContactEvent.isSearching(false))
        },
        active = contactState.isSearchActive,
        onActiveChange ={
            onEvent(ContactEvent.isSearching(it))
        },
        modifier = Modifier
            .fillMaxWidth(),
        placeholder = {
            Text(text = "Search")
        },
        leadingIcon = {
            Icon(
                imageVector =Icons.Default.Search,
                contentDescription = "Search Contacts"
            )
        },
        trailingIcon = {
            if(contactState.searchQuery.isNotEmpty() || contactState.isSearchActive == true) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Search",
                    modifier = Modifier
                        .clickable {
                            if (contactState.searchQuery.isEmpty()) {
                                onEvent(ContactEvent.isSearching(false))
                            } else {
                                onEvent(ContactEvent.onSearchQueryChange(""))
                            }
                        }
                )
            }
        }
    ) {
        LazyColumn {
            items(items = contactState.contacts){contact->
               if(contactState.searchQuery.isEmpty()) {
                   return@items
               }
               if(contact.doesMatchSearchQuery(query = contactState.searchQuery)){
                   ContactItem(
                       contact,
                       onClick = {
                           selectedContact.value = contact
                       },
                       onEvent = onEvent
                   )
               }
            }
        }

        selectedContact.value?.let{
            CallDialog(it.phoneNumber)
        }

    }
}