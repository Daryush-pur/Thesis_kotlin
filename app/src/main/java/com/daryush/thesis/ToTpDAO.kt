package com.daryush.thesis

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface ToTpDAO {
    @Query("SELECT * FROM totp")
    fun getAll(): List<ToTp>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(totp: ToTp)

    @Delete
    fun delete(totp: ToTp)
}