package com.example.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity //entity == table in db . here table name is contact
data class Contact(
    val firstName:String,
    val lastName:String,
    val phoneNumber:String,
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0  //default value as 0
)
