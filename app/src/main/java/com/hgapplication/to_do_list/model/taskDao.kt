package com.hgapplication.to_do_list.model

import androidx.room.*

@Dao
interface taskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(tasks: Tasks):Long
    @Query("SELECT * FROM taskListItems WHERE tasklistname = :tasklistname" )
    suspend fun getTask(tasklistname:String):Tasks
    @Query("DELETE FROM taskListItems WHERE id = :id")
    suspend fun deletTask(id:Long)
    @Query("Select * FROM taskListItems")
    suspend fun getAll():Tasks

}