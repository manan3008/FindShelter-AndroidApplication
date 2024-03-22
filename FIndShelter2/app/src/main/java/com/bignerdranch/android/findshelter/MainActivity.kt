package com.bignerdranch.android.findshelter

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat

// ----- MainActivity class declaration -----

class MainActivity : AppCompatActivity()
{
    // ---- Oncreate method declaration -----

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ----- Asking for the Fine location ------

        if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED )
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),102)

        // ----- Asking for course location -----

        if(ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        )

            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),103)


        // ----- setting the fragment container -----

        val currentFragment =
                supportFragmentManager.findFragmentById(R.id.fragment_container)

        // ---- Checking if the fragment is null ----
        if (currentFragment == null)
        {
            val fragment = ShelterListFragment.newInstance()
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit()
        }
    }
}