package com.bignerdranch.android.findshelter

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// ----- Shekter database class extending the RoomDatabase class -----

@Database(entities = [Shelter::class], version = 1)
abstract class ShelterDatabase : RoomDatabase()
{
    abstract fun shelterDao():ShelterDao

    companion object
    {
        private var instance: ShelterDatabase? = null // Creating the shelter database instance
        fun getInstance(context: Context): ShelterDatabase?
        {
            // ----- Checking if the instance is null -----

            if (instance == null)
            {
                synchronized(ShelterDatabase::class)
                {
                    // ----- Setting the room database builder ------

                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ShelterDatabase::class.java, "shelter" // Created database shelter
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return instance // returning the instance variable data
        }
    }

    // adding the data in shelter database from shelterDao interface declared in shelter class
    fun addShelter(shelter: Shelter)
    {
        shelterDao().insert(shelter)
    }
}