package com.example.patientassistant.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.patientassistant.Model.Appointment
import com.example.patientassistant.Model.Drug
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Drug::class, Appointment::class], version = 1)
abstract class PatientDatabase : RoomDatabase(){

    abstract fun getDrugDao() : DrugDAO
    abstract fun getAppointmentDao() : AppointmentDAO

    //Singleton
    companion object {

        @Volatile //volatile means - make the instance to be created from this class visible to all the other threads
        private var INSTANCE : PatientDatabase? = null

        fun getDataBase(context: Context, scope: CoroutineScope) : PatientDatabase {
            return INSTANCE ?: synchronized(this) {  //synchronized means that if more than one thread tries to create an instance of the database at the same time, it's gonna block it

                val instance = Room.databaseBuilder(context.applicationContext,
                    PatientDatabase::class.java,"database")
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}
