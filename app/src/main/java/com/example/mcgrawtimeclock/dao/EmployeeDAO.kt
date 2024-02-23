package com.example.mcgrawtimeclock.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.mcgrawtimeclock.entity.EmployeeEntity

@Dao
interface EmployeeDAO {
    @Query("SELECT * FROM Employee")
    fun getAllEmployees(): List<EmployeeEntity>

    @Query("INSERT INTO Employee(EmplNo, Card, FirstName, LastName, Category, RegDept, Supervisor) VALUES (:emplNo, :card, :firstName, :lastName, :category, :regDept, :supervisor)")
    fun insertEmployee(emplNo: Int, card: Int, firstName: String, lastName: String, category: Int, regDept: Int, supervisor: Int)

    @Query("SELECT COALESCE(FirstName + ' ' + LastName, 'UNKNOWN') FROM Employee WHERE EmplNo = :emplNo AND Card = :card")
    fun getEmployeeName(emplNo: Int, card: Int): String

    @Query("SELECT COALESCE(RegDept, 0) FROM Employee WHERE EmplNo = :emplNo AND Card = :card")
    fun getEmployeeRegDept(emplNo: Int, card: Int): String
}