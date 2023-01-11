package com.example.patientassistant.Room

import androidx.room.*
import com.example.patientassistant.Model.Weight
import com.example.patientassistant.Objects.TableNames
import kotlinx.coroutines.flow.Flow

@Dao
interface WeightDAO {

    @Insert
    suspend fun insert(weight: Weight)

    @Update
    suspend fun update(weight: Weight)

    @Delete
    suspend fun delete(weight: Weight)

    @Query("SELECT * FROM ${TableNames.weightTable}")
    fun getAllWeight(): Flow<List<Weight>>

    @Query("DELETE FROM ${TableNames.weightTable}")
    suspend fun deleteAllWeight()
}