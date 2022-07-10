package com.example.automatedtaskscheduler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.automatedtaskscheduler.fragments.AddFragment
import com.example.automatedtaskscheduler.fragments.EditFragment
import com.example.automatedtaskscheduler.fragments.HomeFragment
import com.example.automatedtaskscheduler.fragments.SettingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val homeFragment  = HomeFragment()
        val addFragment = AddFragment()
        val editFragment = EditFragment()
        val settingFragment = SettingFragment()

        makeCurrentFragment(homeFragment)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener(){
            when (it.itemId){
                R.id.ic_home -> makeCurrentFragment(homeFragment)
                R.id.ic_add -> makeCurrentFragment(addFragment)
                R.id.ic_edit -> makeCurrentFragment(editFragment)
                R.id.ic_setting -> makeCurrentFragment(settingFragment)
            }
            true
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}