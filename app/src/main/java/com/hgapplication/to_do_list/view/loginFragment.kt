package com.hgapplication.to_do_list.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.hgapplication.to_do_list.R
import com.hgapplication.to_do_list.viewmodel.loginViewModel


class loginFragment : Fragment() {

    private lateinit var viewModel:loginViewModel
    private lateinit var loginButton:Button
    private lateinit var goToSignUpfromLogin:Button
    private lateinit var usernameLogin:EditText
    private lateinit var passwordLogin:EditText
    val num:Float = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater2 = TransitionInflater.from(requireContext())
        exitTransition = inflater2.inflateTransition(R.transition.fade)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val Loginprefs = activity?.getSharedPreferences("ld", 0);
        val userLoginStatus = Loginprefs?.getString("uS", "{}");
        //val loginModel = ViewModelProvider(this)[loginViewModel::class.java]

        //val gson: Gson = Gson()
        //val obj: User = gson.fromJson(userLoginStatus, User::class.java)
        viewModel = androidx.lifecycle.ViewModelProvider(this)[loginViewModel::class.java]

        viewModel.loginAgain(userLoginStatus.toString())

        loginButton = view.findViewById(R.id.loginButton)
        goToSignUpfromLogin = view.findViewById(R.id.goToSignUpFromLogin)
        usernameLogin = view.findViewById(R.id.usernameLogin)
        passwordLogin = view.findViewById(R.id.passwordLogin)

        loginButton.setOnClickListener { onLogin(it) }
        goToSignUpfromLogin.setOnClickListener { onGoSignUp(it) }
        viewModel = androidx.lifecycle.ViewModelProvider(this)[loginViewModel::class.java]
        observeViewModel()
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowCustomEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        val li = LayoutInflater.from(activity)
        val customView: View = li.inflate(R.layout.login_menu, null)
        (activity as AppCompatActivity).supportActionBar?.setCustomView(customView)
        customView.findViewById<TextView>(R.id.title).text = "To Do List"
        (activity as AppCompatActivity).supportActionBar?.elevation = num


        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFDE03")))
        (activity as AppCompatActivity).window.statusBarColor = Color.parseColor("#c7ad00")

    }
    private fun observeViewModel(){
        viewModel.loginComplete.observe(viewLifecycleOwner, Observer { isComplete->
            Toast.makeText(activity, "Signed In", Toast.LENGTH_SHORT).show()
            Navigation.findNavController(usernameLogin).navigate(R.id.actionGoToMain)
        })
        viewModel.error.observe(viewLifecycleOwner, Observer { error->
            Toast.makeText(activity, "Error : $error", Toast.LENGTH_SHORT).show()

        })
    }

    private fun onLogin(v:View){
        val username = usernameLogin.text.toString()
        val password = passwordLogin.text.toString()
        if(username.isEmpty() || password.isEmpty()){
            Toast.makeText(activity, "Please Fill all fields", Toast.LENGTH_SHORT).show()
        }
        else{
            viewModel.login(username, password)
        }

    }
    private fun onGoSignUp(v:View){
        Navigation.findNavController(v).navigate(R.id.signupFragment)
    }

}