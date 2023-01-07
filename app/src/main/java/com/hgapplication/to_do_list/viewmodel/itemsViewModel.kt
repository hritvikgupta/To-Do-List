package com.hgapplication.to_do_list.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hgapplication.to_do_list.model.*
import kotlinx.coroutines.*

class itemsViewModel(application: Application):AndroidViewModel(application) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val db by lazy { UserDatabase(getApplication()).userDao() }

    var job: Job?=null
    val Items = arrayListOf<Items>()
    val currentTask = MutableLiveData<Tasks>()
    var currTask:Tasks?=null
    val listItems = MutableLiveData<List<Items>>()
    val itemsListLoadError = MutableLiveData<String?>()
    var newList:List<Items>?=null
    var newListId:Long?=null
    val perm_val  = Items.add(Items("Create A Task"))

    val itemDeleted = MutableLiveData<Boolean>()

    val exceptionHandler = CoroutineExceptionHandler{ coroutineContext, throwable->
        onError("Exception: ${throwable.localizedMessage}")
    }
    fun refresh(){
        onItemsPage()
        fetchItemsLists()

    }
    fun current(task:Tasks)
    {
        currTask = task

    }

    fun onItemsPage(){
       // perm_val
        Items.add(Items("Add A Item"))
    }
    fun addTaskList(name:String){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            //val dummyList = getList(name:String)
            //val newtask= dummyList + Tasks.add(Tasks(name))
            // val new_Task= dbTasks.getTask(name)
            //val user = db.getUser(LoginState.user?.username!!)

            //var userTaskList =  CurrentTask.taskList?.listItems
            //userTaskList= userTaskList?.plus(Items(name))
            //CurrentTask.taskList?.listItems = userTaskList!!

            //Tasks.add(Tasks(name))
            //val id = db.insertItems(CurrentTask.taskList?.listItems )

            //db.updateTaskListItems(CurrentTask.taskList?.listItems!!, currTask?.TaskListName)
            var itemslist = CurrentTask.taskList?.listItems
            val userId = LoginState.user?.id
            val itemId = db.insertItems(Items(name, CurrentTask.taskList?.TaskListName, userId))
            val newList = db.getItemsFromTask(CurrentTask.taskList?.TaskListName, userId)
            CurrentTask.items?.id =itemId
            withContext(Dispatchers.Main){
                if (itemslist != null) {
                    listItems.postValue(itemslist + newList!!)
                }
                itemsListLoadError.value = null
            }
        }


    }


    fun fetchItemsLists(){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val dummyList = generateDummyItems()
            //val getAll = dbTasks.getAll()
            val userId = LoginState.user?.id
            db.insertItems(Items("Create A Task"))
            newList = db.getItemsFromTask(CurrentTask.taskList?.TaskListName, userId)
            val getAll = LoginState.user?.taskList
            var itemslist = CurrentTask.taskList?.listItems
            //newList = newList.plus(Items("Something", CurrentTask.taskList?.TaskListName,LoginState.user?.id))

            //db.updateTaskListItems(itemslist!! as MutableList<Items>,currTask?.TaskListName)
            withContext(Dispatchers.Main){
                if (itemslist != null) {
                    listItems.postValue( newList!!)
                }
                itemsListLoadError.value = null
            }
        }
    }
    /*
    * if (items != null) {
                    if (items.isEmpty())
                        listItems.postValue(Items)
                    else{
                        val list  = LoginState.user?.taskList?.get(0)?.listItems?.plus(items)
                        //db.updateTasksList(LoginState.user., LoginState.user?.username!!)
                        if (list != null) {
                            db.updateTaskListItems(list,tasks.TaskListName!!)
                        }
                        listItems.postValue(items!!)
                    }
                }*/
    fun generateDummyItems() : List<Items>{
        Items.add(Items("Daily Routine"))
        Items.add(Items("Daily Routine"))
        Items.add(Items("Daily Routine"))
        Items.add(Items("Daily Routine"))
        return Items
    }

    private fun onError(message:String)
    {
        itemsListLoadError.value = message


    }
    fun onUpdateItemName(name:String?){
        coroutineScope.launch {
            val userId = LoginState.user?.id
            //aadb.deleteItemsFromTask(item.itemName,CurrentTask.taskList?.TaskListName,userId)
            db.updataItemName(name,userId, CurrentTask.taskList?.TaskListName)
            withContext(Dispatchers.Main){
                //CurrentTask.taskList?.listItems= newList!!
                newList = db.getItemsFromTask(CurrentTask.taskList?.TaskListName, userId)
                listItems.postValue(newList)
                itemDeleted.value = true
            }
        }

    }

    fun onDeleteItem(item:Items){
        coroutineScope.launch {
            val userId = LoginState.user?.id
            db.deleteItemsFromTask(item.itemName,CurrentTask.taskList?.TaskListName,userId)
            withContext(Dispatchers.Main){
                //CurrentTask.taskList?.listItems= newList!!
                newList = db.getItemsFromTask(CurrentTask.taskList?.TaskListName, userId)
                listItems.postValue(newList)
                itemDeleted.value = true
            }
        }
    }

}