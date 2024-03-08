package com.example.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

//DAO = DATA ACCESS OBJECT
//CONSIST OF FUNCTIONS WHICH HELPS IN DATA EXCHANGE
@Dao
interface ContactDAO {

    @Upsert //update + insert (if already exists with given id it will update)
    suspend fun upsertContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)

    //flow used to update ui whenever data is updated
    @Query("SELECT * FROM contact ORDER BY firstName ASC")
    fun getContactsOrderedByFirstName(): Flow<List<Contact>>

    @Query("SELECT * FROM contact ORDER BY lastName ASC")
    fun getContactsOrderedByLastName(): Flow<List<Contact>>

    @Query("SELECT * FROM contact ORDER BY phoneNumber ASC")
    fun getContactsOrderedByPhoneNumber(): Flow<List<Contact>>

    //now we need to connect DAO and tables
    //make database class

}