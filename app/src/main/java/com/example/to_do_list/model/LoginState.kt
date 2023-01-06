package com.example.to_do_list.model

object LoginState {

    var isLoggedIn = false
    var user:User? = null
    var tasklistName:Tasks?=null


    fun logout(){
        isLoggedIn = false
        user = null
    }

    fun login(user:User){
        isLoggedIn = true
        this.user = user
    }
}