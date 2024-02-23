package com.example.mcgrawtimeclock.api

import com.example.mcgrawtimeclock.entity.EmployeeEntity
import com.example.mcgrawtimeclock.entity.FunctionEntity
import com.example.mcgrawtimeclock.entity.TransEntity
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("getEmployees")
    suspend fun getEmployees(): Response<List<EmployeeEntity>>

    @GET("getFunctions")
    suspend fun getFunctions(): Response<List<FunctionEntity>>

    @POST("insertTrans")
    fun insertTrans(@Body info: TransEntity): Call<Void>
}