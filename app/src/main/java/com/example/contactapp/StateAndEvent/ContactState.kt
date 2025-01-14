package com.example.contactapp.StateAndEvent

import com.example.contactapp.DataBase.Contact

data class ContactState(
    val contacts : List<Contact> = emptyList(),
    val firstName : String = "",
    val lastName : String = "",
    val phoneNumber : String = "",
    val isAddingContact : Boolean = false,
    val sortType: SortType = SortType.FIRST_NAME,
    val searchQuery : String = "",
    val isSearchActive : Boolean = false,
    val openingSortDropDown : Boolean = false
)