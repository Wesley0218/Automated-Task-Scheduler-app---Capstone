package com.example.automatedtaskscheduler

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.automatedtaskscheduler.databinding.ActivityMainBinding


public const val SPLASH_TIME = 4000L

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val firstTime = 0


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
        )
        Handler(Looper.myLooper()!!).postDelayed(
            {
                val prefsName = "MyPrefsFile"

                val settings = getSharedPreferences(prefsName, 0)

                if (settings.getBoolean("my_first_time", true)) {
                    //the app is being launched for first time, do something
                    Log.d("Comments", "First time")

                    val intent = Intent(this, Setup::class.java)
                    startActivity(intent)
                    finish()

                    // first time task

                    // record the fact that the app has been started at least once
                    settings.edit().putBoolean("my_first_time", false).commit()
                }
               else{
                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                    finish()
                    /*val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                    finish()*/
                }
            },
            SPLASH_TIME
        )

        val fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation)

        binding.ivErosLogo.animation = fadeInAnim
    }


}