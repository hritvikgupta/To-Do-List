package com.example.to_do_list.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.CreationExtras.Empty.get
import androidx.navigation.Navigation
import com.example.to_do_list.R
import com.example.to_do_list.viewmodel.loginViewModel
import com.example.to_do_list.viewmodel.signUpViewModel
import kotlin.coroutines.EmptyCoroutineContext.get


class loginFragment : Fragment() {

    private lateinit var viewModel:loginViewModel
    private lateinit var loginButton:Button
    private lateinit var goToSignUpfromLogin:Button
    private lateinit var usernameLogin:EditText
    private lateinit var passwordLogin:EditText

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
        loginButton = view.findViewById(R.id.loginButton)
        goToSignUpfromLogin = view.findViewById(R.id.goToSignUpFromLogin)
        usernameLogin = view.findViewById(R.id.usernameLogin)
        passwordLogin = view.findViewById(R.id.passwordLogin)

        loginButton.setOnClickListener { onLogin(it) }
        goToSignUpfromLogin.setOnClickListener { onGoSignUp(it) }
        viewModel = androidx.lifecycle.ViewModelProvider(this)[loginViewModel::class.java]
        observeViewModel()
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