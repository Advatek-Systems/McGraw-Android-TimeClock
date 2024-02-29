package com.example.mcgrawtimeclock.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Trans")
data class TransEntity(
    @SerializedName("TransID") @ColumnInfo("TransID") @PrimaryKey(autoGenerate = true) val transID: Int,
    @SerializedName("CardNo") @ColumnInfo("CardNo") val cardNo: Int,
    @SerializedName("Date") @ColumnInfo("Date") val date: Int,
    @SerializedName("Time") @ColumnInfo("Time") val time: Int,
    @SerializedName("Status") @ColumnInfo("Status") val status: String,
    @SerializedName("Msg") @ColumnInfo("Msg") val msg: String,
    @SerializedName("DeviceIP") @ColumnInfo("DeviceIP") val deviceIP: String,
    @SerializedName("Sent") @ColumnInfo("Sent") val sent: Boolean
)