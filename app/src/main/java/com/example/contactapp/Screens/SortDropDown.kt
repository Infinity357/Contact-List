package com.example.contactapp.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuBoxScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.contactapp.StateAndEvent.ContactEvent
import com.example.contactapp.StateAndEvent.ContactState
import com.example.contactapp.StateAndEvent.SortType
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.exp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortDropDown(
    state: StateFlow<ContactState>,
    onEvent : (ContactEvent)->Unit
){
    val contactState = state.collectAsState().value

    val list = listOf(
        "First Name",
        "Last Name",
        "Phone Number"
    )

    ExposedDropdownMenuBox(
        expanded = contactState.openingSortDropDown,
        onExpandedChange = {
            if(it){
                onEvent(ContactEvent.openSortDropDown)
            }else{
                onEvent(ContactEvent.closeSortDropDown)
            }
        }
    ) {
        IconButton (
            onClick = {
                if(contactState.openingSortDropDown)
                    onEvent(ContactEvent.closeSortDropDown)
                else
                    onEvent(ContactEvent.openSortDropDown)
            }
        ){
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Change Sort Type",
                tint = Color.White,
                modifier = Modifier.menuAnchor()
            )

        }
        ExposedDropdownMenu(
            expanded = contactState.openingSortDropDown,
            onDismissRequest = {
                onEvent(ContactEvent.closeSortDropDown)
            },
            modifier = Modifier.width(150.dp)
        ) {
            Text(text = "Sort By:",Modifier.background((Color(red = 0 , green =160 , blue = 227 , alpha = 255))).fillMaxWidth().padding(8.dp), color = Color.White)
            HorizontalDivider(Modifier.fillMaxWidth())
            list.forEachIndexed{index,text->
                sortDropDownItems(
                    text= text,
                    onEvent = onEvent
                )
            }
        }
    }
}

@Composable
fun sortDropDownItems(
    text :String,
    onEvent: (ContactEvent) -> Unit
){
    Box(
     modifier = Modifier
         .clickable {
             if(text.equals("Phone Number"))
                 onEvent(ContactEvent.SortContacts(SortType.PHONE_NUMBER))
             else if (text.equals("Last Name"))
                 onEvent(ContactEvent.SortContacts(SortType.LAST_NAME))
             else
                 onEvent(ContactEvent.SortContacts(SortType.FIRST_NAME))
             onEvent(ContactEvent.closeSortDropDown)
         }
         .padding(4.dp)
    ){
        Text(text = text, modifier = Modifier.padding(4.dp))
//        HorizontalDivider(Modifier.fillMaxWidth())
    }
}