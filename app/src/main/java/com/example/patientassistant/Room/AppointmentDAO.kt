package com.example.patientassistant.Room

import androidx.room.*
import com.example.patientassistant.Model.Appointment

@Dao
interface AppointmentDAO {

    @Insert
    suspend fun insert(appointment: Appointment)

}
