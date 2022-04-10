package com.neutron.alterpay.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.neutron.alterpay.data.model.Business

@Database(entities = [Business::class], version = 1, exportSchema = false)
abstract class AppDatabase(): RoomDatabase() {
    companion object{
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java, "cue_db")
                    .allowMainThreadQueries()
                    .build()
            }

            return INSTANCE!!
        }
    }

    abstract fun businessDao(): BusinessDao

}