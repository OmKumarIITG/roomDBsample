package com.example.roomdb

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ContactViewModel(
    private val dao:ContactDAO //pass dao to viewmodel
) : ViewModel(){

    private val _sortType = MutableStateFlow(SortType.FIRST_NAME)
    @OptIn(ExperimentalCoroutinesApi::class)
    //contacts change as soon as sort type changes , provide dynamic ui , flatmaplatest takes flow returns flow
    private val _contacts = _sortType
        .flatMapLatest { sortType->
            when(sortType){
                SortType.FIRST_NAME -> dao.getContactsOrderedByFirstName()
                SortType.LAST_NAME -> dao.getContactsOrderedByLastName()
                SortType.PHONE_NUMBER -> dao.getContactsOrderedByPhoneNumber()
            }

        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(ContactState())
    //combine all above three flows in one flow
    //when any of these three flow , emit any value , code inside will be executed
    val state = combine(_state,_sortType,_contacts){state,sortType,contacts->
        state.copy(
            contacts = contacts,
            sortType = sortType
        )
    }        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ContactState())

    fun onEvent(event:ContactEvent){
        when(event){
            //press ALT+ENTER to add remaining branches
            is ContactEvent.DeleteContact -> {
                viewModelScope.launch {
                    //since delete contact is a suspend function
                    dao.deleteContact(event.contact)
                }
            }
            ContactEvent.HideDialog -> {
                _state.update {
                    it.copy(
                        isAddingContact = false
                    )
                }
            }
            ContactEvent.SaveContact -> {
                //most difficult func
                val firstName=state.value.firstName
                val lastName=state.value.lastName
                val phoneNumber=state.value.phoneNumber
                if(firstName.isBlank() || lastName.isBlank() || phoneNumber.isBlank()){
                    return //do nothing
                }
                //else
                val contact=Contact(
                    firstName=firstName,
                    lastName=lastName,
                    phoneNumber=phoneNumber
                )//id will be automatically generated
                //now insert in database
                viewModelScope.launch {
                    dao.upsertContact(contact)
                }
                //update state
                _state.update {
                    it.copy(
                        isAddingContact = false,
                        firstName = "",
                        lastName = "",
                        phoneNumber = ""
                    )
                }
            }
            is ContactEvent.SetFirstName -> {
                _state.update{
                    it.copy(
                        firstName = event.firstName
                    )
                }
            }
            is ContactEvent.SetLastName -> {
                _state.update{
                    it.copy(
                        lastName = event.lastName
                    )
                }
            }
            is ContactEvent.SetPhoneName -> {
                _state.update{
                    it.copy(
                        phoneNumber = event.phoneNumber
                    )
                }
            }
            ContactEvent.ShowDialog -> {
                _state.update {
                    it.copy(
                        isAddingContact = true
                    )
                }
            }
            is ContactEvent.SortContacts -> {
                _sortType.value=event.sortType
            }
        }
    }
}