package com.example.roomdb.ui.theme.screens

import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.roomdb.ContactEvent
import com.example.roomdb.ContactState
import com.example.roomdb.SortType

@Composable
fun ContactScreen(
    state:ContactState,
    onEvent:(ContactEvent)->Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(ContactEvent.ShowDialog)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    "add contact"
                )
            }
        }
    ){padding->
        if(state.isAddingContact){
            AddContactDialog(state = state, onEvent = onEvent)
        }
        LazyColumn(
          contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            //Top row
            item{
                Row(
                    modifier= Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    SortType.entries.forEach { sortType->
                        //for each make a row element
                        Row(
                            modifier=Modifier
                                .clickable {
                                    onEvent(ContactEvent.SortContacts(sortType))
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            RadioButton(
                                selected = sortType == state.sortType,
                                onClick = {
                                    onEvent(ContactEvent.SortContacts(sortType))
                                }
                            )
                            Text(sortType.name)
                        }
                    }
                }
            }

            // now show all contacts in state
            items(state.contacts){contact->
                Row(
                    modifier=Modifier
                        .fillMaxWidth()
                ){
                    Column(
                        modifier=Modifier
                            .weight(1f)
                    ){
                        Text(
                            "${contact.firstName} ${contact.lastName}",
                            fontSize = 20.sp
                        )
                        Text(
                            contact.phoneNumber,
                            fontSize = 12.sp
                        )
                    }
                    IconButton(
                        onClick = {
                            onEvent(ContactEvent.DeleteContact(contact))
                        }
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            "delete contact"
                        )
                    }
                }
            }
        }

    }
}