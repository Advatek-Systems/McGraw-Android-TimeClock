package com.example.mcgrawtimeclock.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.mcgrawtimeclock.entity.EmployeeEntity

@Dao
interface EmployeeDAO {
    @Query("SELECT * FROM Employee")
    fun getAllEmployees(): List<EmployeeEntity>

    @Query("INSERT INTO Employee(EmplNo, Card, FirstName, LastName, RegDept, Supervisor, Category) VALUES (:emplNo, :card, :firstName, :lastName, :regDept, :supervisor, :category)")
    fun insertEmployee(emplNo: Int, card: Int, firstName: String, lastName: String, regDept: Int, supervisor: Int, category: String)

    @Query("SELECT COUNT(*) FROM Employee WHERE EmplNo = :emplNo")
    fun doesEmployeeExist(emplNo: Int): Int

    @Query("SELECT COUNT(*) FROM Employee WHERE Card = :cardNo")
    fun validateCardNo(cardNo: Int): Int

    @Query("UPDATE Employee SET Card = :card, FirstName = :firstName, LastName = :lastName, RegDept = :regDept, Supervisor = :supervisor, Category = :category WHERE EmplNo = :emplNo")
    fun updateEmployee(emplNo: Int, card: Int, firstName: String, lastName: String, regDept: Int, supervisor: Int, category: String)

    @Query("SELECT FirstName FROM Employee WHERE Card = :card")
    fun getEmployeeName(card: Int): String

    @Query("SELECT COUNT(*) FROM Employee WHERE Card = :card AND Category LIKE '%SUPER%'")
    fun isEmployeeSupervisor(card: Int): Int

    @Query("SELECT COALESCE(RegDept, -1) FROM Employee WHERE Card = :card")
    fun getEmployeeRegDept(card: Int): Int
}