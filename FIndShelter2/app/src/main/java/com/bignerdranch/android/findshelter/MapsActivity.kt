package com.bignerdranch.android.findshelter

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Tasks.await

// ----- Maps activity declaration with extended AppCompatActivity and OnMapReadyCallback -----

class MapsActivity : AppCompatActivity(), OnMapReadyCallback
{

    private lateinit var mMap: GoogleMap // Google map instance object declaration
    private  var shelter:Shelter?=null // Shelter instance object declaration

    // ----- onCreate method declaration -----

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // ----- Setting up the GPS listener -----

        GpsUtils(this).turnGPSOn(object : GpsUtils.onGpsListener {
        override fun gpsStatus(isGPSEnable: Boolean) {
    }

});
        shelter= intent.getSerializableExtra(EXTRA_SHELTER) as Shelter
        // ------ Obtain the SupportMapFragment and get notified when the map is ready to be used. ------
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // ----- Condition for Accessing the fine location -----

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED )
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),102)

        // ----- Accessing the coarse location -----

        if(ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        )

            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),103)

        // ----- Fetching the current location -----

        mMap.isMyLocationEnabled=true
        val myMarkerOptions=MarkerOptions()
        myMarkerOptions.title("You are here") // Setting the message when user click on balloon mark
        myMarkerOptions.position(LatLng(0.0,0.0))

        val myMarker=mMap.addMarker(myMarkerOptions) // Variable for adding the marker
        val fusedLocationProviderClient=FusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(LocationRequest.create(),object :
            LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
             myMarker!!.position= LatLng(p0.locations[0].latitude,p0.locations[0].longitude)
            }
                               },
            Looper.getMainLooper())
        val  shelterMarker=MarkerOptions()
        shelterMarker.title(shelter?.name)
        val shelterLatlng=LatLng(shelter!!.latitude!!, shelter!!.longitude!!)
        shelterMarker.position(shelterLatlng)

        // Adding the marker at the sherter position

        mMap.addMarker(shelterMarker)

        // Setting the Zoom factor 14

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(shelterLatlng,14f))
    }
}

class GpsUtils(private val context: Context)
{
    private val mSettingsClient: SettingsClient = LocationServices.getSettingsClient(context)
    private val mLocationSettingsRequest: LocationSettingsRequest
    private val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val locationRequest: LocationRequest = LocationRequest.create()

    // ----- method for turn on GPS ------

    fun turnGPSOn(onGpsListener: onGpsListener?) {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            onGpsListener?.gpsStatus(true)
        } else {
            mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener((context as Activity)) { //  GPS is already enable, callback GPS status through listener
                    onGpsListener?.gpsStatus(true)
                }
                    // ----- Failure declaration of the map location fetching -----

                .addOnFailureListener(context) { e ->
                    val statusCode = (e as ApiException).statusCode
                    when (statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(context, AppConstants.GPS_REQUEST)
                        } catch (sie: IntentSender.SendIntentException) {
                            Log.i(ContentValues.TAG, "PendingIntent unable to execute request.")
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            val errorMessage = "Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in Settings."
                            Log.e(ContentValues.TAG, errorMessage)
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }
    }

    interface onGpsListener {
        fun gpsStatus(isGPSEnable: Boolean)
    }

    init {
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10 * 1000.toLong()
        locationRequest.fastestInterval = 2 * 1000.toLong()
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        mLocationSettingsRequest = builder.build()
        builder.setAlwaysShow(true) //this is the key ingredient
    }
}


class AppConstants
{
    companion object
    {
        const val GPS_REQUEST = 105
    }

}