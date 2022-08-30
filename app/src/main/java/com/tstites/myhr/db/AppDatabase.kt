package com.tstites.myhr.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tstites.myhr.obj.Employee
import com.tstites.myhr.obj.EmployeeDao

@Database(entities = [Employee::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao
}
class DBConnection {
    fun buildDB(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "AppDB")
            .allowMainThreadQueries().build()
    }
}