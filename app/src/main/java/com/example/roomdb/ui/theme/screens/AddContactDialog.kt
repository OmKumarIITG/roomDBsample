package com.example.roomdb.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.roomdb.ContactEvent
import com.example.roomdb.ContactState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContactDialog(
    state:ContactState,
    onEvent: (ContactEvent)->Unit,
    modifier: Modifier =Modifier
) {
    //make sure to choose correct alert dialog
    AlertDialog(
        modifier=modifier,
        onDismissRequest = {
                 onEvent(ContactEvent.HideDialog)
        },
        confirmButton = {
                        Box(
                            modifier=Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterEnd
                        ){
                            Button(
                                onClick = {
                                    onEvent(ContactEvent.SaveContact)
                                }
                            ){
                                Text("Save Contact")
                            }
                        }
        },
        title = { Text("Add Contact") },
        text={
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                TextField(
                    value = state.firstName,
                    onValueChange = {
                        onEvent(ContactEvent.SetFirstName(it))
                    },
                    placeholder = {
                        Text("First Name")
                    }
                )
                TextField(
                    value = state.lastName,
                    onValueChange = {
                        onEvent(ContactEvent.SetLastName(it))
                    },
                    placeholder = {
                        Text("Last Name")
                    }
                )
                TextField(
                    value = state.phoneNumber,
                    onValueChange = {
                        onEvent(ContactEvent.SetPhoneName(it))
                    },
                    placeholder = {
                        Text("Phone Name")
                    }
                )
            }
        }
    )
}