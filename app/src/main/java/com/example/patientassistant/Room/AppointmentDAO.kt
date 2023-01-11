package com.example.patientassistant.Room

import androidx.room.*
import com.example.patientassistant.Model.Appointment
import com.example.patientassistant.Objects.TableNames
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentDAO {

    @Insert
    suspend fun insert(appointment: Appointment)

    @Query("SELECT * FROM ${TableNames.appointmentTable} ORDER BY id ASC")
    fun getAllAppointments(): Flow<List<Appointment>>

    @Update
    suspend fun update(appointment: Appointment)

    @Query("SELECT * FROM ${TableNames.appointmentTable} WHERE year=:yearToday AND month=:monthToday AND day=:dayToday ORDER BY year, month, day, hour, minute ASC")
    fun todayAppointments(yearToday: Int, monthToday: Int, dayToday: Int): Flow<List<Appointment>>

    @Delete
    suspend fun delete(appointment: Appointment)

    @Query("DELETE FROM ${TableNames.appointmentTable}")
    suspend fun deleteAllAppointments()
}
