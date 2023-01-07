package com.hgapplication.to_do_list.model

import androidx.room.*

@Entity(tableName = "Items")
data class Items(
    @ColumnInfo(name = "itemName")
    val itemName:String?=null ,
    @ColumnInfo(name = "tasklistname")
    val TaskListName:String?=null,
    @ColumnInfo(name = "username")
    val userID:Long?=null

){
@PrimaryKey(autoGenerate = true)
var id: Long = 0
}


