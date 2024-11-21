package com.example.contactapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.contactapp.DataBase.Contact
import com.example.contactapp.StateAndEvent.ContactEvent
import com.example.contactapp.StateAndEvent.ContactState
import com.example.contactapp.StateAndEvent.SortType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ContactViewModel(
    private val repository: ContactRepository
) : ViewModel(){


    private val _state = MutableStateFlow(ContactState())

    private val _sortType = MutableStateFlow(SortType.FIRST_NAME)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _contacts =_sortType.flatMapConcat { sortType ->
       when(sortType) {
           SortType.FIRST_NAME -> repository.getContactByFirstName()
           SortType.LAST_NAME -> repository.getContactByLastName()
           SortType.PHONE_NUMBER -> repository.getContactByPhoneNumber()
       }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    }

    val state = combine(_state,_contacts,_sortType){state,contact,sortType->
        state.copy(
            contacts = contact,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ContactState()).asLiveData(viewModelScope.coroutineContext)

    fun onEvent(event : ContactEvent){
        when(event){
            is ContactEvent.SortContacts -> {
                _sortType.value =event.sortType
            }
            is ContactEvent.deleteContact ->{
                viewModelScope.launch {
                    repository.deleteContact(event.contact)
                }
            }
            ContactEvent.saveContact ->{
                val firstName = _state.value.firstName
                val lastName = _state.value.lastName
                val phoneNumber = _state.value.phoneNumber

                if(firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty())
                    return

                val contact = Contact(
                    firstName = firstName,
                    lastName = lastName,
                    phoneNumber = phoneNumber
                )

                viewModelScope.launch {
                    repository.upsertContact(contact)
                }
            }
            is ContactEvent.saveFirstName ->{
                _state.update {
                    it.copy(
                        firstName =event.firstName
                    )
                }
            }
            is ContactEvent.saveLastName -> {
                _state.update {
                    it.copy(
                        lastName =event.lastName
                    )
                }
            }
            is ContactEvent.savePhoneNumber -> {
                _state.update {
                    it.copy(
                        phoneNumber =event.phoneNumeber
                    )
                }
            }
        }
    }

}