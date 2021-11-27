package com.reephub.praeter.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.reephub.praeter.data.local.dao.UserDao
import com.reephub.praeter.data.local.model.User

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
abstract class PraeterDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao

    companion object {
        const val DATABASE_NAME = "praeter_db"
    }
}