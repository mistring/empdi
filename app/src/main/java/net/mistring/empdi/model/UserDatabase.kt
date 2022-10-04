package net.mistring.empdi.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(EmployeeEntry::class), version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}