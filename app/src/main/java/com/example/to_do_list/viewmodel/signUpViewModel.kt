package com.example.to_do_list.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.to_do_list.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class signUpViewModel(application:Application):AndroidViewModel(application) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val db by lazy { UserDatabase(getApplication()).userDao() }
    val signupComplete = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()
    val Tasks = arrayListOf<Tasks>()
    val default:List<Items> = listOf()


    fun signup(username: String, password: String, info: String) {
        coroutineScope.launch {

            val user = db.getUser(username)
            if (user != null) {
                withContext(Dispatchers.Main) {
                    error.value = "User already Exists"
                }
            } else {
                val getAll = generateDummyTasks()
                val user = User(username, password.hashCode(), info,getAll)
                val userID = db.insertUser(user)
                user.id = userID
                LoginState.login(user)
                withContext(Dispatchers.Main) {
                    signupComplete.value = true
                }
            }
        }
    }

    private fun generateDummyTasks(): List<Tasks> {
        Tasks.add(Tasks("Daily Routine", null,default))
        Tasks.add(Tasks("Office Tasks", null,default))
        Tasks.add(Tasks("Grocery List", null,default))

        return Tasks
    }
}