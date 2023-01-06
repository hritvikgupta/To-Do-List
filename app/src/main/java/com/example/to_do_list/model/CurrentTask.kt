package com.example.to_do_list.model

object CurrentTask {
    var taskList:Tasks?=null
    var currentTask:List<Items>?=null
    var items:Items? = null

    fun currentState(task:Tasks){
        this.taskList = task
    }

}