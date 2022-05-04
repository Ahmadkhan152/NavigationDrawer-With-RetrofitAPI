package com.example.navigation_drawer.Retrofit_Interfaces

import com.example.navigation_drawer.Classes.UserInfoUpdate
import com.example.navigation_drawer.Classes.Userinformation
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface RetrofitInterfaceUpdateObject {
    @PUT("{id}")
    fun updateUser(@Path("id") id:Int,@Body userInfoUpdate: UserInfoUpdate): retrofit2.Call<Userinformation>
    companion object {
        const val baseUrl="http://restapi.adequateshop.com/api/users/"
    }
}