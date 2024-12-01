package com.example.contactapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.contactapp.DataBase.Contact
import com.example.contactapp.R
import com.example.contactapp.StateAndEvent.ContactEvent
import com.example.contactapp.StateAndEvent.ContactState
import com.example.contactapp.StateAndEvent.SortType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch



class ContactViewModel(
    private val repository: ContactRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ContactState())

    private val _sortType = MutableStateFlow(SortType.FIRST_NAME)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _contacts = _sortType
        .map { sortType ->
            when (sortType) {
                SortType.FIRST_NAME -> repository.getContactByFirstName()
                SortType.LAST_NAME -> repository.getContactByLastName()
                SortType.PHONE_NUMBER -> repository.getContactByPhoneNumber()
            }
        }
        .flatMapLatest { it } // Flatten the Flow returned by the repository
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private var hashMap : HashMap<String , Int> = HashMap<String , Int>()

    init {
        addImages()
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

            ContactEvent.closeSortDropDown -> {
                _state.update {
                    it.copy(
                        openingSortDropDown = false
                    )
                }
            }
            ContactEvent.openSortDropDown -> {
                _state.update {
                    it.copy(
                        openingSortDropDown = true
                    )
                }
            }
        }
    }

    fun addImages(){
        hashMap.put("a", R.drawable.letter_a )
        hashMap.put("b", R.drawable.letter_b )
        hashMap.put("c", R.drawable.letter_c )
        hashMap.put("d", R.drawable.letter_d )
        hashMap.put("e", R.drawable.letter_e )
        hashMap.put("f", R.drawable.letter_f )
        hashMap.put("g", R.drawable.letter_g )
        hashMap.put("h", R.drawable.letter_h )
        hashMap.put("i", R.drawable.letter_i )
        hashMap.put("j", R.drawable.letter_j )
        hashMap.put("k", R.drawable.letter_k )
        hashMap.put("l", R.drawable.letter_l )
        hashMap.put("m", R.drawable.letter_m )
        hashMap.put("n", R.drawable.letter_n )
        hashMap.put("o", R.drawable.letter_o )
        hashMap.put("p", R.drawable.letter_p )
        hashMap.put("q", R.drawable.letter_q )
        hashMap.put("r", R.drawable.letter_r )
        hashMap.put("s", R.drawable.letter_s )
        hashMap.put("t", R.drawable.letter_t )
        hashMap.put("u", R.drawable.letter_u )
        hashMap.put("v", R.drawable.letter_v )
        hashMap.put("w", R.drawable.letter_w )
        hashMap.put("x", R.drawable.letter_x )
        hashMap.put("y", R.drawable.letter_y )

        hashMap.put("z", R.drawable.letter_z )
    }

    fun getImageIndex(firstLetter : String): Int {
        if(hashMap.containsKey(firstLetter)) {
            var imageIndex: Int? = hashMap[firstLetter]
            if (imageIndex != null)
                return imageIndex;
        }
        return R.drawable.three

    }

}

