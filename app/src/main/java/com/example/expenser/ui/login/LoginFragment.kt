package com.example.expenser.ui.login

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.expenser.LoginActivity
import com.example.expenser.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_login.view.*


class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        val view: View = inflater.inflate(R.layout.fragment_login, container, false)

        view.login_button.setOnClickListener {
            val email = view.email_login.text.toString().trim()
            val password = view.password_login.text.toString().trim()
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(context, "Email and password can't be empty", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "$email $password", Toast.LENGTH_SHORT).show()
                (activity as LoginActivity).loginUser(email, password)
            }
        }

        view.text_register.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
        }

        return view
    }
}