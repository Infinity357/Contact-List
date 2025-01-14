package com.example.contactapp.DataBase

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.contactapp.R

@Entity
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val firstName : String ,
    val lastName : String = "",
    val phoneNumber : String,
){
    fun doesMatchSearchQuery(query : String) : Boolean{
        val matchingCombinations = listOf(
            "$firstName$lastName",
            "$firstName $lastName",
            "$firstName",
            "$lastName",
        )
        return matchingCombinations.any{
            it.contains(query , ignoreCase = true)
        }
    }
}
