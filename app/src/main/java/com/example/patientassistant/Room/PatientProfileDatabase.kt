package com.example.patientassistant.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.patientassistant.Model.BloodPressure
import com.example.patientassistant.Model.PatientConstants
import com.example.patientassistant.Model.Temperature
import com.example.patientassistant.Model.Weight
import kotlinx.coroutines.CoroutineScope

@Database(
    entities = [PatientConstants::class, Weight::class, Temperature::class, BloodPressure::class],
    version = 1
)
abstract class PatientProfileDatabase : RoomDatabase() {

    abstract fun getPatientConstantsDao(): PatientConstantsDAO
    abstract fun getWeightDao(): WeightDAO
    abstract fun getTemperatureDao(): TemperatureDAO
    abstract fun getBloodPressureDao(): BloodPressureDAO

    companion object {

        @Volatile //volatile means - make the instance to be created from this class visible to all the other threads
        private var INSTANCE: PatientProfileDatabase? = null

        fun getDataBase(context: Context, scope: CoroutineScope): PatientProfileDatabase {
            return INSTANCE
                ?: synchronized(this) {  //synchronized means that if more than one thread tries to create an instance of the database at the same time, it's gonna block it

                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        PatientProfileDatabase::class.java, "patient_profile_database"
                    )
                        .build()

                    INSTANCE = instance

                    instance
                }
        }
    }
}