package com.example.to_do_list.model

import androidx.room.*
import com.google.gson.Gson


@Entity(tableName = "taskListItems")
data class Tasks(
    @ColumnInfo(name = "tasklistname")
    var TaskListName:String?=null,
    val logo:String?=null,
   // @ColumnInfo(name ="listItems")
    var listItems:List<Items>
){
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

class itemListTypeConverters{
    @TypeConverter
    fun listToJson(value:List<Items>?) = Gson().toJson(value)
    @TypeConverter
    fun jsonToList(value:String) = Gson().fromJson(value,Array<Items>::class.java).toList()
}
