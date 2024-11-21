package com.example.contactapp.DataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Upsert
    suspend fun upsertContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)

    @Query("SELECT * FROM Contact ORDER BY firstName ASC")
    fun getContactByFirstName():Flow<List<Contact>>

    @Query("SELECT * FROM Contact ORDER BY lastName ASC")
    fun getContactByLastName(): Flow<List<Contact>>

    @Query("SELECT * FROM Contact ORDER BY phoneNumber ASC")
    fun getContactByPhoneNumber() : Flow<List<Contact>>

}