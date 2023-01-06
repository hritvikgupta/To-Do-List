package com.example.to_do_list.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.to_do_list.R
import com.example.to_do_list.viewmodel.MainViewModel
import com.example.to_do_list.viewmodel.signUpViewModel


class signUpFragment : Fragment() {

    private lateinit var signUpButton:Button
    private lateinit var viewModel:signUpViewModel
    private lateinit var goToLoginFromSignUp:Button
    private lateinit var usernameSignUp:EditText
    private lateinit var passwordSignUp:EditText
    private lateinit var confirmPasswordSignUp:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signUpButton = view.findViewById(R.id.signUpButton)
        goToLoginFromSignUp = view.findViewById(R.id.goToLoginfromSignUp)
        usernameSignUp = view.findViewById(R.id.usernameSignUp)
        passwordSignUp = view.findViewById(R.id.passwordSignUp)
        confirmPasswordSignUp = view.findViewById(R.id.confirmpasswordSignUp)

        (activity as AppCompatActivity).supportActionBar?.setDisplayShowCustomEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)

        (activity as AppCompatActivity).supportActionBar?.title = "To Do List"

        signUpButton.setOnClickListener { onSignUp(it) }
        goToLoginFromSignUp.setOnClickListener { onGotoLogin(it) }
        viewModel = androidx.lifecycle.ViewModelProvider(this)[signUpViewModel::class.java]
        observeModelView()

    }
    private fun observeModelView(){
        viewModel.signupComplete.observe(viewLifecycleOwner, Observer { isComplete->
            Toast.makeText(activity, "SignUp Complete", Toast.LENGTH_SHORT).show()
            Navigation.findNavController(usernameSignUp).navigate(R.id.actionGoToMain)

        })

        viewModel.error.observe(viewLifecycleOwner, Observer { error->
            Toast.makeText(activity, "Error : $error", Toast.LENGTH_SHORT).show()
        })
    }

    private fun onSignUp(v:View)
    {
        val username = usernameSignUp.text.toString()
        val password = passwordSignUp.text.toString()
        val otherInfo = confirmPasswordSignUp.text.toString()
        if(username.isEmpty() || password.isEmpty()){
            Toast.makeText(activity, "Please Fill all fields", Toast.LENGTH_SHORT).show()
        }
        else{
            viewModel.signup(username, password, otherInfo)
        }
    }
    private fun onGotoLogin(v:View){
        Navigation.findNavController(v).navigate(R.id.actionGoToLogin)
    }


}