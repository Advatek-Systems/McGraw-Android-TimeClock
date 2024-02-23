package com.example.mcgrawtimeclock.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.mcgrawtimeclock.entity.FunctionEntity

@Dao
interface FunctionDAO {
    @Query("SELECT * FROM Function")
    fun getAllFunctions(): List<FunctionEntity>

    @Query("INSERT INTO Function(FunctionNo, Description, Enabled, DepartmentNo) VALUES (:funcNo, :desc, :enabled, :deptNo)")
    fun insertFunction(funcNo: Int, desc: String, enabled: String, deptNo: Int)
}