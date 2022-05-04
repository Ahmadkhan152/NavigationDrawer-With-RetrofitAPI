package com.example.navigation_drawer.Retrofit_Interfaces

import com.example.navigation_drawer.Classes.UserCreatedInfo
import com.example.navigation_drawer.Classes.Userinformation
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitInterfaceUserCreated {
    @POST("users")
    fun addUser(@Body userCreated: UserCreatedInfo): retrofit2.Call<Userinformation>

    companion object {
        const val baseUrl="http://restapi.adequateshop.com/api/"
    }
}