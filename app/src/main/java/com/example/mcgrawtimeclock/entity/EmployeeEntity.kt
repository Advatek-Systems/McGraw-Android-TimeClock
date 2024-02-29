package com.example.mcgrawtimeclock.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Employee")
data class EmployeeEntity(
    @SerializedName("EmplNo") @ColumnInfo("EmplNo") @PrimaryKey(autoGenerate = false) val emplNo: Int,
    @SerializedName("Card") @ColumnInfo("Card") val card: Int,
    @SerializedName("First_Name") @ColumnInfo("FirstName") val firstName: String,
    @SerializedName("Last_Name") @ColumnInfo("LastName") val lastName: String,
    @SerializedName("Reg_Dept") @ColumnInfo("RegDept") val regDept: Int,
    @SerializedName("Supervisor") @ColumnInfo("Supervisor") val supervisor: Int,
    @SerializedName("Category") @ColumnInfo("Category") val category: String
)