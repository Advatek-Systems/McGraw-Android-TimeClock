package com.example.mcgrawtimeclock.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.mcgrawtimeclock.entity.EmployeeEntity

@Dao
interface EmployeeDAO {
    @Query("SELECT * FROM Employee")
    fun getAllEmployees(): List<EmployeeEntity>

    @Query("INSERT INTO Employee(EmplNo, Card, FirstName, LastName, RegDept, Supervisor) VALUES (:emplNo, :card, :firstName, :lastName, :regDept, :supervisor)")
    fun insertEmployee(emplNo: Int, card: Int, firstName: String, lastName: String, regDept: Int, supervisor: Int)

    @Query("SELECT COUNT(*) FROM Employee WHERE EmplNo = :emplNo")
    fun doesEmployeeExist(emplNo: Int): Int

    @Query("UPDATE Employee SET Card = :card, FirstName = :firstName, LastName = :lastName, RegDept = :regDept, Supervisor = :supervisor WHERE EmplNo = :emplNo")
    fun updateEmployee(emplNo: Int, card: Int, firstName: String, lastName: String, regDept: Int, supervisor: Int)

    @Query("SELECT COALESCE(FirstName + ' ' + LastName, 'UNKNOWN') FROM Employee WHERE EmplNo = :emplNo AND Card = :card")
    fun getEmployeeName(emplNo: Int, card: Int): String

    @Query("SELECT COALESCE(RegDept, 0) FROM Employee WHERE EmplNo = :emplNo AND Card = :card")
    fun getEmployeeRegDept(emplNo: Int, card: Int): String
}