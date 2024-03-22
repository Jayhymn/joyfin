package com.wakeupdev.joyfin.utils

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import com.google.android.material.textfield.TextInputLayout

object LoginUtils {
    fun validateInputs (emailEditText: TextInputLayout, passwordEditText: TextInputLayout): Boolean {
        var isValid = true

        val email: String = emailEditText.editText?.text.toString().trim()
        val password: String = passwordEditText.editText?.text.toString().trim()

        emailEditText.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                emailEditText.error = null
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        passwordEditText.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                passwordEditText.error = null
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        if (email.isEmpty()) {
            emailEditText.error = "Email cannot be empty"
            isValid = false
            return isValid
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "Invalid email format"
            isValid = false
            return isValid
        } else {
            emailEditText.error = null // Clear error
        }

        if (password.isEmpty()) {
            passwordEditText.error = "Password cannot be empty"
            isValid = false
            return isValid
        } else if (password.length < 5) {
            passwordEditText.error = "Password cannot be less than 5 characters"
            isValid = false
            return isValid
        } else {
            passwordEditText.error = null // Clear error
        }

        return isValid
    }
}