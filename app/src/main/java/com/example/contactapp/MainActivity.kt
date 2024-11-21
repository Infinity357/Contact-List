package com.example.contactapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.contactapp.DataBase.ContactDataBase
import com.example.contactapp.Screens.HomeScreenTest
import com.example.contactapp.ViewModel.ContactRepository
import com.example.contactapp.ViewModel.ContactViewModel
import com.example.contactapp.ui.theme.ContactAppTheme

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            ContactDataBase::class.java,
            name = "contacts.db"
        ).build()
    }

    private val viewModel by viewModels<ContactViewModel>(
        factoryProducer ={
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                   return ContactViewModel(ContactRepository(db)) as T
                }
                
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContactAppTheme {
                HomeScreenTest(
                    state = viewModel.state,
                    onEvent = {viewModel::onEvent}
                )
            }
        }
    }
}