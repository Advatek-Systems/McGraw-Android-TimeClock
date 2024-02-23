package com.example.mcgrawtimeclock.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mcgrawtimeclock.dao.EmployeeDAO
import com.example.mcgrawtimeclock.entity.EmployeeEntity

@Database(entities = [EmployeeEntity::class], version = 1, exportSchema = false)
abstract class EmployeeDatabase : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDAO
}