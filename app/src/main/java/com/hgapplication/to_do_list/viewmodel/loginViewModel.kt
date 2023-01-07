package com.hgapplication.to_do_list.viewmodel

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hgapplication.to_do_list.model.LoginState
import com.hgapplication.to_do_list.model.UserDatabase
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class loginViewModel(application: Application):AndroidViewModel(application) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val db by lazy { UserDatabase(getApplication()).userDao() }

    val loginComplete = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    fun login(username: String, password: String) {

        coroutineScope.launch {
            val user = db.getUser(username)
            if (user == null) {
                withContext(Dispatchers.Main) {
                    error.value = "User Does Not Exists"
                }

            } else {
                if (user.passwordHash == password.hashCode()) {
                    LoginState.login(user)
                    storeUser(username)
                    withContext(Dispatchers.Main) {
                        loginComplete.value = true
                    }
                }else{
                    withContext(Dispatchers.Main){
                        error.value = "Password Is Incorrect"
                    }
                }

            }
        }
    }

    fun storeUser(username: String){
        val prefs = getApplication<Application>().applicationContext.getSharedPreferences("ld",0)
        val edit: SharedPreferences.Editor = prefs.edit()
        val gson:Gson = Gson()
        //val store_user = gson.toJson(user)
        edit.putString("uS", username)
        edit.apply()
    }

    fun loginAgain(username:String){
        coroutineScope.launch {
            val user = db.getUser(username)
            if (user != null) {
                    LoginState.login(user)
                    //storeUser(user)
                    withContext(Dispatchers.Main) {
                        loginComplete.value = true
                    }


            }
        }
}
}