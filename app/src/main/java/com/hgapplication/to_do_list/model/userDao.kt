package com.hgapplication.to_do_list.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.*

@Dao
interface userDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user:User):Long

    @Query("SELECT * FROM user WHERE username = :username" )
    suspend fun getUser(username:String):User

    @Query("DELETE FROM user WHERE id = :id")
    suspend fun deleteUser(id:Long)

    //@Query("SELECT * FROM user WHERE userTaskLists = :taskList")
    //suspend fun getTaskList(taskList:List<Tasks>):List<Tasks>

    @Query("UPDATE user SET userTaskLists = :taskList WHERE username = :username")
    suspend fun updateTasksList(taskList: List<Tasks>, username: String)

    //@Query("UPDATE taskListItems SET listItems = :listItems WHERE taskListName = :taskListName")
    //suspend fun updateTaskListItems(listItems:List<Items>, taskListName:String?)

    @Transaction
    @Query("SELECT * FROM taskListItems Where tasklistname = :taskListName")
    suspend fun getTaskWithItems(taskListName: String?):List<Items>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items:Items):Long

    @Query("SELECT * FROM Items Where tasklistname = :taskListName AND username = :username")
    suspend fun getItemsFromTask(taskListName: String?, username:Long?):List<Items>

    @Query("DELETE FROM Items WHERE itemName= :itemName AND tasklistname = :taskListName AND username = :username")
    suspend fun deleteItemsFromTask(itemName:String?,taskListName: String?, username:Long?)

    @Query("DELETE FROM Items WHERE tasklistname = :taskListName AND username = :username")
    suspend fun deleteItemAfterTaskDelete(taskListName: String?, username:Long?)

    @Query("UPDATE Items SET itemName =:itemName WHERE username = :username AND tasklistname = :tasklistname ")
    suspend fun updataItemName(itemName: String?, username: Long?, tasklistname: String?)

    @Query("UPDATE Items SET tasklistname = :tasklistname WHERE username = :username")
    suspend fun updateItem(tasklistname: String?, username: Long?)

}