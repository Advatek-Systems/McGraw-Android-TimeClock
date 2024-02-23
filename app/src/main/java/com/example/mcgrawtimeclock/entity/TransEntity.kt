package com.example.mcgrawtimeclock.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Trans")
data class TransEntity(
    @SerializedName("CardNo") @ColumnInfo("CardNo") val cardNo: Int,
    @SerializedName("Date") @ColumnInfo("Date") val date: Int,
    @SerializedName("Time") @ColumnInfo("Time") val time: Int,
    @SerializedName("Status") @ColumnInfo("Status") val status: String,
    @SerializedName("State") @ColumnInfo("State") val state: Int,
    @SerializedName("Msg") @ColumnInfo("Msg") val msg: String,
    @SerializedName("EmplNo") @ColumnInfo("EmplNo") val emplNo: String,
    @SerializedName("ScanID") @ColumnInfo("ScanID") @PrimaryKey(autoGenerate = false) val scanID: String,
    @SerializedName("PayWeek") @ColumnInfo("PayWeek") val payWeek: Int,
    @SerializedName("DeviceIP") @ColumnInfo("DeviceIP") val deviceIP: String,
    @SerializedName("Sent") @ColumnInfo("Sent") val sent: Boolean
)