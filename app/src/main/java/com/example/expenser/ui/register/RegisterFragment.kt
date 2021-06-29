package com.example.expenser.ui.register

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.expenser.LoginActivity
import com.example.expenser.R
import com.example.expenser.ui.login.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_register.view.*

class RegisterFragment : Fragment() {

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        val view: View = inflater.inflate(R.layout.fragment_register, container, false)

        view.register_button.setOnClickListener {
            val email = view.email_register.text.toString().trim()
            val password = view.password_register.text.toString().trim()
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(context, "Email and password can't be empty", Toast.LENGTH_SHORT).show()
            } else {
                (activity as LoginActivity).registerUser(email, password)
            }
        }

        view.text_login.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
        }

        return view
    }
}