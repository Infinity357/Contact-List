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
) : ViewModel() {

    private val _state = MutableStateFlow(ContactState())

    private val _sortType = MutableStateFlow(SortType.FIRST_NAME)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _contacts = _sortType.flatMapConcat { sortType ->
        when (sortType) {
            SortType.FIRST_NAME -> repository.getContactByFirstName()
            SortType.LAST_NAME -> repository.getContactByLastName()
            SortType.PHONE_NUMBER -> repository.getContactByPhoneNumber()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    }

    val state = combine(_state, _contacts, _sortType) { state, contacts, sortType ->
        state.copy(
            contacts = contacts,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ContactState())

    fun onEvent(event: ContactEvent) {
        when (event) {
            is ContactEvent.SortContacts -> {
                _sortType.value = event.sortType
            }
            is ContactEvent.DeleteContact -> {
                viewModelScope.launch {
                    repository.deleteContact(event.contact)
                }
            }
            ContactEvent.SaveContact -> {
                val firstName = _state.value.firstName
                val lastName = _state.value.lastName
                val phoneNumber = _state.value.phoneNumber

                if (firstName.isEmpty() || phoneNumber.isEmpty()) return

                val contact = Contact(
                    firstName = firstName,
                    lastName = lastName,
                    phoneNumber = phoneNumber
                )

                viewModelScope.launch {
                    repository.upsertContact(contact)
                }
            }
            is ContactEvent.SaveFirstName -> {
                _state.update {
                    it.copy(
                        firstName = event.firstName
                    )
                }
            }
            is ContactEvent.SaveLastName -> {
                _state.update {
                    it.copy(
                        lastName = event.lastName
                    )
                }
            }
            is ContactEvent.SavePhoneNumber -> {
                _state.update {
                    it.copy(
                        phoneNumber = event.phoneNumber
                    )
                }
            }
            ContactEvent.doneAdding -> {
                _state.update {
                    it.copy(
                        isAddingContact = false,
                        firstName = "",
                        lastName = "",
                        phoneNumber = ""
                    )
                }
            }
            ContactEvent.nowAdding -> {
                _state.update {
                    it.copy(
                        isAddingContact = true
                    )
                }
            }

            is ContactEvent.onSearchQueryChange -> {
                _state.update {
                    it.copy(
                        searchQuery = event.newQuery
                    )
                }
            }

            is ContactEvent.isSearching -> {
                _state.update {
                    it.copy(
                        isSearchActive = event.isSearch
                    )
                }
            }

        }
    }
}
