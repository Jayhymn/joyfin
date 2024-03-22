package com.wakeupdev.joyfin.views

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.wakeupdev.joyfin.MainActivity
import com.wakeupdev.joyfin.R
import com.wakeupdev.joyfin.databinding.ActivityLoginBinding
import com.wakeupdev.joyfin.models.LoginCredential
import com.wakeupdev.joyfin.models.networkres.ApiResponse
import com.wakeupdev.joyfin.network.ApiClient
import com.wakeupdev.joyfin.services.AuthService
import com.wakeupdev.joyfin.utils.AlertUtils
import com.wakeupdev.joyfin.utils.LoginUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException


class LoginActivity : ApiClient.OnRequestExceptionListener, AppCompatActivity() {
    private var hasTokenExpired = false
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        ApiClient.setAuthTokenExpiredListener(this)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
//            startActivity(
//                Intent(this@LoginActivity, MainActivity::class.java)
//            )
            // Dismiss the keyboard
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)

            val isValidCredentials =
                LoginUtils.validateInputs(binding.emailInputLayout, binding.passwordInputLayout)
            if (isValidCredentials) {
                val email = binding.emailInputLayout.editText?.text.toString().trim()
                val password = binding.passwordInputLayout.editText?.text.toString().trim()
                val credentials = LoginCredential(email, password)
                performLogin(credentials)
            }
        }

        binding.passwordInputLayout.editText?.setOnEditorActionListener { _, actionId, _ ->
            // Call the method to initiate login
            binding.loginButton.callOnClick()
            return@setOnEditorActionListener true
        }
    }

    private fun performLogin(credentials: LoginCredential) {
        if (!isNetworkAvailable(this)) {
            Snackbar.make(
                binding.root,
                "Please turn on your internet then retry!",
                Snackbar.LENGTH_LONG
            ).apply {
                setAction("Dismiss") { dismiss() }
                show()
            }

            return
        }

        AlertUtils.showCustomDialog(this)

        ApiClient.retrofit.create(AuthService::class.java)
            .loginUser()
            .enqueue(object : Callback<ApiResponse?> {
                override fun onResponse(call: Call<ApiResponse?>, response: Response<ApiResponse?>) {
                    AlertUtils.cancelCustomDialog()

                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            if (credentials.username != responseBody.userData?.email){
                                binding.emailInputLayout.error = "You've entered a wrong email address"
                                return
                            }
                            if (credentials.password != "herconomy"){
                                binding.passwordInputLayout.error = "incorrect password inputted"
                                return
                            }

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("responseBody", responseBody)  // Pass userData as Parcelable
                            startActivity(intent)
                        } else {
                            // Handle case where response body is null (e.g., empty response)
                            Snackbar.make(
                                binding.root,
                                "An unexpected error occurred. Please try again later.",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "An error occurred."
                        Snackbar.make(
                            binding.root,
                            errorMessage,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse?>, error: Throwable) {
                    AlertUtils.cancelCustomDialog()

                    val errorMessage = when (error) {
                        is IOException -> "Network error. Please check your internet connection and try again."
                        is HttpException -> error.message() ?: "An unknown HTTP error occurred."
                        else -> "An unknown error has occurred. Please retry again."
                    }

                    Snackbar.make(
                        binding.root,
                        errorMessage,
                        Snackbar.LENGTH_LONG
                    ).apply {
                        setAction("Dismiss") { dismiss() }
                        show()
                    }
                }
            })
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        connectivityManager?.let { manager ->
            val network = manager.activeNetwork
            val capabilities = manager.getNetworkCapabilities(network)
            return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
        }
        return false
    }


    override fun onAuthTokenExpired() {
        if (hasTokenExpired) {
            return
        }
        hasTokenExpired = true
    }

    override fun onSessionTimeout() {

    }
}