package com.example.navigation_drawer.Retrofit_Interfaces

import com.example.navigation_drawer.Classes.LoginResponse
import com.example.navigation_drawer.Classes.UserInfoLogin
import com.example.navigation_drawer.Classes.Userinformation
import retrofit2.http.*

interface RetrofitInterfaceDeleteObject {
    @DELETE("{id}")
    fun deleteUser(@Path("id") id:Int): retrofit2.Call<Userinformation>
    companion object {
        const val baseUrl="http://restapi.adequateshop.com/api/users/"
    }
}