// SplashActivity.kt

package com.example.autoinsight

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var topAnim: Animation
    private lateinit var bottomAnim: Animation
    private lateinit var splashLogo: ImageView
    private lateinit var autoStringsLogo: ImageView
    private var splashScreen: Int = 5000

    private lateinit var firebaseAuth: FirebaseAuth
    private var networkReceiver: NetworkReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        splashLogo = this.findViewById(R.id.splashLogo)
        autoStringsLogo = this.findViewById(R.id.autoStringsLogo)

        splashLogo.animation = topAnim
        autoStringsLogo.animation = bottomAnim

        firebaseAuth = FirebaseAuth.getInstance()

        // Check internet connectivity
        if (isNetworkAvailable()) {
            // Check if a user is already authenticated using Firebase
            if (firebaseAuth.currentUser != null) {
                // If a user is logged in, navigate to SelectActivity
                val intent = Intent(this, SelectActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // If not logged in, proceed to LoginActivity after the splash screen delay
                val r = Runnable {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                Handler(Looper.getMainLooper()).postDelayed(r, splashScreen.toLong())
            }
        } else {
            // Show custom dialog for no internet
            showNoInternetDialog()
        }

        // Register the network receiver to monitor network changes
        networkReceiver = NetworkReceiver()
        registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the network receiver to avoid memory leaks
        unregisterReceiver(networkReceiver)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun showNoInternetDialog() {
        val dialog = CustomDialog(this, "Please turn on the internet connection")
        dialog.show()
    }

    inner class NetworkReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (isNetworkAvailable()) {


                if (firebaseAuth.currentUser != null) {
                    // If a user is logged in, navigate to SelectActivity
                    val intent = Intent(context, SelectActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If not logged in, proceed to LoginActivity after the splash screen delay
                    val r = Runnable {
                        val intent = Intent(context, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    Handler(Looper.getMainLooper()).postDelayed(r, splashScreen.toLong())
                }






            }
        }
    }



}
