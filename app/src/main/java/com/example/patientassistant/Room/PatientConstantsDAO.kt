package com.example.patientassistant.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.patientassistant.Model.PatientConstants

@Dao
interface PatientConstantsDAO {

    @Insert
    suspend fun insert(patientConstants: PatientConstants)

    @Update
    suspend fun update(patientConstants: PatientConstants)

    @Delete
    suspend fun delete(patientConstants: PatientConstants)
}