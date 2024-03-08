package com.example.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(//define our database(i.e. class is database) , list of contacts
    entities = [Contact::class],
    version = 1
)
abstract class ContactDatabase : RoomDatabase(){

    abstract  val dao:ContactDAO
}