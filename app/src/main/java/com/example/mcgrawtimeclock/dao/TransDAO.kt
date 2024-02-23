package com.example.mcgrawtimeclock.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.mcgrawtimeclock.entity.TransEntity

@Dao
interface TransDAO {
    @Query("SELECT * FROM Trans WHERE Sent = 0")
    fun getUnsentTransRecords(): List<TransEntity>

    @Query("INSERT INTO Trans(CardNo, Date, Time, Status, State, Msg, EmplNo, ScanID, PayWeek, DeviceIP, Sent) VALUES (:cardNo, :date, :time, :status, 0, :msg, :emplNo, :scanID, :payWeek, :deviceIP, 0)")
    fun insertTransRecord(cardNo: Int, date: Int, time: Int, status: String, msg: String, emplNo: String, scanID: String, payWeek: Int, deviceIP: String)

    @Query("UPDATE Trans SET Sent = 1 WHERE ScanID = :scanID")
    fun updateSentStatus(scanID: String)
}