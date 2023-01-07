package com.hgapplication.to_do_list.viewmodel

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hgapplication.to_do_list.model.Items
import com.hgapplication.to_do_list.model.LoginState
import com.hgapplication.to_do_list.model.Tasks
import com.hgapplication.to_do_list.model.UserDatabase
import kotlinx.coroutines.*


class MainViewModel(application:Application):AndroidViewModel(application) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val db by lazy { UserDatabase(getApplication()).userDao() }
    private val dbTasks by lazy{ UserDatabase(getApplication()).taskDao()}
    val userDeleted = MutableLiveData<Boolean>()
    val taskDeleted = MutableLiveData<Boolean>()
    val signout = MutableLiveData<Boolean>()

    var Tasks= arrayListOf<Tasks>()
    var job:Job? = null
    val default:List<Items> = listOf()
    var newList:List<Tasks> = listOf(Tasks("DELETED",null, listOf()))
    var it:Tasks?=null
    val perm_val  = Tasks.add(Tasks("Add New Lists",null, default))

    val exceptionHandler = CoroutineExceptionHandler{ coroutineContext, throwable->
        onError("Exception: ${throwable.localizedMessage}")
    }
    val taskLists = MutableLiveData<List<Tasks>>()
    val taskListLoadError = MutableLiveData<String?>()


    fun onSignout(){
        LoginState.logout()
        signout.value = true
        val prefs = getApplication<Application>().getSharedPreferences("ld", 0)
        val edit: SharedPreferences.Editor = prefs.edit()
        edit.clear()
        edit.apply()


    }
    fun refresh(){
        addPermVal()
        fetchTasksLists()
    }
    fun addTaskList(name:String){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            //val dummyList = getList(name:String)
            //val newtask= dummyList + Tasks.add(Tasks(name))
            // val new_Task= dbTasks.getTask(name)
            //val user = db.getUser(LoginState.user?.username!!)
            var userTaskList =  LoginState.user?.taskList
            userTaskList= userTaskList?.plus(Tasks(name, null, default))
            //Tasks.add(Tasks(name))
            LoginState.user?.taskList = userTaskList!!
            db.updateTasksList(userTaskList!!, LoginState.user?.username!!)
            withContext(Dispatchers.Main){
                taskLists.postValue(userTaskList!!)
                taskListLoadError.value = null
            }
        }


    }

    fun fetchTasksLists(){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val dummyList = generateDummyTasks()
            //val getAll = dbTasks.getAll()
            val getAll = LoginState.user?.taskList
            withContext(Dispatchers.Main){
                taskLists.postValue(getAll!!)
                taskListLoadError.value = null
            }
        }

    }
    private fun getList():List<Tasks>{
        return Tasks
    }
    private fun generateDummyTasks() : List<Tasks>{
        Tasks.add(Tasks("Daily Routine",null, default))
        Tasks.add(Tasks("Office Tasks",null, default))
        Tasks.add(Tasks("Grocery List",null, default))
        Tasks.add(Tasks("Personal Tasks",null, default))
        return Tasks
    }
    private fun generate(name:String) : List<Tasks>{
        Tasks.add(Tasks("Daily Routine",null, default))
        Tasks.add(Tasks("Office Tasks",null, default))
        Tasks.add(Tasks(name, null, default))
        return Tasks
    }

    private fun addPermVal(){
        perm_val
    }

    fun onDeleteUser(){
        coroutineScope.launch {
            LoginState.user?.let { user->
                db.deleteUser(user.id)

            }
            withContext(Dispatchers.Main){
                LoginState.logout()
                userDeleted.value = true
            }
        }

    }

    private fun onError(message:String)
    {
        taskListLoadError.value = message
    }

    fun onUpdateTask(clicked:String, name:String){
        coroutineScope.launch {
            var userTaskList =  LoginState.user?.taskList?.toMutableList()
            userTaskList?.find { it.TaskListName == clicked }?.TaskListName = name
            //userTaskList= userTaskList?.toMutableList()
            //newList = listOf(tasks)
            LoginState.user?.taskList = userTaskList!!
            db.updateTasksList(userTaskList!!, LoginState.user?.username!!)
            db.updateItem(name,LoginState.user?.id)
            withContext(Dispatchers.Main){
                taskLists.postValue(userTaskList!!)
                //taskDeleted.value = true
            }
        }

    }

    fun onDeleteTask(tasks: Tasks){
        coroutineScope.launch {
            var userTaskList =  LoginState.user?.taskList?.toMutableList()
            userTaskList?.remove(tasks)
            //userTaskList= userTaskList?.toMutableList()
            newList = listOf(tasks)
            LoginState.user?.taskList = userTaskList!!
            db.updateTasksList(userTaskList!!, LoginState.user?.username!!)
            db.deleteItemAfterTaskDelete(tasks.TaskListName, LoginState.user!!.id)
            withContext(Dispatchers.Main){
                taskLists.postValue(userTaskList!!)
                taskDeleted.value = true
            }
        }

    }


}