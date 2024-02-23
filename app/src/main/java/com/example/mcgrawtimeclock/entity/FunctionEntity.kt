package com.example.mcgrawtimeclock.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Function")
data class FunctionEntity (
    @SerializedName("FunctionNo") @ColumnInfo("FunctionNo") @PrimaryKey(autoGenerate = false) val functionNo: Int,
    @SerializedName("Description") @ColumnInfo("Description") val description: Int,
    @SerializedName("Enabled") @ColumnInfo("Enabled") val enabled: Int,
    @SerializedName("DepartmentNo") @ColumnInfo("DepartmentNo") val departmentNo: Int
)