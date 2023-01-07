package com.hgapplication.to_do_list.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.hgapplication.to_do_list.R
import com.hgapplication.to_do_list.model.User
import com.hgapplication.to_do_list.viewmodel.signUpViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


class signUpFragment : Fragment() {

    private lateinit var signUpButton:Button
    private lateinit var viewModel:signUpViewModel
    //private lateinit var loginViewModel:loginViewModel
    private lateinit var goToLoginFromSignUp:Button
    private lateinit var usernameSignUp:EditText
    private lateinit var passwordSignUp:EditText
    private lateinit var confirmPasswordSignUp:EditText

    private lateinit var googleLogin:ImageView
    private lateinit var facebookLogin:ImageView
    val num:Float = 0f

    val user: User?=null

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
        googleLogin = view.findViewById(R.id.googleLogin)
        facebookLogin = view.findViewById(R.id.facebookLogin)
        googleLogin.setOnClickListener{
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        }

        (activity as AppCompatActivity).supportActionBar?.setDisplayShowCustomEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        val li = LayoutInflater.from(activity)
        val customView: View = li.inflate(R.layout.login_menu, null)
        (activity as AppCompatActivity).supportActionBar?.setCustomView(customView)
        customView.findViewById<TextView>(R.id.title).text = "To Do List"
        (activity as AppCompatActivity).supportActionBar?.elevation = num


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
        else if (password != otherInfo){
            Toast.makeText(activity, "Passwords does not matches", Toast.LENGTH_SHORT).show()
        }
        else{
            viewModel.signup(username, password, otherInfo)
        }
    }
    private fun onGotoLogin(v:View){
        Navigation.findNavController(v).navigate(R.id.actionGoToLogin)
    }
    private fun onGotoMain(v:View){
        Navigation.findNavController(v).navigate(R.id.actionGoToMain)
    }


}