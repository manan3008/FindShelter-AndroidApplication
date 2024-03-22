package com.bignerdranch.android.findshelter

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*
import java.io.Serializable                 // Must add Java Serializable import manually

// ----- Class shelter declaration -----

@Entity(tableName = "shelter")
// ---- constructor declaration for getting the details from view model -----

data class Shelter(
                   var name: String = "",
                   var address: String = "",
                   var suburb: String = "",
                   var phone: String = "",
                   var email: String = "",
                   val latitude:Double?,
                   val longitude:Double?
                   ) : Serializable{
    @PrimaryKey(autoGenerate  = true)
    var id: Int = 0
}

// ----- Interface declaration for update and select queries -----
@Dao
interface  ShelterDao
{
    @Insert(onConflict = OnConflictStrategy.ABORT)
    // Function insert declaration

    fun insert(shelter:Shelter)
    @Query("UPDATE shelter SET name = :name, address = :address, suburb = :suburb,phone= :phone, email = :email WHERE id == :id;") //update query

    // Function update declaration
    fun update(id:Int,name:String,email:String,address: String,suburb: String,phone: String)

    @Query("SELECT * FROM shelter") // select all query

    // Creating the mutable list using the LiveData
    fun loadShelters():LiveData<MutableList<Shelter>>
}

