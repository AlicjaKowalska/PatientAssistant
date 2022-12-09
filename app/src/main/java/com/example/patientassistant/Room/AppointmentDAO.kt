package com.example.patientassistant.Room

import androidx.room.*
import com.example.patientassistant.Model.Appointment
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentDAO {

    @Insert
    suspend fun insert(appointment: Appointment)

    @Query("SELECT * FROM appointment_table ORDER BY id ASC")
    fun getAllAppointments() : Flow<List<Appointment>>

    @Update
    suspend fun update(appointment: Appointment)

}
