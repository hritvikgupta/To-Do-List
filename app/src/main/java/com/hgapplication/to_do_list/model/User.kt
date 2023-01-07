package com.hgapplication.to_do_list.model

import androidx.room.*
import com.google.gson.Gson

@Entity
data class User(
    @ColumnInfo(name = "username")
    val username:String,
    @ColumnInfo(name = "password_info")
    val passwordHash:Int,
    val info:String,
    @ColumnInfo(name = "userTaskLists")
    var taskList:List<Tasks>
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
class taskListTypeConverters{
    @TypeConverter
    fun listToJson(value:List<Tasks>?) = Gson().toJson(value)
    @TypeConverter
    fun jsonToList(value:String) = Gson().fromJson(value,Array<Tasks>::class.java).toList()

    @TypeConverter
    fun itemsListToJson(value:List<Items>?) = Gson().toJson(value)
    @TypeConverter
    fun jsonToItemList(value:String) = Gson().fromJson(value, Array<Items>::class.java).toList()
}