package com.example.contactapp.StateAndEvent

import com.example.contactapp.DataBase.Contact

sealed interface ContactEvent {
    object saveContact : ContactEvent
    data class deleteContact(val contact: Contact) : ContactEvent
    data class saveFirstName(val firstName: String) : ContactEvent
    data class saveLastName(val lastName: String) : ContactEvent
    data class  savePhoneNumber(val phoneNumeber : String) : ContactEvent
//    object showDialog : ContactEvent
//    object hideDialog : ContactEvent
    data class SortContacts(val sortType : SortType):ContactEvent
}