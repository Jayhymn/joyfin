package com.wakeupdev.joyfin

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.wakeupdev.joyfin.databinding.ActivityMainBinding
import com.wakeupdev.joyfin.models.networkres.ApiResponse
import com.wakeupdev.joyfin.models.networkres.Transaction
import com.wakeupdev.joyfin.models.networkres.UserData

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiResponse = intent.getParcelableExtra("responseBody") as? ApiResponse

//        val apiResponse = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            intent.getParcelableExtra("responseBody", ApiResponse::class.java)
//        } else {
//            intent.getParcelableExtra("responseBody")
//        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_send, R.id.navigation_credit_card, R.id.navigation_profile
            )
        )

//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        if (apiResponse != null) {
            val action = navController.navigate(R.id.navigation_home, Bundle().apply { putParcelable("apiResponse", apiResponse) })

//            navController.navigate(action)
        }
    }

    private fun hasArguments(intent: Intent): Boolean {
        val bundle = intent.extras
        return bundle != null && bundle.containsKey("userData") // Check for the argument name
    }
}