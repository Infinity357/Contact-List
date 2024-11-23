package com.example.contactapp.StateAndEvent

import com.example.contactapp.DataBase.Contact

sealed interface ContactEvent {
    object SaveContact : ContactEvent
    data class DeleteContact(val contact: Contact) : ContactEvent
    data class SaveFirstName(val firstName: String) : ContactEvent
    data class SaveLastName(val lastName: String) : ContactEvent
    data class SavePhoneNumber(val phoneNumber : String) : ContactEvent
//    object showDialog : ContactEvent
//    object hideDialog : ContactEvent
    data class SortContacts(val sortType : SortType):ContactEvent
}