package com.bignerdranch.android.findshelter

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_details.*

// ----- DetailsActivity class with extended AppCompatActivity -----

class DetailsActivity : AppCompatActivity()
{
    private var shelter: Shelter? = null // private shelter object instance declaration

    private val shelterListViewModel: ShelterListViewModel by lazy { ViewModelProviders.of(this).get(
        ShelterListViewModel::class.java)
    }

    // ----- onCreate method declaration -----

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        shelterListViewModel.initData(this)

        val intent = this.intent // Calling the same activity screen intent

        // ----- Loading the data of shelter from database -----

        if (intent != null && intent.hasExtra(EXTRA_SHELTER) )
        {
            shelter = intent.getSerializableExtra(EXTRA_SHELTER) as Shelter // Getting serialize data into listview
        }

        // ----- Error message if the error occurs from fetching the data from database -----

        if (shelter == null)
        {
            Toast.makeText(this, R.string.shelter_details_load_error, Toast.LENGTH_LONG).show() // display error on toast
            return
        }

        // ----- Setting the details of the shelter -----

        shelter_name.setText(shelter?.name)
        shelter_address.setText(shelter?.address)
        shelter_suburb.setText(shelter?.suburb)
        shelter_phone.setText(shelter?.phone)
        shelter_email.setText(shelter?.email)
    }

    // ----- onBackPressed method declaration ------

    override fun onBackPressed()
    {
        // ----- Setting the edited text in the variable -----

        val editedShelter=Shelter(
            name = shelter_name.text.toString(),
            address = shelter_address.text.toString(),
            suburb = shelter_suburb.text.toString(),
            phone = shelter_phone.text.toString(),
            email = shelter_email.text.toString(),
            shelter!!.latitude,
            shelter!!.longitude
        )

        // ----- Updating the phone number and email in the database on pressback if data change -----

        if(shelter?.phone != editedShelter.phone ||shelter?.email!=editedShelter.email) {
            editedShelter.id = shelter?.id!!
            Thread {
                shelterListViewModel.editShelter(editedShelter)

            }.start()
        }
        super.onBackPressed()
    }

    // ----- Email button pressed function -----

    fun emailShelter(view: View)
    {
        // ----- checking if the name and email is not empty -----

        if(shelter?.name!!.isNotEmpty() && shelter?.email!!.isNotEmpty())
        {
            val it = Intent(Intent.ACTION_SEND) // variable for intent to sent the mail
            it.data = Uri.parse("mailto:")
            val to = arrayOf(shelter_email.text.toString())
            it.putExtra(Intent.EXTRA_EMAIL, to)
            it.putExtra(Intent.EXTRA_SUBJECT, "${getString(R.string.email_title)}") // Setting the title from string resource
            it.putExtra(Intent.EXTRA_TEXT, "${getString(R.string.email_message)} ${shelter_name.text}") // Setting the message from string resource
            it.type = "vnd.android.cursor.item/email"
            startActivity(Intent.createChooser(it, "Choose"))
        }

        // ----- error message if name or email is empty -----

        else
            Toast.makeText(this,"Sorry, email or name is empty",Toast.LENGTH_LONG).show()
    }

    fun showOnMap(view: View)
    {
        // ---- checking if the latitude and longitude is not null -----

        if(shelter!!.latitude!=null && shelter!!.longitude!=null)
        {
            // ----- asking the permission and updating the location -----

            with(ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION),103)){
            val intent = Intent(this@DetailsActivity, MapsActivity::class.java)
            intent.putExtra(EXTRA_SHELTER, shelter)
            startActivity(intent)

        }
    }
    // ---- error message if the location is not available -----
    else
        Toast.makeText(this,"Sorry, Location is not available",Toast.LENGTH_LONG).show()
    }
}