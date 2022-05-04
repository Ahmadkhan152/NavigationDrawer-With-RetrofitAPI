package com.example.navigation_drawer.Retrofit_Interfaces

import com.example.navigation_drawer.Classes.LoginResponse
import com.example.navigation_drawer.Classes.UserInfoRegister
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitInterfaceRegister {
    @POST("registration")
    fun addUser(@Body userInfoRegister: UserInfoRegister): retrofit2.Call<LoginResponse>

    companion object {
        const val baseUrl="http://restapi.adequateshop.com/api/authaccount/"
    }

}