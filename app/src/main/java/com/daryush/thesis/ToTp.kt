package com.daryush.thesis

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "totp")
data class ToTp(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val secret: String,
    val IssuerName: String,
    val label: String,

)