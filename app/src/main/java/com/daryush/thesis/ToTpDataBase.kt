package com.daryush.thesis

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [ToTp::class], version = 1)
abstract class ToTpDataBase : RoomDatabase() {
    abstract fun toTpDao(): ToTpDAO

    companion object {
        @Volatile
        private var INSTANCE: ToTpDataBase? = null

        fun getAppDatabase(context: Context): ToTpDataBase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ToTpDataBase::class.java,
                    "totp-database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
