package com.example.to_do_list.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.to_do_list.model.Items
import com.example.to_do_list.model.LoginState
import com.example.to_do_list.model.Tasks
import com.example.to_do_list.model.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class saveListViewModel(application:Application):AndroidViewModel(application) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val db by lazy { UserDatabase(getApplication()).taskDao()}
    private val error =MutableLiveData<String>()
    val listSaved = MutableLiveData<Boolean>()
    val default:List<Items> = listOf()


    fun saveTaskList(taskListName:String){
        coroutineScope.launch {
            val listName = db.getTask(taskListName)
            if(listName!=null){
                withContext(Dispatchers.Main){
                    error.value = "User List Already Exists"
                }
            }else{
                val listName = Tasks(taskListName,null, default)
                val taskId = db.insertTask(listName)
                listName.id = taskId
                LoginState
                withContext(Dispatchers.Main){
                    listSaved.value = true
                }
            }
        }
    }
}