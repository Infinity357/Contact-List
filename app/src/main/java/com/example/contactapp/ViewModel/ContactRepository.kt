package com.example.contactapp.ViewModel

import com.example.contactapp.DataBase.Contact
import com.example.contactapp.DataBase.ContactDataBase

class ContactRepository(
    private val db : ContactDataBase
) {

    suspend fun upsertContact(contact: Contact){
        db.dao.upsertContact(contact)
    }

    suspend fun deleteContact(contact: Contact) {
        db.dao.deleteContact(contact)
    }

    fun getContactByFirstName() = db.dao.getContactByFirstName()

    fun getContactByLastName() = db.dao.getContactByLastName()

    fun getContactByPhoneNumber() = db.dao.getContactByPhoneNumber()

}