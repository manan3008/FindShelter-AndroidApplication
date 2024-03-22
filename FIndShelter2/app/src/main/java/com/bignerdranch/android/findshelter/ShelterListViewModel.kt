package com.bignerdranch.android.findshelter

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

// ---- List view model provided and updated ------

class ShelterListViewModel : ViewModel() {
   private var database:ShelterDatabase?=null

    init
    {
        // For testing purpose declaration
    }
    fun initData(context: Context)
    {
        database= ShelterDatabase.getInstance(context = context)!!
    }

    public  fun addShelter(shelter: Shelter)
    {
        database!!.addShelter(shelter = shelter)
    }
    public  fun loadAllShelters(): LiveData<MutableList<Shelter>>
    {
        return database!!.shelterDao().loadShelters()
    }

    fun editShelter(shelter: Shelter)
    {
        println("EDITING  ${shelter.id}")
        database!!.shelterDao().update(shelter.id,name = shelter.name,email = shelter.email,phone = shelter.phone,suburb = shelter.suburb,address = shelter.address)
    }
}

